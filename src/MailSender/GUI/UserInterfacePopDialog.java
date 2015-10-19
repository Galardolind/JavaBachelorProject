package MailSender.GUI;

import MailSender.CoreClasses.DatabaseEmail;
import MailSender.CoreClasses.DatabaseHandler;
import MailSender.CoreClasses.ExceptionSender;
import MailSender.CoreClasses.PopCompleteProcess;
import MailSender.CoreClasses.PopSettings;
import MailSender.CoreClasses.PopStatus;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This class provides a pop system to retrieve mail return. unsuscribing
 * return. rebounces retrun.
 */
public class UserInterfacePopDialog extends JDialog {

    /**
     * JComponents
     */
    public JLabel popLabel;
    public JTextField popField;
    public JLabel portLabel;
    public JTextField portField;
    public JLabel userLabel;
    public JLabel passLabel;
    public JTextField userField;
    public JPasswordField passField;
    public JButton collectButton;
    public JTextArea collectTrace;
    public JProgressBar collectProgess;
    public JLabel collectLabel;
    public JTextField collectOne;
    public JButton collectAdd;
    public JScrollPane collectTraceScroll;
    public JButton flushButton;
    public JLabel flushLabel;
    public JPanel fieldPanel;
    public JPanel collectPanel;
    public JPanel flushPanel;
    /**
     *
     */
    public PopStatus status;
    public PopCompleteProcess popProcess;
    private DatabaseEmail maildb;
    private DatabaseHandler dbSystem;

