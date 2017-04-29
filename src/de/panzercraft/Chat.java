/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.panzercraft;

import jaddon.controller.JAddOnStandard;
import jaddon.controller.JFrameManager;
import jaddon.controller.StandardMethods;
import jaddon.controller.StaticStandard;
import jaddon.utils.JUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

/**
 *
 * @author Paul
 */
public class Chat implements ActionListener, StandardMethods, WindowListener {
    
    public static final String PROGRAMNAME = "Chat";
    public static final String VERSION = "2.0";
    
    public static final String ICONMAIN = "/de/panzercraft/icons/backup_manager.png";
    
    //Java Swing Main Start
    public static final JFrameManager frame = new JFrameManager(PROGRAMNAME, VERSION);
    private final JAddOnStandard standard = new JAddOnStandard(PROGRAMNAME, VERSION, true, true, false, true, true);
    private final JTabbedPane tp = new JTabbedPane();
    private final JMenuBar MB1 = new JMenuBar();
    private final JMenu M1 = new JMenu("File");
    private final JMenu M2 = new JMenu("Edit");
    private final JMenuItem M1I1 = new JMenuItem("Exit");
    private final JMenuItem M1I2 = new JMenuItem("Restart");
    private final JMenuItem M2I1 = new JMenuItem("Change Language");
    
    private boolean init = false;
    
    public Chat() {
        init();
        //StaticStandard.getUpdater().start(); //FIXME TURN THIS ON
    }

