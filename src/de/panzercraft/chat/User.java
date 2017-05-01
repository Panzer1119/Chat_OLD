/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.chat;

import java.awt.Color;

/**
 *
 * @author Paul
 */
public class User {
    
    private String username = "";
    private Color color = Color.BLACK;
    
    public User(String username) {
        this(username, Color.BLACK);
    }
    
    public User(String username, Color color) {
        this.username = username;
        this.color = color;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
}
