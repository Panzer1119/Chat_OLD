/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.message;

import de.panzercraft.chat.ChatTab;
import de.panzercraft.util.Utils;
import jaddon.controller.StaticStandard;
import java.io.Serializable;
import java.time.Instant;

/**
 *
 * @author Paul
 */
public class Message implements Serializable {
    
    private final String message;
    private final Object source;
    private final Instant timestamp;
    
    public Message(String message, Object source, Instant timestamp) {
        this.message = message;
        this.source = source;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }
    
    public Object getSource() {
        return source;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s%s: %s", timestamp, source, message);
    }
    
    public String toChat(ChatTab chatTab) {
        String user = "";
        if(source == null) {
            user = getUnknownUsername();
        } else if(source instanceof ChatTab) {
            user = ((ChatTab) source).getUsername();
        } else {
            user = source.toString();
        }
        String timestamp_formatted = "";
        if(timestamp != null && chatTab.isShowTimestamp()) {
            timestamp_formatted = String.format("[%s] ", Utils.formatInstant(timestamp, chatTab.getDateTimeFormat()));
        }
        return String.format("%s%s: %s", timestamp_formatted, user, message);
    }
    
    public String toUsername() {
        if(source == null) {
            return getUnknownUsername();
        } else {
            if(source instanceof ChatTab) {
                return ((ChatTab) source).getUsername();
            } else {
                return source.toString();
            }
        }
    }
    
    public static String getUnknownUsername() {
        return StaticStandard.getLang().getLang("unknown", "Unknown");
    }
    
}
