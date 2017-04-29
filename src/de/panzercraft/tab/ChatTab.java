/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.tab;

import de.panzercraft.message.MessageReceiveListener;
import de.panzercraft.message.MessageSender;
import javax.swing.JPanel;
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
    
    private ChatType chatType = ChatType.INVALID;
    
    private String tabName = "";
    private MessageReceiveListener messageReceiveListener = null;
    private MessageSender messageSender = null;
    
    private final JTextPane textPane = new JTextPane();
    
    private boolean showOnlineUser = false;
    
    public ChatTab(String tabName) {
        this.tabName = tabName;
    }

    public String getTabName() {
        return tabName;
    }

    public ChatTab setTabName(String tabName) {
        this.tabName = tabName;
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
    
}
