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
public class ConnectorLocalNew extends Connector {

    public static Connector getInstance(ChatTab chatTab) {
        return new ConnectorLocalNew(chatTab);
    }
    
    public ConnectorLocalNew(ChatTab chatTab) {
        super(chatTab);
    }
    
    @Override
    public boolean connect() {
        
        return true;
    }

    @Override
    public boolean disconnect() {
        
        return true;
    }

    @Override
    public boolean sendMessage(Message m) {
        return false;
    }
    
}
