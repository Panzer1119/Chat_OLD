/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.net;

import de.panzercraft.tab.ChatTab;

/**
 *
 * @author Paul
 */
public class ConnectorLocalNew extends Connector {

    public ConnectorLocalNew(ChatTab chatTab) {
        super(chatTab);
    }
    
    @Override
    public boolean connect(Object[] options) {
        
        return true;
    }

    @Override
    public boolean disconnect(Object[] options) {
        
        return true;
    }
    
}
