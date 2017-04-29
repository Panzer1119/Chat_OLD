/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.message;

import de.panzercraft.tab.ChatTab;
import java.time.Instant;

/**
 *
 * @author Paul
 */
public class MessageReceivedEvent {
    
    private final String message;
    private final String source;
    private final ChatTab chatTab;
    private final Instant timestamp;
    
    public MessageReceivedEvent(String message, String source, ChatTab chatTab, Instant timestamp) {
        this.message = message;
        this.source = source;
        this.chatTab = chatTab;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getSource() {
        return source;
    }

    public ChatTab getChatTab() {
        return chatTab;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
}
