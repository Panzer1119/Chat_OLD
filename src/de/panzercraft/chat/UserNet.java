/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.chat;

import java.awt.Color;
import java.net.InetAddress;

/**
 *
 * @author Paul
 */
public class UserNet extends User {
    
    private InetAddress inetaddress = null;
    private int port = 0;
    
    public UserNet(String username) {
        this(username, Color.BLACK);
    }
    
    public UserNet(String username, Color color) {
        super(username, color);
    }

    public InetAddress getInetaddress() {
        return inetaddress;
    }

    public UserNet setInetaddress(InetAddress inetaddress) {
        this.inetaddress = inetaddress;
        return this;
    }

    public int getPort() {
        return port;
    }

    public UserNet setPort(int port) {
        this.port = port;
        return this;
    }
    
}
