package MailSender.GUI;

import MailSender.CoreClasses.Smtp;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This class provides a panel displaying progress following on workers, running
 * pair with th refresher.
 */
public class UserInterfaceSendStatePanel extends JPanel {

    /**
     * Gui components
     */
    JPanel main_upload_zone = new JPanel();
    JPanel main_upload_zone_info;
    JProgressBar progressbar;
    JLabel nb_envoi_total;
    JLabel nb_envoi_actuel;
    JLabel nb_envoi_restant;
    JLabel vitesse_actuel;
    JLabel temps_ecoule;
    JLabel temps_restant;
    JTextArea trace;
    JScrollPane scroll_trace;
    JPanel Queue_zone;
    DefaultListModel envoi_queue;
    JList<Smtp> list_envoi_attente;
    JScrollPane list_envoi_scroll;
    JPanel Queue_zone_info;
    /**
     * information zone
     */
    JLabel Host;
    JLabel SmtpHost;
    JLabel SmtpPort;
    JLabel from;
    JLabel Keys;
    ListSelectionModel listSelectionModel;
    JButton play_one;
    JCheckBox play_all;
    JButton pause;
    JButton stop;
    JPanel button_control;
    /**
     * graph
     */
    UserInterfaceGraph graph = new UserInterfaceGraph(500, 10, 20, 10, 20, "mails/s");

    /**
     * Constructor.
     *
     * Generates a JPanel extended instance of UserInterfaceSendStatePanel
     */
    public UserInterfaceSendStatePanel() {

        /**
         * Initializing components
         */
        main_upload_zone = new JPanel();
        main_upload_zone_info = new JPanel();
        progressbar = new JProgressBar();
        nb_envoi_total = new JLabel("total number of sending : N/A ");
        nb_envoi_actuel = new JLabel("actual number of sending : N/A ");
        nb_envoi_restant = new JLabel("remaining number of sending : N/A ");
        vitesse_actuel = new JLabel("speed : N/A mail/s");
        temps_ecoule = new JLabel("elapsed time : N/A s");
        temps_restant = new JLabel("time remaining : N/A s");

        trace = new JTextArea();
        scroll_trace = new JScrollPane(trace);

        Queue_zone = new JPanel();
        envoi_queue = new DefaultListModel();
        list_envoi_attente = new JList<Smtp>(envoi_queue);
        list_envoi_scroll = new JScrollPane(list_envoi_attente);
        Queue_zone_info = new JPanel();

        /**
         * Information zone
         */
        Host = new JLabel();
        SmtpHost = new JLabel();
        SmtpPort = new JLabel();
        from = new JLabel();
        Keys = new JLabel();

        /**
         * Buttons zone
         */
        play_one = new JButton("Spam Them All !");
        play_all = new JCheckBox("Chain");
        pause = new JButton("Pause");
        stop = new JButton("Stop");
        button_control = new JPanel();

        /**
         * Progress Bar
         */
        progressbar.setMinimumSize(new Dimension(progressbar.getWidth(), 30));
        progressbar.setMaximumSize(new Dimension(progressbar.getWidth(), 30));
        progressbar.setPreferredSize(new Dimension(progressbar.getWidth(), 30));

        /**
         * initialization of element localization
         */
        initEnv();

        /**
         * initialization of event and bindings
         */
        initBinds();
    }

