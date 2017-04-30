/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.net;

import de.panzercraft.message.MessageEvent;
import de.panzercraft.tab.ChatTab;
import jaddon.controller.StaticStandard;
import java.net.URI;
import org.ardulink.core.Link;
import org.ardulink.core.convenience.Links;
import org.ardulink.util.URIs;

/**
 *
 * @author Paul
 */
public class ConnectorUSB extends Connector {
    
    public static Connector getInstance(ChatTab chatTab) {
        return new ConnectorUSB(chatTab);
    }

    private Link link = null;
    private URI uri = null;
    private String port = "COM3";
    private int baudrate = 9600;
    private boolean pingprobe = false;
    private int waitsecs = 1;

    public ConnectorUSB(ChatTab chatTab) {
        super(chatTab);
    }

    /**
     * Connects to an USB Decive (e.g. Arduino UNO)
     * @param options [String: COM-PORT; Integer: Baudrate; Boolean: pingprobe; Integer: waitsecs]
     * @return 
     */
    @Override
    public boolean connect(Object[] options) {
        if(options[0] instanceof String) {
            if(options.length == 1) {
                port = (String) options[0];
            } else {
                if(options[1] instanceof Integer) {
                    switch (options.length) {
                        case 2:
                            baudrate = (Integer) options[1];
                            break;
                        case 4:
                            if(options[2] instanceof Boolean && options[3] instanceof Integer) {
                                pingprobe = (Boolean) options[2];
                                waitsecs = (Integer) options[3];
                            } else {
                                return false;
                            }   break;
                        default:
                            return false;
                    }
                }
            }
        } else {
            return false;
        }
        try {
            uri = getURI(port, baudrate, pingprobe, waitsecs);
            link = Links.getLink(uri);
        } catch (Exception ex) {
            StaticStandard.logErr("Error while connecting to " + port + ": " + ex, ex);
        }
        return link != null;
    }

    @Override
    public boolean disconnect(Object[] options) {
        
        return true;
    }
    
    public static URI getURI(String port, int baudrate, boolean pingprobe, int waitsecs) {
        return URIs.newURI(String.format("ardulink://serial-jssc-custom?port=%s&baudrate=%d&pingprobe=%b&waitsecs=%d", port, baudrate, pingprobe, waitsecs));
    }
    
    @Override
    public boolean sendMessage(MessageEvent me) {
        return false;
    }

}
