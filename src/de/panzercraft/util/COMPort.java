/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.util;

/**
 *
 * @author Paul
 */
public class COMPort {
    
    private final String name;
    private final String device;
    
    public COMPort(String name, String device) {
        this.name = name;
        this.device = device;
    }

    public String getName() {
        return name;
    }

    public String getDevice() {
        return device;
    }

    @Override
    public String toString() {
        return String.format("%s%s", name, ((device != null && !device.isEmpty()) ? ("(" + device + ")") : ""));
    }
    
}
