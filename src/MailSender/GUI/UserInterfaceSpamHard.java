package MailSender.GUI;

import MailSender.CoreClasses.ExceptionSender;
import MailSender.CoreClasses.SimpleSettings;
import MailSender.CoreClasses.Smtp;
import MailSender.CoreClasses.Template;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This class provides a dialog handling multiple spam with only one target.
 * This will generates an Smtp Instance overriding filtering avoid an anti flood
 * protection.
 */
public class UserInterfaceSpamHard extends JDialog {

    /**
     * Chaos class. ////////////////////////////////////////////////////////////
     *
     * This class provides the dark side of the spam. It'll drive you in hell
     * with flames, bloody demons and zombie NyanCat.
     *
     * Use at you own risk.
     *
     * May the evil, the dark and the bad remain at your side.
     *
     * //////////////////////////////////////////////////////////// Chaos
     * Class.
     */
    /**
     * Gui Components
     */
    JLabel floodMailNumberLabel = new JLabel("Mails number : ");
    JLabel floodMailLabel = new JLabel("Mail : ");
    JTextField floodMailNumber;
    JTextField floodMail;
    JTextArea sendedWorld;
    JScrollPane scrollPane;
    JButton addToQueue = new JButton("Add");
    JPanel panelPrincipal = new JPanel();
    boolean normalyclosed = false;

    /**
     * Constructor.
     *
     * generate an instance of UserInterfaceSpamHard with calling component 
     * calling in first parameter. 
     * 
     *
     * @param JFrame owner. maybe null.
     */
    public UserInterfaceSpamHard(JFrame owner) {
        super(owner, true);

        floodMailNumber = new JTextField("100");
        floodMail = new JTextField("topiko@yopmail.com");
        sendedWorld = new JTextArea();
        scrollPane = new JScrollPane(sendedWorld);

        GridBagConstraints gbc = new GridBagConstraints();
        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(panelPrincipal, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(addToQueue, gbc);

        panelPrincipal.setLayout(gbl);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelPrincipal.add(floodMailLabel, gbc);
        gbc.weightx = 1.0;
        gbc.weighty = 0;

        gbc.gridx = 1;
        gbc.gridy = 0;
        panelPrincipal.add(floodMail, gbc);
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelPrincipal.add(floodMailNumberLabel, gbc);
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.gridx = 1;
        gbc.gridy = 1;
        panelPrincipal.add(floodMailNumber, gbc);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelPrincipal.add(scrollPane, gbc);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setMinimumSize(new Dimension(400, 200));
        this.setResizable(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int) (((screenSize.getWidth() / 2.0) - (this.getWidth() / 2.0))), (int) (((screenSize.getHeight() / 2.0) - (this.getHeight() / 2.0))));

        this.addToQueue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                normalyclosed = true;
                dispose();
            }
        });

    }

    /**
     * generate template from fields
     * 
     * @return Template
     */
    private Template makeTemplate() {
        return new Template("spamHardGenerated", "SPAM HARD\n$$template_EOO\n" + sendedWorld.getText() + "\n$$template_EOF\n$$var_name osef\n$$var_surname osef");
    }

    /**
     * Displays the dialog and return the smtp generated from the fields. maybe
     * null if field elements aren't properly
     *
     * @param settings
     * @return
     */
    public Smtp ShowDialog(SimpleSettings settings) {
        Smtp temp = null;
        this.setVisible(true);
        if(normalyclosed){
            try {
                temp = getSmtp(settings);
                return temp;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Options wasn't valid. Send hasn't been created neither added", "Warning", JOptionPane.WARNING_MESSAGE);
                return temp;
            }
        }
        return temp;
    }

    /**
     * Generate an smtp intsance overiding mailfiltering with nbMail mails
     * waiting to be send, using settings.
     *
     * generation may fail for bad arguments, or bad values retrieved from
     * fields.
     *
     * @param settings
     * @return Smtp
     * @throws ExceptionSender
     */
    public Smtp getSmtp(SimpleSettings settings) throws ExceptionSender {
        try{
            String[] to = new String[Integer.parseInt(floodMailNumber.getText())];
            String[] keys = {"spamHard"};
            for (int i = 0; i < Integer.parseInt(floodMailNumber.getText()); i++) {
                to[i] = floodMail.getText();                
            }
            return new Smtp(settings, makeTemplate(), to, keys, true);
        } catch (Exception e) {
            throw new ExceptionSender("Not a number");
        }        
    }
}
