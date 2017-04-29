/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.message;

import de.panzercraft.tab.ChatTab;
import jaddon.controller.StaticStandard;

/**
 *
 * @author Paul
 */
public class MessageSenderImpl {
    
    public static final MessageSender messageSender_local_new = (String message, ChatTab chatTab) -> {
        try {
            chatTab.addText(message);
            StaticStandard.log(String.format("[%s]: %s", chatTab.getTabName(), message)); //TODO Überarbeiten
            chatTab.chat.textField_send.setText("");
            return true;
        } catch (Exception ex) {
            return false;
        }
    };
    
}
