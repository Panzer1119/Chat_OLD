/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.tab;

import de.panzercraft.message.MessageReceiveListener;
import de.panzercraft.message.MessageSender;
import javax.swing.JPanel;

/**
 *
 * @author Paul
 */
public class ChatTab extends JPanel {
    
    private String tabName = "";
    private MessageReceiveListener messageReceiveListener = null;
    private MessageSender messageSender = null;
    
    public ChatTab(String tabName) {
        this.tabName = tabName;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
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
    
}
