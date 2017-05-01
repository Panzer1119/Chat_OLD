/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.net;

import de.panzercraft.message.Message;
import de.panzercraft.chat.ChatTab;

/**
 *
 * @author Paul
 */
public abstract class Connector {
    
    protected final ChatTab chatTab;
    
    public Connector(ChatTab chatTab) {
        this.chatTab = chatTab;
    }
    
    public abstract boolean connect();
    public abstract boolean disconnect();
    public abstract boolean sendMessage(Message m);
    
    public String getName() {
        return getClass().getSimpleName();
    }

    public ChatTab getChatTab() {
        return chatTab;
    }
    
}
