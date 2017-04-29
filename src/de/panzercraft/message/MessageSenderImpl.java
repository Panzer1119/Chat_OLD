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
    
    public static final MessageSender messageSender_echo = (MessageEvent me) -> {
        try {
            final ChatTab chatTab =  me.getChatTab();
            chatTab.chat.textField_send.setText("");
            chatTab.receiveMessage(me);
            return true;
        } catch (Exception ex) {
            StaticStandard.logErr("Error while sending message: " + ex, ex);
            return false;
        }
    };
    
}
