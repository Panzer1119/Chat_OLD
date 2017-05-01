/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.chat;

import de.panzercraft.Chat;
import de.panzercraft.message.Message;
import static de.panzercraft.message.Message.getUnknownUsername;
import de.panzercraft.message.MessageHyperlink;
import de.panzercraft.net.Connector;
import de.panzercraft.net.ConnectorLocalNew;
import de.panzercraft.net.ConnectorUSB;
import de.panzercraft.util.Utils;
import jaddon.controller.StaticStandard;
import java.awt.BorderLayout;
import java.awt.Color;
import java.time.Instant;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Paul
 */
public class ChatTab extends JPanel {
    
    public static final String LINEENDCHAR = "\n";
    
    public enum ChatType {
        INVALID     ("INVALID"),
        LOCAL_OLD   ("LOCAL_OLD"),
        LOCAL_NEW   ("LOCAL_NEW"),
        USB         ("USB");
        
        private final String name;
        
        ChatType(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
    }
    
    public final Chat chat;
    
    private ChatType chatType = ChatType.INVALID;
    
    private final ArrayList<Message> messages_received = new ArrayList<>();
    private final ArrayList<Message> messages_sent = new ArrayList<>();
    
    private String tabName = "";
    private String username = System.getProperty("user.name");
    private Connector connector = null;
    
    private String dateTimeFormat = "dd.MM.yyyy HH:mm:ss";
    
    private final JTextPane textPane = new JTextPane();
    private final JScrollPane scrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private final StyledDocument doc = textPane.getStyledDocument();
    private final Style style = textPane.addStyle("Style", null);
    
    private boolean showTimestamp = false;
    private boolean showOnlineUser = false;
    private boolean enabled = false;
    
    public ChatTab(String tabName, ChatType chatType, Chat chat) {
        this.chat = chat;
        this.tabName = tabName;
        this.chatType = chatType;
        textPane.setEditable(false);
        setChatEnabled(false);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        switch(chatType) {
            case INVALID:
                connector = null;
                break;
            case LOCAL_OLD:
                connector = null;
                break;
            case LOCAL_NEW:
                connector = ConnectorLocalNew.getInstance(this);
                break;
            case USB:
                connector = ConnectorUSB.getInstance(this);
                break;
            default:
                break;
        }
        clearText();
        StyleConstants.setBackground(style, Color.WHITE);
        StyleConstants.setForeground(style, Color.BLACK);
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
    }

    public String getTabName() {
        return tabName;
    }

    public ChatTab setTabName(String tabName) {
        this.tabName = tabName;
        return this;
    }

    public boolean isShowTimestamp() {
        return showTimestamp;
    }

