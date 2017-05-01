/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.message;

import de.panzercraft.chat.User;
import java.time.Instant;

/**
 *
 * @author Paul
 */
public class MessageHyperlink extends Message {
    
    public MessageHyperlink(String message, User source, Instant timestamp) {
        super(message, source, timestamp);
    }
    
}
