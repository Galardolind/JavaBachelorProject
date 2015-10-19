package Fusion;

import MailRecuperator.GUI.Mainframe;
import MailSender.GUI.UserInterfaceMainFrame;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Fusion extends JFrame {
    
    RecuperatorPanel recuperatorPanel;
    SenderPanel senderPanel;
    JSeparator separator;
    JButton quitFusionButton;
    
    public Fusion() {
        
        this.setMinimumSize(new Dimension(640, 480));
        this.setResizable(false);
        this.setTitle("Mail Bomber Fusion EX");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        /* 
         * Windows Look and Feel
         */
        
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows"
                    + ".WindowsLookAndFeel");
        } catch (ClassNotFoundException 
                | InstantiationException 
                | IllegalAccessException 
                | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Mainframe.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        this.recuperatorPanel = new RecuperatorPanel();
        this.recuperatorPanel.recuperatorButton
                .addActionListener(new RecuperatorButtonListener());
        
        this.senderPanel = new SenderPanel();
        this.senderPanel.senderButton
                .addActionListener(new SenderButtonListener());
        
        this.separator = new JSeparator();
        
        this.quitFusionButton = new JButton("Quit");
        this.quitFusionButton
                .addActionListener(new QuitFusionButtonListener());
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(this.recuperatorPanel, gbc);
        gbc.gridy = 1;
        gbc.weighty = 0;
        this.add(this.separator, gbc);
        gbc.gridy = 2;
        gbc.weighty = 1;
        this.add(this.senderPanel, gbc);
        gbc.gridy = 3;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        this.add(this.quitFusionButton, gbc);
        
        this.setVisible(true);
      
    }
    
    public void callRecuperator(ActionEvent event) {
        
        Mainframe recuperator = new Mainframe(this);
        this.setVisible(false);
        
    }
    
    public void callSender(ActionEvent event) {
        
        UserInterfaceMainFrame sender = new UserInterfaceMainFrame(this);
        this.setVisible(false);
        
    }
    
    public void exitFusion(ActionEvent event) {
        
        this.dispose();
        
    }
    
    class RecuperatorButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            callRecuperator(e);
        }
        
    }
    
    class SenderButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            callSender(e);
        }
        
    }
    
    class QuitFusionButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            exitFusion(e);
        }
        
    }
        
    public static void main(String[] args) {
        
        Fusion mb = new Fusion();
        
    }
    
}
