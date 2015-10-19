package MailSender.GUI;

import MailSender.CoreClasses.DatabaseEmail;
import MailSender.CoreClasses.DatabaseHandler;
import MailSender.CoreClasses.ExceptionSender;
import MailSender.CoreClasses.SimpleSettings;
import MailSender.CoreClasses.Smtp;
import MailSender.CoreClasses.Template;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * This class generates a main panel, it contains settings, templates, keys
 * list.
 */
public class UserInterfaceMainPanel extends JPanel {

    DatabaseHandler dbSystem;
    DatabaseEmail dbEmail;
    //modal panel
    UserInterfaceChooseOnListDialog dial_keyList;
    UserInterfaceSettingsDialog dial_settings;
    String[] keyStart;
    String[] keyEnd;
    SimpleSettings result_settings;
    //organising JPanel
    JPanel panel_template = new JPanel();
    JPanel panel_template_border = new JPanel();
    JPanel panel_template_vars = new JPanel();
    //JPanel side bar
    JPanel panel_column_options = new JPanel();
    JPanel panel_options_zone_template = new JPanel();
    JPanel panel_options_zone_key = new JPanel();
    JPanel panel_options_zone_main = new JPanel();
    JPanel panel_options_zone_settings = new JPanel();
    //JPane Help 
    UserInterfaceHtmlPreviewPanel previewer = new UserInterfaceHtmlPreviewPanel();
    JScrollPane template_scroll_pane = new JScrollPane(previewer);
    //Ensemble des Jbutton repartis
    JButton options_zone_key_select = new JButton("Select Keys");
    JButton options_zone_settings = new JButton("Set Settings");
    JButton options_zone_main_envoi = new JButton("Add to send Queue");
    //List keys -> non editable directement : passage par le selectkey
    DefaultListModel listKeys = new DefaultListModel();
    DefaultListModel listKeyApp = new DefaultListModel();
    JList jlistKey = new JList(listKeyApp);
    JScrollPane jlistKey_scroll_pane = new JScrollPane(jlistKey);
    DefaultComboBoxModel<Template> templateList = new DefaultComboBoxModel<>();
    JComboBox test = new JComboBox(templateList);

    /**
     * Constructor.
     *
     * Generate an instance of UserInterfaceMainPanel.
     */
    public UserInterfaceMainPanel() {
        previewer = new UserInterfaceHtmlPreviewPanel("text/html", "");
        dial_settings = new UserInterfaceSettingsDialog(null, UserInterfaceSettingsDialog.SIMPLE, null);

        GridBagLayout alpha = new GridBagLayout();
        panel_template_border.setLayout(alpha);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10, 10, 10, 10);
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        panel_template_border.add(template_scroll_pane, c);
        c.insets = null;

        //Main grid
        GridBagLayout beta = new GridBagLayout();
        GridBagConstraints mgc = new GridBagConstraints();
        panel_column_options.setLayout(beta);
        mgc.fill = GridBagConstraints.BOTH;
        mgc.weightx = 1.0;
        mgc.weighty = 1.0;
        mgc.gridwidth = 1;
        mgc.gridx = 0;
        mgc.gridy = 0;
        panel_column_options.add(panel_options_zone_key, mgc);
        mgc.weighty = 0;
        mgc.gridx = 0;
        mgc.gridy = 1;
        panel_column_options.add(panel_options_zone_settings, mgc);
        mgc.weighty = 0;
        mgc.gridx = 0;
        mgc.gridy = 2;
        panel_column_options.add(panel_options_zone_template, mgc);
        mgc.weighty = 0;
        mgc.gridx = 0;
        mgc.gridy = 3;
        panel_column_options.add(panel_options_zone_main, mgc);