    public ChatTab setShowTimestamp(boolean showTimestamp) {
        this.showTimestamp = showTimestamp;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ChatTab setUsername(String username) {
        this.username = username;
        return this;
    }
    
    public boolean isChatEnabled() {
        return enabled;
    }
    
    public ChatTab setChatEnabled(boolean enabled) {
        this.enabled = enabled;
        textPane.setEnabled(enabled);
        chat.updateChatTabs();
        return this;
    }

    public Connector getConnector() {
        return connector;
    }

    public ChatTab setConnector(Connector connector) {
        this.connector = connector;
        return this;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public ChatTab setChatType(ChatType chatType) {
        this.chatType = chatType;
        return this;
    }

    public JTextPane getTextPane() {
        return textPane;
    }
    public boolean isShowOnlineUser() {
        return showOnlineUser;
    }

    public ChatTab setShowOnlineUser(boolean showOnlineUser) {
        this.showOnlineUser = showOnlineUser;
        return this;
    }
    
    public ChatTab addText(String text) {
        final String text_old = textPane.getText();
        if(text.endsWith(LINEENDCHAR)) {
            text = text.substring(0, text.length() - LINEENDCHAR.length() - 1);
        }
        textPane.setText(text_old + (text_old.isEmpty() ? "" : LINEENDCHAR) + text);
        return this;
    }
    
    public boolean connect() {
        if(connector == null) {
            return false;
        }
        return connector.connect();
    }
    
    public boolean disconnect() {
        if(connector == null) {
            return false;
        }
        return connector.disconnect();
    }
    
    public ChatTab sendMessage(String message) {
        if(connector != null) {
            Message m = new Message(message, this, Instant.now());
            connector.sendMessage(m);
            messages_sent.add(m);
        }
        return this;
    }
    
    public ChatTab receiveMessage(Message m) {
        try {
            messages_received.add(m);
            addMessage(m);
        } catch (Exception ex) {
            StaticStandard.logErr("Error while receiving message: " + ex, ex);
        }
        return this;
    }
    
    private void addMessage(Message m) {
        if(m == null) {
            return;
        }
        boolean isHyperlink = m instanceof MessageHyperlink;
        boolean knownUsername = false;
        String source = m.toUsername();
        if(source.equals(username)) {
            StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT); //Eigentlich ALIGN_RIGHT
            StyleConstants.setBackground(style, Color.YELLOW);
            StyleConstants.setForeground(style, Color.BLACK);
            knownUsername = true;
        } else {
            /*
            if(onlineusers.getColors().get(name) != null) {
                StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT); //Eigentlich ALIGN_LEFT
                StyleConstants.setBackground(style, Color.WHITE);
                StyleConstants.setForeground(style, onlineusers.getColors().get(name));
                knownUsername = true;
            }
            */
        }
        if(!knownUsername) {
            StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
            StyleConstants.setBackground(style, Color.WHITE);
            StyleConstants.setForeground(style, Color.BLACK);
        }
        //Here is a <a href='http://A'>hyperlink</a>
        if(style.containsAttribute("hyperlink", true) || style.containsAttribute("hyperlink", false)) {
            style.removeAttribute("hyperlink");
        }
        style.addAttribute("hyperlink", false);
        Color backup_back = StyleConstants.getBackground(style);
        Color backup_front = StyleConstants.getForeground(style);
        try {
            if(isHyperlink) {
                /*
                StyleConstants.setBackground(style, backup_back);
                StyleConstants.setForeground(style, backup_front);
                doc.insertString(doc.getLength(), hyperlink_vor, style);



                StyleConstants.setForeground(style, tp.getBackground());
                StyleConstants.setForeground(style, Color.BLUE);
                if(style.containsAttribute("hyperlink", true) || style.containsAttribute("hyperlink", false)) {
                    style.removeAttribute("hyperlink");
                }
                style.addAttribute("hyperlink", hyperlink);
                style.addAttribute("hyperlink_to", hyperlink_to);
                style.addAttribute("hyperlink_art", hyperlink_art);
                doc.insertString(doc.getLength(), hyperlink_string, style);
                style.removeAttribute("hyperlink_to");
                style.removeAttribute("hyperlink_art");



                StyleConstants.setBackground(style, backup_back);
                StyleConstants.setForeground(style, backup_front);
                if(style.containsAttribute("hyperlink", true) || style.containsAttribute("hyperlink", false)) {
                    style.removeAttribute("hyperlink");
                }
                style.addAttribute("hyperlink", false);
                doc.insertString(doc.getLength(), hyperlink_nach + "\n", style);
                */
            } else {
                doc.insertString(doc.getLength(), m.toChat(this) + "\n", style);
            }
        } catch (Exception ex) {
            StaticStandard.logErr("Error: " + ex, ex);
        }
    }
    
    public Thread reloadReceivedMessages() {
        clearText();
        return StaticStandard.execute(() -> {
            clearText();
            for(Message m : messages_received) {
                addMessage(m);
            }
        });
    }
    
    public ChatTab clearText() {
        textPane.setText("");
        try {
            doc.remove(0, doc.getLength());
        } catch (Exception ex) {
            StaticStandard.logErr("Error while clearing TextPane: " + ex, ex);
        }
        return this;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public ChatTab setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
        return this;
    }
    
}
