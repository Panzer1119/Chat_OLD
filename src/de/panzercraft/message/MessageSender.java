/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.message;

import de.panzercraft.tab.ChatTab;

/**
 *
 * @author Paul
 */
public interface MessageSender {
    
    public boolean sendMessage(String message, ChatTab chatTab);
    
}
