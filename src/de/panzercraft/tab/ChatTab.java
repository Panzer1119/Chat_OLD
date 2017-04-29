/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.tab;

import de.panzercraft.Chat;
import de.panzercraft.message.MessageEvent;
import de.panzercraft.message.MessageReceiveListener;
import de.panzercraft.message.MessageReceiveListenerImpl;
import de.panzercraft.message.MessageSender;
import de.panzercraft.message.MessageSenderImpl;
import de.panzercraft.net.Connector;
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
    private MessageReceiveListener messageReceiveListener = null;
    private MessageSender messageSender = null;
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
                messageReceiveListener = null;
                messageSender = null;
                break;
            case LOCAL_OLD:
                messageReceiveListener = null;
                messageSender = null;
                break;
            case LOCAL_NEW:
                messageReceiveListener = MessageReceiveListenerImpl.messageReceiveListener_local_new;
                messageSender = MessageSenderImpl.messageSender_echo;
                break;
            case USB:
                messageReceiveListener = null;
                messageSender = null;
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
    
    public MessageReceiveListener getMessageReceiveListener() {
        return messageReceiveListener;
    }

    public ChatTab setMessageReceiveListener(MessageReceiveListener messageReceiveListener) {
        this.messageReceiveListener = messageReceiveListener;
        return this;
    }

    public MessageSender getMessageSender() {
        return messageSender;
    }

    public ChatTab setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
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
        textPane.setText(text_old + (text_old.isEmpty() ? "" : "\n") + text);
        return this;
    }
    
    public ChatTab sendMessage(String message) {
        if(messageSender != null) {
            MessageEvent me = new MessageEvent(message, this, Instant.now());
            messageSender.sendMessage(me);
            messages_sent.add(me);
        }
        return this;
    }
    
    public ChatTab receiveMessage(MessageEvent me) {
        if(messageReceiveListener != null) {
            messageReceiveListener.messageReceived(me);
            messages_received.add(me);
        }
        return this;
    }
    
    public Thread reloadReceivedMessages() {
        if(messageReceiveListener == null) {
            return null;
        }
        clearText();
        Runnable run = () -> {
            for(MessageEvent me : messages_received) {
                messageReceiveListener.messageReceived(me);
            }
        };
        return StaticStandard.execute(run);
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
