/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.message;

import java.time.Instant;

/**
 *
 * @author Paul
 */
public class MessageEvent {
    
    private final String message;
    private final Object source;
    private final Instant timestamp;
    
    public MessageEvent(String message, Object source, Instant timestamp) {
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
    
}
