/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft;

import jaddon.controller.JFrameManager;

/**
 *
 * @author Paul
 */
public class Chat {
    
    public static final String PROGRAMNAME = "Chat";
    public static final String VERSION = "2.0";
    
    private final JFrameManager frame = new JFrameManager(PROGRAMNAME, VERSION);
    
    public Chat() {
        
    }

    public static void main(String[] args) {
        Chat x = new Chat();
    }
    
}