        //key grid
        panel_options_zone_key.setLayout(beta);
        GridBagConstraints kgc = new GridBagConstraints();
        kgc.insets = new Insets(10, 10, 10, 10);
        kgc.fill = GridBagConstraints.BOTH;
        kgc.weightx = 1.0;
        kgc.weighty = 0.9;
        kgc.gridwidth = 2;
        kgc.gridx = 0;
        kgc.gridy = 0;
        panel_options_zone_key.add(jlistKey_scroll_pane, kgc);
        kgc.insets = new Insets(0, 10, 10, 10);
        kgc.weightx = 1.0;
        kgc.weighty = 0;
        kgc.ipady = 5;
        kgc.gridwidth = 1;
        kgc.gridx = 0;
        kgc.gridy = 1;
        panel_options_zone_key.add(options_zone_key_select, kgc);

        //template grid
        panel_options_zone_template.setLayout(beta);
        GridBagConstraints tgc = new GridBagConstraints();
        tgc.fill = GridBagConstraints.BOTH;
        tgc.insets = new Insets(10, 10, 10, 10);
        tgc.weightx = 1.0;
        tgc.weighty = 1.0;
        tgc.gridwidth = 4;
        tgc.ipady = 5;
        tgc.gridx = 0;
        tgc.gridy = 0;
        panel_options_zone_template.add(test, tgc);

        //main_main grid
        panel_options_zone_main.setLayout(beta);
        GridBagConstraints main_c = new GridBagConstraints();
        main_c.insets = new Insets(10, 10, 10, 10);
        main_c.fill = GridBagConstraints.BOTH;
        main_c.weightx = 1.0;
        main_c.weighty = 0;
        main_c.gridwidth = 2;
        main_c.gridx = 0;
        main_c.gridy = 0;
        main_c.ipady = 40;
        panel_options_zone_main.add(options_zone_main_envoi, main_c);

        //main_main settings 
        panel_options_zone_settings.setLayout(beta);
        GridBagConstraints main_s = new GridBagConstraints();
        main_s.insets = new Insets(10, 10, 10, 10);
        main_s.fill = GridBagConstraints.BOTH;
        main_s.weightx = 1.0;
        main_s.weighty = 0;
        main_s.gridx = 0;
        main_s.gridy = 0;
        main_s.ipady = 5;
        panel_options_zone_settings.add(options_zone_settings, main_s);

        //inclusions mineures
        GridBagLayout template_layout = new GridBagLayout();
        GridBagConstraints solo = new GridBagConstraints();
        panel_template.setLayout(template_layout);
        solo.fill = GridBagConstraints.BOTH;
        solo.weightx = 1.0;
        solo.weighty = 1.0;
        solo.gridwidth = 1;
        solo.gridx = 0;
        solo.gridy = 1;
        panel_template.add(panel_template_border, solo);

        //inclusions majeures        
        GridBagLayout ender = new GridBagLayout();
        this.setLayout(ender);
        GridBagConstraints supgc = new GridBagConstraints();
        supgc.fill = GridBagConstraints.BOTH;
        supgc.insets = new Insets(5, 5, 5, 5);
        supgc.weightx = 1.0;
        supgc.weighty = 1.0;
        supgc.gridwidth = 1;
        supgc.gridx = 0;
        supgc.gridy = 0;
        this.add(panel_template, supgc);
        supgc.weightx = 0;
        supgc.gridx = 1;
        supgc.gridy = 0;
        this.add(panel_column_options, supgc);
        panel_column_options.setMinimumSize(new Dimension(250, panel_column_options.getHeight()));
        panel_column_options.setPreferredSize(new Dimension(250, panel_column_options.getHeight()));
        panel_column_options.setMaximumSize(new Dimension(250, panel_column_options.getHeight()));

        //borders
        panel_template_border.setBorder(BorderFactory.createTitledBorder(" Get Started "));
        panel_options_zone_key.setBorder(BorderFactory.createTitledBorder(" Key "));
        panel_options_zone_settings.setBorder(BorderFactory.createTitledBorder(" Setings "));
        panel_options_zone_template.setBorder(BorderFactory.createTitledBorder(" Template Set "));
        panel_options_zone_main.setBorder(BorderFactory.createTitledBorder(" Main "));

