/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.tab;

import de.panzercraft.Chat;
import de.panzercraft.message.MessageEvent;
import de.panzercraft.net.Connector;
import de.panzercraft.net.ConnectorLocalNew;
import de.panzercraft.net.ConnectorUSB;
import de.panzercraft.util.Utils;
import jaddon.controller.StaticStandard;
import java.awt.BorderLayout;
import java.time.Instant;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 *
 * @author Paul
 */
public class ChatTab extends JPanel {
    
    public static final String UNKNOWNSOURCE = "Unknown";
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
    
    private final ArrayList<MessageEvent> messages_received = new ArrayList<>();
    private final ArrayList<MessageEvent> messages_sent = new ArrayList<>();
    
    private String tabName = "";
    private String username = System.getProperty("user.name");
    private Connector connector = null;
    
    private String dateTimeFormat = "dd.MM.yyyy HH:mm:ss";
    
    private final JTextPane textPane = new JTextPane();
    private final JScrollPane scrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    
    private boolean showTimestamp = false;
    private boolean showOnlineUser = false;
    
    public ChatTab(String tabName, ChatType chatType, Chat chat) {
        this.chat = chat;
        this.tabName = tabName;
        this.chatType = chatType;
        textPane.setEditable(false);
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
        StaticStandard.logErr("ADDTEXT:" + text);
        if(text.endsWith(LINEENDCHAR)) {
            text = text.substring(0, text.length() - LINEENDCHAR.length() - 1);
        }
        textPane.setText(text_old + (text_old.isEmpty() ? "" : LINEENDCHAR) + text);
        return this;
    }
    
    public boolean connect(Object... options) {
        if(connector == null) {
            return false;
        }
        return connector.connect(options);
    }
    
    public boolean disconnect(Object... options) {
        if(connector == null) {
            return false;
        }
        return connector.disconnect(options);
    }
    
    public ChatTab sendMessage(String message) {
        if(connector != null) {
            MessageEvent me = new MessageEvent(message, this, Instant.now());
            connector.sendMessage(me);
            messages_sent.add(me);
        }
        return this;
    }
    
    public ChatTab receiveMessage(MessageEvent me) {
        try {
            final String message = me.getMessage();
            Object source = me.getSource();
            final Instant timestamp = me.getTimestamp();
            if(source == null) {
                source = UNKNOWNSOURCE;
            } else if(source instanceof ChatTab) {
                source = ((ChatTab) source).getUsername();
            } else {
                source = source.toString();
            }
            String timestamp_formatted = "";
            if(timestamp != null && isShowTimestamp()) {
                timestamp_formatted = String.format("[%s] ", Utils.formatInstant(timestamp, getDateTimeFormat()));
            }
            final String message_complete = String.format("%s%s: %s", timestamp_formatted, source, message);
            addText(message_complete);
            messages_received.add(me);
        } catch (Exception ex) {
            StaticStandard.logErr("Error while receiving message: " + ex, ex);
        }
        return this;
    }
    
    public Thread reloadReceivedMessages() {
        clearText();
        return StaticStandard.execute(() -> {
            for(MessageEvent me : messages_received) {
                receiveMessage(me);
            }
        });
    }
    
    public ChatTab clearText() {
        textPane.setText("");
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