    public static void main(String[] args) {
        Chat x = new Chat();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == M1I1) {
            exit();
        } else if(e.getSource() == M1I2) {
            JUtils.restart();
        } else if(e.getSource() == M2I1) {
            StaticStandard.getLang().changeLanguage();
            StaticStandard.getConfig().setProperty("lang", StaticStandard.getLang().getLang());
            StaticStandard.getConfig().saveConfig();
            reloadLang();
        } else {
            /*
            for(Tab tab : tabs) {
                tab.actionPerformed(e);
            }
            */
        }
    }
    
    private void initFrame() {
        M1I1.addActionListener(this);
        M1I2.addActionListener(this);
        M2I1.addActionListener(this);
        M1.add(M1I2);
        M1.add(M1I1);
        M2.add(M2I1);
        MB1.add(M1);
        MB1.add(M2);
        frame.setJMenuBar(MB1);
        //addTabs();
        //initTabs();
        frame.add(tp, BorderLayout.CENTER);
    }

    @Override
    public boolean init() {
        if(init) {
            return false;
        }
        /*
        try {
            for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                StaticStandard.log(info.getName() + " : " + info.getClassName());
            }
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
            StaticStandard.logErr("Error while setting system default look and feel: " + ex);
        }
        */
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex2) {
        }
        StaticStandard.getLogger().setLoggingOnlyIfIDE(false);
            StaticStandard.getLogger().setLoggingOnlyIfIDE(false);
            StaticStandard.getLogger().setPrintTimestamp(true);
            StaticStandard.getLogger().setPrintExtraInformation(true);
            StaticStandard.getLogger().enableOutputStream();
        //    StaticStandard.getLogger().enableErrorOutputStream();
            StaticStandard.getLogger().enableInputStream();
        int id = frame.addWork(String.format(StaticStandard.getLang().getLang("work_initialation_phase", "Initiation phase %d..."), 1), false);
        StaticStandard.getLogger().log("Initiation phase 1 started");
        standard.setDoUpdate(true);
        StaticStandard.getUpdater().setFileTemp(StaticStandard.getConfig().getTempFolder());
        StaticStandard.getUpdater().loadURLsFromInternResource("/de/panzercraft/urls/updaterURLs.txt");
        StaticStandard.getConfig().setDefaultConfig(new String[] {"lang"}, new String[] {"EN"});
        StaticStandard.getLang().setClassReference(Chat.class);
        StaticStandard.getLang().setFile("/de/panzercraft/lang");
        StaticStandard.getConfig().reloadConfig();
        StaticStandard.getConfig().setDoUpdate(true);
        StaticStandard.getConfig().update();
        StaticStandard.getLang().setDoUpdate(true);
        StaticStandard.getLang().update();
        StaticStandard.getLogger().setIsIDE(StaticStandard.isIsIDE());
        StaticStandard.getLogger().setDoUpdate(true);
        StaticStandard.getLogger().update();
        frame.setIconImage(ICONMAIN);
        reloadConfig();
        frame.delWork(id);
        StaticStandard.getLogger().log("Initiation phase 1 completed successfully");
        id = frame.addWork(String.format(StaticStandard.getLang().getLang("work_initialation_phase", "Initiation phase %d..."), 2), false);
        StaticStandard.getLogger().log("Initiation phase 2 started");
        frame.setSize(new Dimension(700, 600));
        frame.setDefaultCloseOperation(JFrameManager.DO_NOTHING_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.addWindowListener(this);
        //frame.addMouseListener(this);
        initFrame();
        frame.pack();
        frame.setLocationRelativeTo(null);
        reloadLang();
        frame.setVisible(true);
        frame.delWork(id);
        StaticStandard.getLogger().log("Initiation phase 3 completed successfully");
        //timer.start();
        init = true;
        return true;
    }

    @Override
    public boolean reloadConfig() {
        StaticStandard.getConfig().reloadConfig();
        StaticStandard.getLang().setLang(StaticStandard.getConfig().getProperty("lang", "EN"));
        /*
        for(Tab tab : tabs) {
            tab.update();
        }
        */
        /*
        folder_categories = new File(StaticStandard.getConfig().getConfigFolder().getAbsolutePath() + File.separator + "backuptasks");
        folder_categories.mkdirs();
        folder_data = new File(StaticStandard.getConfig().getConfigFolder().getAbsolutePath() + File.separator + "data");
        folder_data.mkdirs();
        file_expanded_paths = new File(folder_data.getAbsolutePath() + File.separator + "expandedPaths.txt");
        try {
            file_expanded_paths.createNewFile();
        } catch (Exception ex) {
            StaticStandard.logErr("Error while creating file_expanded_paths file: " + ex);
        }
        int timer_speed_old = timer_speed;
        timer_speed = Integer.parseInt(StaticStandard.getConfig().getProperty("timer_speed", "1000"));
        if(timer_speed != timer_speed_old) {
            setTimer(timer_speed);
        }
        reloadBackupTasks();
        */
        return true;
    }

    @Override
    public boolean reloadLang() {
        StaticStandard.getConfig().reloadConfig();
        StaticStandard.getLang().setLang(StaticStandard.getConfig().getProperty("lang", "EN"));
        StaticStandard.getLang().reloadLang();
        StaticStandard.getLogger().update();
        int i = 0;
        /*
        for(Tab tab : tabs) {
            tab.reloadLang();
            tab.update();
            tp.setTitleAt(i, tab.getLocalizedName());
            i++;
        }
        */
        M1.setText(StaticStandard.getLang().getLang("file", "File"));
        M2.setText(StaticStandard.getLang().getLang("edit", "Edit"));
        /*
        M2.setText(StaticStandard.getLang().getLang("extras", "Extras"));
        M3.setText(StaticStandard.getLang().getLang("backup", "Backup"));
        */
        M1I1.setText(StaticStandard.getLang().getLang("exit", "Exit"));
        M1I2.setText(StaticStandard.getLang().getLang("restart", "Restart"));
        M2I1.setText(StaticStandard.getLang().getLang("change_language", "Change Language"));
        /*
        M2I1.setText(StaticStandard.getLang().getLang("change_language", "Change Language"));
        M2I2.setText(StaticStandard.getLang().getLang("reload", "Reload"));
        M2I3.setText(StaticStandard.getLang().getLang("settings", "Settings"));
        M3I1.setText(StaticStandard.getLang().getLang("add_backup_task", "Add Backup Task"));
        M3I2.setText(StaticStandard.getLang().getLang("remove_backup_task", "Remove Backup Task"));
        PM1I1.setText(StaticStandard.getLang().getLang("execute_backup", "Execute Backup Task"));
        PM1I2.setText(StaticStandard.getLang().getLang("properties", "Properties"));
        PM1I3.setText(StaticStandard.getLang().getLang("delete", "Delete"));
        PM1I4.setText(StaticStandard.getLang().getLang("add_backup_task", "Add Backup Task"));
        PM1I5.setText(StaticStandard.getLang().getLang("add_category", "Add Category"));
        PM1I6.setText(StaticStandard.getLang().getLang("recover_backup", "Recover Backup"));
        */
        return true;
    }

    @Override
    public void exit() {
        while(true) {
            try {
                System.exit(0);
            } catch (Exception ex) {
                System.exit(-1);
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if(e.getSource() == frame) {
            exit();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
    
}
