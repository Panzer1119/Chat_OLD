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

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public User setColor(Color color) {
        this.color = color;
        return this;
    }
    
    @Override
    public String toString() {
        return username;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof User) {
            return ((User) o).toString().equals(toString());
        } else {
            return false;
        }
    }
    
}