    /**
     * initialization of element localization
     */
    private void initEnv() {
        GridBagLayout gdl = new GridBagLayout();
        GridBagConstraints gdc = new GridBagConstraints();
        this.setLayout(gdl);
        gdc.fill = GridBagConstraints.BOTH;
        gdc.weightx = 1.0;
        gdc.weighty = 1.0;
        gdc.gridx = 0;
        gdc.gridy = 0;
        this.add(main_upload_zone, gdc);
        gdc.weightx = 1.0;
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 1;
        this.add(Queue_zone, gdc);
        gdc.insets = new Insets(0, 10, 5, 10);
        gdc.weightx = 1.0;
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 2;
        this.add(graph, gdc);
        Queue_zone.setPreferredSize(new Dimension(Queue_zone.getWidth(), 150));
        Queue_zone.setMaximumSize(new Dimension(Queue_zone.getWidth(), 150));
        Queue_zone.setMinimumSize(new Dimension(Queue_zone.getWidth(), 150));

        graph.setPreferredSize(new Dimension(Queue_zone.getWidth(), 100));
        graph.setMaximumSize(new Dimension(Queue_zone.getWidth(), 100));
        graph.setMinimumSize(new Dimension(Queue_zone.getWidth(), 100));

        graph.setBorder(BorderFactory.createLoweredBevelBorder());

        //NIVEAU 1 - main_zone

        GridBagLayout gdl_main = new GridBagLayout();
        GridBagConstraints gdc_main = new GridBagConstraints();
        main_upload_zone.setLayout(gdl_main);
        gdc_main.fill = GridBagConstraints.BOTH;
        gdc_main.insets = new Insets(5, 10, 5, 10);
        gdc_main.weightx = 1.0;
        gdc_main.gridx = 0;
        gdc_main.gridy = 0;
        main_upload_zone.add(main_upload_zone_info, gdc_main);
        gdc_main.insets = new Insets(5, 10, 0, 10);
        gdc_main.weighty = 0;
        gdc_main.gridx = 0;
        gdc_main.gridy = 1;
        main_upload_zone.add(progressbar, gdc_main);
        gdc_main.insets = new Insets(5, 10, 5, 10);
        gdc_main.weightx = 0;
        gdc_main.weighty = 0;
        gdc_main.gridx = 0;
        gdc_main.gridy = 2;
        gdc_main.fill = GridBagConstraints.NONE;
        gdc_main.anchor = GridBagConstraints.EAST;
        main_upload_zone.add(button_control, gdc_main);
        gdc_main.fill = GridBagConstraints.BOTH;
        gdc_main.weightx = 1.0;
        gdc_main.anchor = GridBagConstraints.CENTER;
        gdc_main.insets = new Insets(0, 10, 5, 10);
        gdc_main.weighty = 1.0;
        gdc_main.gridx = 0;
        gdc_main.gridy = 3;
        main_upload_zone.add(scroll_trace, gdc_main);

        //NIVEAU 2 - main_zone_info

        GridBagLayout gdl_main_info = new GridBagLayout();
        GridBagConstraints gdc_main_info = new GridBagConstraints();
        main_upload_zone_info.setLayout(gdl_main_info);
        gdc_main_info.fill = GridBagConstraints.BOTH;
        gdc_main_info.insets = new Insets(0, 5, 0, 0);
        gdc_main_info.weightx = 0.2;
        gdc_main_info.gridx = 0;
        gdc_main_info.gridy = 0;
        main_upload_zone_info.add(nb_envoi_total, gdc_main_info);
        gdc_main_info.gridx = 1;
        gdc_main_info.gridy = 0;
        main_upload_zone_info.add(nb_envoi_actuel, gdc_main_info);
        gdc_main_info.gridx = 2;
        gdc_main_info.gridy = 0;
        main_upload_zone_info.add(nb_envoi_restant, gdc_main_info);
        gdc_main_info.gridx = 0;
        gdc_main_info.gridy = 1;
        main_upload_zone_info.add(vitesse_actuel, gdc_main_info);
        gdc_main_info.gridx = 1;
        gdc_main_info.gridy = 1;
        main_upload_zone_info.add(temps_ecoule, gdc_main_info);
        gdc_main_info.gridx = 2;
        gdc_main_info.gridy = 1;
        main_upload_zone_info.add(temps_restant, gdc_main_info);

        // NIVEAU 1 - queue

        GridBagLayout gdl_Queue = new GridBagLayout();
        GridBagConstraints gdc_Queue = new GridBagConstraints();
        Queue_zone.setLayout(gdl_Queue);
        gdc_Queue.fill = GridBagConstraints.BOTH;
        gdc_Queue.insets = new Insets(0, 10, 10, 5);
        gdc_Queue.weightx = 1.0;
        gdc_Queue.weighty = 1.0;
        gdc_Queue.gridx = 0;
        gdc_Queue.gridy = 0;
        Queue_zone.add(list_envoi_scroll, gdc_Queue);
        gdc_Queue.insets = new Insets(0, 5, 10, 10);
        gdc_Queue.gridx = 1;
        gdc_Queue.gridy = 0;
        Queue_zone.add(Queue_zone_info, gdc_Queue);
        Queue_zone_info.setPreferredSize(new Dimension(100, Queue_zone_info.getHeight()));

        // QueueInfoZone
        GridBagLayout gdl2 = new GridBagLayout();
        GridBagConstraints gdc2 = new GridBagConstraints();
        Queue_zone_info.setLayout(gdl2);
        gdc2.fill = GridBagConstraints.BOTH;
        gdc2.gridx = 0;
        gdc2.gridy = 0;
        Queue_zone_info.add(Host, gdc2);
        gdc2.gridx = 0;
        gdc2.gridy = 1;
        Queue_zone_info.add(SmtpHost, gdc2);
        gdc2.gridx = 0;
        gdc2.gridy = 2;
        Queue_zone_info.add(SmtpPort, gdc2);
        gdc2.gridx = 0;
        gdc2.gridy = 3;
        Queue_zone_info.add(from, gdc2);
        gdc2.gridx = 0;
        gdc2.gridy = 4;
        Queue_zone_info.add(Keys, gdc2);

        //JPanel Button control
        GridBagLayout gbl_b = new GridBagLayout();
        GridBagConstraints gbc_b = new GridBagConstraints();
        button_control.setLayout(gbl_b);
        gbc_b.weightx = 1;
        gbc_b.weighty = 1;
        gbc_b.fill = GridBagConstraints.NONE;
        gbc_b.anchor = GridBagConstraints.EAST;
        gbc_b.gridx = 0;
        button_control.add(play_one, gbc_b);
        gbc_b.gridx = 1;
        button_control.add(pause, gbc_b);
        gbc_b.gridx = 2;
        button_control.add(stop, gbc_b);
        gbc_b.gridx = 3;
        button_control.add(play_all, gbc_b);

        //Borders        
        main_upload_zone_info.setBorder(BorderFactory.createTitledBorder("Infos Upload"));
        Queue_zone_info.setBorder(BorderFactory.createTitledBorder("Information of selected Upload"));
        list_envoi_attente.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }

    /**
     * initialization of event and bindings
     */
    private void initBinds() {
        ListSelectionListener listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                boolean adjust = listSelectionEvent.getValueIsAdjusting();
                if (!adjust) {
                    JList list = (JList) listSelectionEvent.getSource();
                    Object selectionValues[] = list.getSelectedValues();
                    if ((selectionValues.length == 1) && (selectionValues[0].getClass() == MailSender.CoreClasses.Smtp.class)) {

                        setInformationPanel((Smtp) selectionValues[0]);
                    } else {
                        System.out.println("Not an smtp");
                    }
                }
            }
        };

        list_envoi_attente.addListSelectionListener(listSelectionListener);
    }

    /**
     * clear labels and displayed datas in the info zone
     */
    public void clearInfoZone() {
        Host.setText("");
        SmtpHost.setText("");
        SmtpPort.setText("");
        from.setText("");
        Keys.setText("");
        list_envoi_attente.repaint();
    }

    /**
     * Displays informations from smtpObject in argument. if null , info area is
     * cleared.
     *
     * @param smtpsample
     */
    public void setInformationPanel(Smtp smtpsample) {
        if (smtpsample == null) {
            clearInfoZone();
        } else {
            Host.setText("Host : " + smtpsample.getSettings().host);
            SmtpHost.setText("Smtp Host : " + smtpsample.getSettings().smtpHost);
            SmtpPort.setText("Smtp Port : " + smtpsample.getSettings().smtpPort);
            from.setText("From : " + smtpsample.getSettings().from);
            Keys.setText("<html>Keys : <ul>" + KeyProcess(smtpsample.getKeys(), 3) + "</ul>" + "</html>");
        }
    }

    /**
     * Generate an html view of the string array passed in argument. the
     * subarray processed is arg[0, sub].
     *
     * if arg.length > sub, "..." is added at the end.
     *
     * @param arg
     * @param sub
     * @return
     */
    public String KeyProcess(String[] arg, int sub) {
        String result = "";
        for (int i = 0; i < Math.min(sub, arg.length); i++) {
            result += "<li>" + arg[i] + "</li>";
        }
        if (arg.length > sub) {
            result += "...";
        }
        return result;
    }
}