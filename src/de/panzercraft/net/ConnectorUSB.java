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
import java.time.Instant;
import org.ardulink.core.Link;
import org.ardulink.core.convenience.Links;
import org.ardulink.util.URIs;

/**
 *
 * @author Paul
 */
public class ConnectorUSB extends Connector {
    
    public static final String USERSPLITSTRING = "/";
    
    public enum Key {
        DISCONNECT  (":-:--++*9&8ÖL.äÜ*0o9K."),
        STARTED     ("setup");
        
        private final String key;
        
        Key(String key) {
            this.key = key;
        }
        
        public String getKey() {
            return key;
        }
    }
    
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
    public boolean connect(Object... options) {
        if(link != null) {
            disconnect();
        }
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
            link.addCustomListener(e -> {
                //StaticStandard.execute(() -> {
                    try {
                        StaticStandard.logErr("NEWMESSAGE:" + e.getMessage());
                        final MessageEvent me = convertFromArduino(e.getMessage(), Instant.now());
                        if(me.getMessage() == null || me.getMessage().isEmpty()) {
                            return;
                        }
                        if(me.getMessage().equals(Key.DISCONNECT.getKey())) {
                            disconnect();
                        } else if(me.getMessage().equalsIgnoreCase(Key.STARTED.getKey())) {
                            StaticStandard.log("Connected");
                        } else {
                            chatTab.receiveMessage(me);
                        }
                    } catch (Exception ex) {
                        StaticStandard.logErr("Error while receiving message: " + ex, ex);
                    }
                //});
            });
        } catch (Exception ex) {
            StaticStandard.logErr("Error while connecting to " + port + ": " + ex, ex);
        }
        return link != null;
    }

    @Override
    public boolean disconnect(Object... options) {
        sendRawMessage(Key.DISCONNECT.getKey());
        link = null;
        return true;
    }
    
    public static URI getURI(String port, int baudrate, boolean pingprobe, int waitsecs) {
        return URIs.newURI(String.format("ardulink://serial-jssc-custom?port=%s&baudrate=%d&pingprobe=%b&waitsecs=%d", port, baudrate, pingprobe, waitsecs));
    }
    
    private boolean sendRawMessage(String message) {
        try {
            link.sendCustomMessage(message);
            return true;
        } catch (Exception ex) {
            StaticStandard.logErr("Error while sending raw message: " + ex, ex);
            return false;
        }
    }
    
    @Override
    public boolean sendMessage(MessageEvent me) {
        String mm = convertToArduino(me);
        StaticStandard.log(mm);
        boolean done = sendRawMessage(mm);
        if(done) {
            chatTab.receiveMessage(me);
            chatTab.chat.textField_send.setText("");
        }
        return done;
    }
    
    public static String convertToArduino(MessageEvent me) {
        try {
            String message = "";
            message += ((ChatTab) me.getSource()).getUsername();
            message += USERSPLITSTRING;
            message += me.getMessage();
            return message;
        } catch (Exception ex) {
            //StaticStandard.logErr("Error while converting to Arduino: " + ex, ex);
            return me.getMessage();
        }
    }
    
    public static MessageEvent convertFromArduino(String text, Instant timestamp) {
        try {
            String message = text.substring(0, text.indexOf(USERSPLITSTRING));
            String source = text.substring(text.indexOf(USERSPLITSTRING) + USERSPLITSTRING.length());
            return new MessageEvent(message, source, timestamp);
        } catch (Exception ex) {
            //StaticStandard.logErr("Error while converting from Arduino: " + ex, ex);
            return new MessageEvent(text, null, timestamp);
        }
    }

}
