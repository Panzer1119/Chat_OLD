/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.message;

import de.panzercraft.tab.ChatTab;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Paul
 */
public class MessageReceiveListenerImpl {
    
    public static final MessageReceiveListener messageReceiveListener_local_new = (MessageReceivedEvent mre) -> {
        final ChatTab chatTab =  mre.getChatTab();
        final String message = mre.getMessage();
        final String source = mre.getSource();
        final Instant timestamp = mre.getTimestamp();
        String timestamp_formatted = "";
        if(timestamp != null) {
            timestamp_formatted = String.format("[%s]", DateTimeFormatter.ofPattern(chatTab.getDateTimeFormat()).format(timestamp));
        }
        chatTab.addText(timestamp_formatted + source + ":" + message);
    };
    
}
