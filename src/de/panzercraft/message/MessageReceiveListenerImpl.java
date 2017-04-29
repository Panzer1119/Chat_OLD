/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.message;

import de.panzercraft.tab.ChatTab;
import de.panzercraft.util.Utils;
import jaddon.controller.StaticStandard;
import java.time.Instant;

/**
 *
 * @author Paul
 */
public class MessageReceiveListenerImpl {
    
    public static final MessageReceiveListener messageReceiveListener_local_new = (MessageEvent me) -> {
        try {
            final ChatTab chatTab =  me.getChatTab();
            final String message = me.getMessage();
            final String source = chatTab.getUsername();
            final Instant timestamp = me.getTimestamp();
            String timestamp_formatted = "";
            if(timestamp != null && chatTab.isShowTimestamp()) {
                timestamp_formatted = String.format("[%s] ", Utils.formatInstant(timestamp, chatTab.getDateTimeFormat()));
            }
            final String message_complete = String.format("%s%s: %s", timestamp_formatted, source, message);
            chatTab.addText(message_complete);
            return true;
        } catch (Exception ex) {
            StaticStandard.logErr("Error while receiving message: " + ex, ex);
            return false;
        }
    };
    
}
