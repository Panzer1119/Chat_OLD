/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft.net;

import de.panzercraft.Chat;
import de.panzercraft.message.MessageEvent;
import de.panzercraft.tab.ChatTab;
import de.panzercraft.util.COMPort;
import gnu.io.CommPortIdentifier;
import jaddon.controller.StaticStandard;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.JOptionPane;
import org.ardulink.core.Link;
import org.ardulink.core.convenience.Links;
import org.ardulink.util.URIs;

/**
 *
 * @author Paul
 */
public class ConnectorUSB extends Connector {
    
    public static final String USERSPLITSTRING = "/";
    public static final Duration MAXDURATION = Duration.ofSeconds(10);
    
    public enum Key {
        CONNECT     ("C/ONNECT"),
        DISCONNECT  ("DISC/ONNECT"),
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
    
    private boolean connected_arduino = false;
    private boolean connected_partner = false;

    public ConnectorUSB(ChatTab chatTab) {
        super(chatTab);
    }

    /**
     * Connects to an USB Decive (e.g. Arduino UNO)
     * @param options [String: COM-PORT; Integer: Baudrate; Boolean: pingprobe; Integer: waitsecs]
     * @return 
     */
    @Override
    public boolean connect() {
        if(link != null) {
            disconnect();
        }
        final ArrayList<COMPort> ports = getCOMPorts(true, CommPortIdentifier.PORT_SERIAL);
        Object input = JOptionPane.showInputDialog(Chat.frame, StaticStandard.getLang().getLang("choose_port", "Choose Port:"), StaticStandard.getLang().getLang("port_selection", "Port Selection"), JOptionPane.QUESTION_MESSAGE, null, ports.toArray(), ports.get(0));
        if(input == null) {
            return false;
        }
        if(input instanceof COMPort) {
            port = ((COMPort) input).getName();
            input = JOptionPane.showInputDialog(Chat.frame, StaticStandard.getLang().getLang("baudrate", "Baudrate:"), StaticStandard.getLang().getLang("set_baudrate", "Set Baudrate"), JOptionPane.QUESTION_MESSAGE);
            if(input == null) {
                return false;
            }
            if(input instanceof String) {
                baudrate = Integer.parseInt((String) input);
            } else {
                return false;
            }
        } else {
            return false;
        }
        return waitForConnect();
    }
    
    private boolean waitForConnect() {
        try {
            uri = getURI(port, baudrate, pingprobe, waitsecs);
            link = Links.getLink(uri);
            link.addCustomListener(e -> {
                //StaticStandard.execute(() -> {
                    try {
                        if(e.getMessage() == null || e.getMessage().isEmpty()) {
                            return;
                        }
                        String message = e.getMessage();
                        if(message.getBytes()[message.length() - 1] == new byte[]{13}[0]) {
                            message = message.substring(0, message.length() - 1);
                        }
                        final MessageEvent me = convertFromArduino(message, Instant.now());
                        if(message.equals(Key.CONNECT.getKey())) {
                            connected_partner = true;
                            StaticStandard.log("Connected to Partner");
                        } else if(message.equals(Key.DISCONNECT.getKey())) {
                            connected_partner = false;
                            StaticStandard.log("Disconnected from Partner");
                            disconnect();
                        } else if(message.equals(Key.STARTED.getKey())) {
                            connected_arduino = true;
                            StaticStandard.log("Connected to Arduino");
                        } else {
                            chatTab.receiveMessage(me);
                        }
                    } catch (Exception ex) {
                        StaticStandard.logErr("Error while receiving message: " + ex, ex);
                    }
                //});
            });
            sendRawMessage(Key.CONNECT.getKey());
            Thread.sleep(100);
            sendRawMessage(Key.CONNECT.getKey());
            Thread.sleep(100);
            Instant instant_started = Instant.now();
            while(!(connected_arduino && connected_partner) && Duration.between(instant_started, Instant.now()).compareTo(MAXDURATION) < 0) {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
            }
        } catch (Exception ex) {
            StaticStandard.logErr("Error while connecting to " + port + ": " + ex, ex);
        }
        return connected_arduino;
    }

    @Override
    public boolean disconnect() {
        sendRawMessage(Key.DISCONNECT.getKey());
        link = null;
        connected_partner = false;
        try {
            Thread.sleep(500);
        } catch (Exception ex) {
        }
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
            if(me.getSource() != null) {
                if(me.getSource() instanceof ChatTab) {
                    message += ((ChatTab) me.getSource()).getUsername();
                } else {
                    message += me.getSource().toString();
                }
            }
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
            String source = text.substring(0, text.indexOf(USERSPLITSTRING));
            String message = text.substring(text.indexOf(USERSPLITSTRING) + USERSPLITSTRING.length());
            return new MessageEvent(message, source, timestamp);
        } catch (Exception ex) {
            //StaticStandard.logErr("Error while converting from Arduino: " + ex, ex);
            return new MessageEvent(text, null, timestamp);
        }
    }
    
    public static ArrayList<COMPort> getCOMPorts(boolean whitelist, int... filter) {
        final ArrayList<COMPort> ports = new ArrayList<>();
        Enumeration pList = CommPortIdentifier.getPortIdentifiers();
        while(pList.hasMoreElements()) {
            CommPortIdentifier cpi = (CommPortIdentifier) pList.nextElement();
            boolean allowed = !whitelist;
            for(int f : filter) {
                if(cpi.getPortType() == f) {
                    allowed = whitelist;
                    break;
                }
            }
            if(!allowed) {
                continue;
            }
            COMPort port = new COMPort(cpi.getName(), cpi.getCurrentOwner());
            ports.add(port);
        }
        return ports;
    }

}