        //Bindings and events
        options_zone_settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                event_settings(ae);
            }
        });
        options_zone_key_select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                event_keys(ae);
            }
        });

    }

    /**
     * Event. opens the settings dialog.
     *
     * @param ae
     */
    private void event_settings(ActionEvent ae) {
        result_settings = dial_settings.ShowDialog();
    }

    /**
     * Event. open the listChooser.
     *
     * @param ae
     */
    private void event_keys(ActionEvent ae) {
        dial_keyList = new UserInterfaceChooseOnListDialog(null, "Key select", listKeys, listKeyApp, true);
        DefaultListModel a = dial_keyList.ShowDialog();
        if (a != null) {
            listKeyApp = a;
            //to array -> a revisiter
            Object[] testba = a.toArray();
            keyEnd = new String[testba.length];
            for (int i = 0; i < testba.length; i++) {
                if (testba[i].getClass() == String.class) {
                    keyEnd[i] = (String) testba[i];

                } else {
                    keyEnd[i] = "rate";
                }
            }
        }
    }

    /**
     * return the template selected in the comboBox.
     * 
     * @return Template, 
     */
    public Template getSelectedTemplate() {
        //Cast between systems. errors
        if (test.getSelectedItem().getClass() == Template.class) {
            return (Template) test.getSelectedItem();
        } else {
            return null;
        }
    }

    /**
     * give a reference of the dbSystem. MUST BE setted.
     * this is set here beacause the constructor copy the instance instead of 
     * taking reference.
     * 
     * @param dbSystem
     */
    public void setDatabaseHandler(DatabaseHandler dbSystem) {
        this.dbSystem = dbSystem;
        refreshTemplateList();
    }

    /**
     * give a reference of the dbemail. MUST BE setted.
     * this is set here beacause the constructor copy the instance instead of 
     * taking reference.
     * 
     * @param dbEmail
     */
    public void setDatabaseEmail(DatabaseEmail dbEmail) {
        this.dbEmail = dbEmail;
    }

    /**
     * refresh the combox box and the list behind.
     */
    public void refreshTemplateList() {
        DefaultComboBoxModel<Template> essai = (DefaultComboBoxModel<Template>) test.getModel();
        essai.removeAllElements();
        if (this.dbSystem.getTemplateList() != null) {
            ArrayList<Template> templates = this.dbSystem.getTemplateList();
            for (Template template : templates) {
                essai.addElement(template);
            }
        }
    }

    /**
     * Generate the mail list from the key list.
     * 
     * @param Key
     * @return
     * @throws ExceptionSender
     */
    public String[] mailFromKeys(String[] Key) throws ExceptionSender {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < Key.length; i++) {
            list.add(Key[i]);
        }
        if (this.dbEmail != null) {
            ArrayList<String> list2 = this.dbEmail.getMailAdressesByKeywords(list);
            String[] Systemtest = new String[0];
            return list2.toArray(Systemtest);
        }
        throw new ExceptionSender("dbEmail est null");
    }

    /**
     * return the smtp object from fields in this Instance.  
     * 
     * @return @throws ExceptionSender
     */
    public Smtp getSmtpObject() throws ExceptionSender {
        // attention, on ne cree que si la structure est correcte.
        SimpleSettings settings_local = result_settings;
        Template template_local = getSelectedTemplate();
        String[] mail_list_local = new String[0];
        try {
            mail_list_local = mailFromKeys(keyEnd);
        } catch (ExceptionSender ex) {
            JOptionPane.showMessageDialog(null, "Unable to read the mails from the database given : " + ex, "error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (settings_local == null) {
            JOptionPane.showMessageDialog(null, "The options for the shipment were not configured ", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (template_local == null) {
            JOptionPane.showMessageDialog(null, "No sending template has been selected ", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if ((keyEnd == null) || (keyEnd.length == 0)) {
            JOptionPane.showMessageDialog(null, "Key words list is empty.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (mail_list_local.length == 0) {
            JOptionPane.showMessageDialog(null, "Mail list is empty.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return new Smtp(settings_local, template_local, mail_list_local, keyEnd, false);
    }

    /**
     * set Html viewer to the String content.
     *
     * @param content
     */
    public void updateRenderHtml(String content) {
        template_scroll_pane.setViewportView(new UserInterfaceHtmlPreviewPanel("text/html", content));
    }
}