    /**
     * Constructor.
     *
     * Generate an instance of UserInterfacePopDialog, this dialog is modal and
     * disable user inputs in calling frame.
     *
     * @param owner
     */
    public UserInterfacePopDialog(Dialog owner) {
        super(owner, "Popotor", true);

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows."
                    + "WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }



        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        popLabel = new JLabel("Pop : ");
        popField = new JTextField("");
        portLabel = new JLabel("Port :");
        portField = new JTextField("");
        userLabel = new JLabel("User : ");
        passLabel = new JLabel("Password : ");
        userField = new JTextField("");
        passField = new JPasswordField("");

        collectButton = new JButton("Collect");
        collectLabel = new JLabel("unprocesssed : ");
        collectProgess = new JProgressBar();
        collectTrace = new JTextArea();
        Font font = new Font("sansserif", Font.PLAIN, 10);
        //Font font = collectTrace.getFont();
        collectTraceScroll = new JScrollPane(collectTrace);
        collectTrace.setFont(font.deriveFont(10.0f));
        collectTrace.setEditable(false);
        collectOne = new JTextField();
        collectAdd = new JButton("Add to flush List");

        flushLabel = new JLabel("<html>Warning !! this action will flush all emails listed above from the data base.<br />"
                + " This operation can't be undone</html>");
        flushLabel.setForeground(Color.red);
        flushButton = new JButton("Flush Mails");
        flushButton.setForeground(Color.red);

        flushPanel = new JPanel();
        fieldPanel = new JPanel();
        collectPanel = new JPanel();

        makeUI();

        flushButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processFlush();
            }
        });

        collectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processRecup();
            }
        });
        collectAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processAdd();
            }
        });





    }
    
    /**
     * generate UI components and localisations and general comportement
     */
    private void makeUI() {
        this.setLocationRelativeTo(null);

        GridBagConstraints gdc = new GridBagConstraints();
        GridBagLayout gdl = new GridBagLayout();

        this.setLayout(gdl);
        gdc.fill = GridBagConstraints.BOTH;
        gdc.insets = new Insets(5, 5, 5, 5);
        gdc.weightx = 1.0;
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 0;
        this.add(fieldPanel, gdc);
        gdc.weighty = 1.0;
        gdc.gridx = 0;
        gdc.gridy = 1;
        this.add(collectPanel, gdc);
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 2;
        this.add(flushPanel, gdc);

        fieldPanel.setLayout(gdl);
        gdc.weighty = 0;
        gdc.weightx = 0;
        gdc.gridx = 0;
        gdc.gridy = 0;
        fieldPanel.add(popLabel, gdc);
        gdc.weightx = 1.0;
        gdc.gridx = 1;
        gdc.gridy = 0;
        fieldPanel.add(popField, gdc);
        gdc.weightx = 0;
        gdc.gridx = 0;
        gdc.gridy = 1;
        fieldPanel.add(portLabel, gdc);
        gdc.weightx = 1.0;
        gdc.gridx = 1;
        gdc.gridy = 1;
        fieldPanel.add(portField, gdc);
        gdc.weightx = 0;
        gdc.gridx = 0;
        gdc.gridy = 2;
        fieldPanel.add(userLabel, gdc);
        gdc.weightx = 1.0;
        gdc.gridx = 1;
        gdc.gridy = 2;
        fieldPanel.add(userField, gdc);
        gdc.weightx = 0;
        gdc.gridx = 0;
        gdc.gridy = 3;
        fieldPanel.add(passLabel, gdc);
        gdc.weightx = 1.0;
        gdc.gridx = 1;
        gdc.gridy = 3;
        fieldPanel.add(passField, gdc);

        collectPanel.setLayout(gdl);
        gdc.weightx = 0;
        gdc.gridx = 1;
        gdc.gridy = 0;
        collectPanel.add(collectButton, gdc);
        gdc.weightx = 1.0;
        gdc.gridx = 0;
        gdc.gridy = 0;
        collectPanel.add(collectProgess, gdc);
        gdc.gridwidth = 2;
        gdc.weighty = 1.0;
        gdc.gridx = 0;
        gdc.gridy = 1;
        collectPanel.add(collectTraceScroll, gdc);
        gdc.weighty = 0;
        gdc.weightx = 1.0;
        gdc.gridwidth = 1;
        gdc.gridx = 0;
        gdc.gridy = 2;
        collectPanel.add(collectOne, gdc);
        gdc.weightx = 0;
        gdc.gridx = 1;
        gdc.gridy = 2;
        collectPanel.add(collectAdd, gdc);


        flushPanel.setLayout(gdl);
        gdc.weightx = 1.0;
        gdc.gridwidth = 1;
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 0;
        flushPanel.add(flushLabel, gdc);
        gdc.gridx = 0;
        gdc.gridy = 1;
        flushPanel.add(flushButton, gdc);

        flushPanel.setBorder(BorderFactory.createTitledBorder("Flush mails"));
        collectPanel.setBorder(BorderFactory.createTitledBorder("Collect mail Box"));
        fieldPanel.setBorder(BorderFactory.createTitledBorder("Pop Settings"));

        this.setMinimumSize(new Dimension(350, 600));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int) (((screenSize.getWidth() / 2.0) - (this.getWidth() / 2.0))), (int) (((screenSize.getHeight() / 2.0) - (this.getHeight() / 2.0))));

        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent we) {
            }

            @Override
            public void windowClosing(WindowEvent we) {
                if (dbSystem != null) {
                    PopSettings pop = new PopSettings(popField.getText(), portField.getText(), userField.getText(), new String(passField.getPassword()));
                    dbSystem.saveUserPopSettings(pop);
                }
            }

            @Override
            public void windowClosed(WindowEvent we) {
            }

            @Override
            public void windowIconified(WindowEvent we) {
            }

            @Override
            public void windowDeiconified(WindowEvent we) {
            }

            @Override
            public void windowActivated(WindowEvent we) {
            }

            @Override
            public void windowDeactivated(WindowEvent we) {
            }
        });

    }
    
    /**
     * This function launch the mail recuperation of the pop.
     */
    public void processRecup() {
        flushButton.setEnabled(false); //on coupe l'acces concurentiel ... sinon c'est giga merde en boucle
        collectAdd.setEnabled(false);
        collectButton.setEnabled(false);

        log("Popotor started...\n");

        try {
            log("Checking coordinates...\n");

            //test de validite
            if (!(popField.getText() != null && !("".equals(popField.getText())))) {
                throw new ExceptionSender("provider is not initialized");
            }
            if (!(portField.getText() != null && !("".equals(portField.getText())))) {
                throw new ExceptionSender("port is not initialized");
            }
            try {
                int portInt = Integer.parseInt(portField.getText());
                if (portInt > 65535 || portInt < 0) {
                    throw new ExceptionSender("port must belongs to range [0, 65535]");
                }
            } catch (NumberFormatException nFE) {
                throw new ExceptionSender("port is not an Integer");
            }
            if (!(userField.getText() != null && !("".equals(userField.getText())))) {
                throw new ExceptionSender("user is not initialized");
            }
            if (passField.getPassword() == null) {
                throw new ExceptionSender("pass is not initialized");
            }
            //fin des test de validite de premier ordre

            //on initialise le pop           
            popProcess = new PopCompleteProcess(popField.getText(), portField.getText(), userField.getText(), new String(passField.getPassword()), this);
            popProcess.setStatus(status);
            popProcess.start();

        } catch (Exception me) {
            me.printStackTrace();
            log("Error has been caught : \n");
            log(me.getStackTrace()[0].toString() + "\n");
            flushButton.setEnabled(true); //on retablie l'acces au flush /add (plus de concurentiel)
            collectAdd.setEnabled(true);
            collectButton.setEnabled(true);
        }

    }
    
    /**
     * Give a reference of the PopStatus. MUST BE setted.
     * this is set here beacause the constructor copy the instance instead of 
     * taking reference.
     * 
     * @param popStatus 
     */
    public void setPopStatus(PopStatus popStatus) {
        this.status = popStatus;
    }
    
    /**
     * Give a reference of the dbSystem. MUST BE setted.
     * this is set here beacause the constructor copy the instance instead of 
     * taking reference.
     * 
     * @param dbSystem 
     */
    public void setDatabaseHandler(DatabaseHandler dbSystem) {
        this.dbSystem = dbSystem;
        if (this.dbSystem != null) {
            PopSettings pop = this.dbSystem.loadUserPopSettings();
            if (pop != null) {
                userField.setText(pop.user);
                passField.setText(pop.pass);
                popField.setText(pop.popProvider);
                portField.setText(pop.popPort);
            } else {
                userField.setText("");
                passField.setText("");
                popField.setText("");
                portField.setText("");
            }

        }
    }

    /**
     * Give a reference of the dbemails. MUST BE setted.
     * this is set here beacause the constructor copy the instance instead of 
     * taking reference.
     * 
     * @param maildb 
     */
    public void setDb(DatabaseEmail maildb) {
        this.maildb = maildb;
    }

    /**
     * delete emails fromdatabase.
     */
    private void processFlush() {
        if (this.status == null) {
            JOptionPane.showMessageDialog(null, "An error has been encountered during initialisation, you should restart the program !", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            if (this.maildb != null) {
                this.collectButton.setEnabled(false);
                this.collectAdd.setEnabled(false);
                this.collectAdd.setEnabled(false);
                try {
                    log("Flush started \n");
                    int i = this.maildb.getNumberOfMails();
                    //on demande a la bd de supprimer ses emails.
                    for (String str : this.status.getMail()) {
                        this.maildb.deleteMailFromDatabase(str);
                    }
                    this.maildb.saveDatabase();
                    log((i - this.maildb.getNumberOfMails()) + " / " + this.status.getMail().size() + " mails has been deleted\n");
                    this.status.clearMail();
                    log("Flush ended \n");
                    //si tout s'est bien passé : on flush aussi les deux listes en memoire de status.
                } catch (Exception e) {
                    log(e.getStackTrace()[0].toString() + "\n");
                }
                this.collectButton.setEnabled(true);
                this.collectAdd.setEnabled(true);
                this.collectAdd.setEnabled(true);
            } else {
                log("No database Email has been setted \n");
            }
        }
    }
    
    /**
     * process the button addEvent with adding an email to list from the 
     * collectField
     */
    private void processAdd() {
        if (!(collectOne.getText() != null && !("".equals(collectOne.getText())))) {
            System.out.println("nothing to add.");
            return;
        }
        this.status.addMail(collectOne.getText());
        log("Added : " + collectOne.getText() + " \n");
        collectOne.setText("");
    }
    
    /**
     * append to traceZone of this PopObject the String msg.
     * 
     * @param msg 
     */
    public void log(String msg) {
        collectTrace.append(msg);
        collectTrace.setCaretPosition(collectTrace.getText().length());
    }
}
