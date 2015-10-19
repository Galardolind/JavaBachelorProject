package Fusion;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SenderPanel extends JPanel {
    
    JButton senderButton;
    JLabel senderLabel1;
    JLabel senderLabel2;
    JLabel senderLabel3;
    JLabel senderLabel4;
    JLabel senderLabel5;
    JPanel empty1;
    JPanel empty2;
    Image recuperatorIcon;
    
    public SenderPanel() {
        
        this.senderButton = new JButton();
        
        this.empty1 = new JPanel();
        this.empty2 = new JPanel();
        
        try {
            this.recuperatorIcon = ImageIO.read(getClass().getResource("mailsender.png"));
            this.senderButton.setIcon(new ImageIcon(this.recuperatorIcon));
        } catch (IOException ex) {
            Logger.getLogger(RecuperatorPanel.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        this.senderLabel1 = new JLabel("Email Sender");
        Font font = new Font(this.senderLabel1.getFont().getFontName(), Font.BOLD, 12);
        this.senderLabel1.setFont(font);
        this.senderLabel2 = new JLabel("This software will enable you to send a great number");
        this.senderLabel3 = new JLabel("of mails to the adresses found with the recuperator.");
        this.senderLabel4 = new JLabel("It uses a custom templates that will adapt the message");
        this.senderLabel5 = new JLabel("sent to the recipient in function of the keywords associated.");
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 7;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(30,30,30,30);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(this.senderButton, gbc);
        gbc.gridx = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0;
        gbc.weighty = 1;
        this.add(this.empty1, gbc);
        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(30,30,0,30);
        this.add(this.senderLabel1, gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(0,30,0,30);
        this.add(this.senderLabel2, gbc);
        gbc.gridy = 3;
        this.add(this.senderLabel3, gbc);
        gbc.gridy = 4;
        this.add(this.senderLabel4, gbc);
        gbc.gridy = 5;
        gbc.insets = new Insets(0,30,30,30);
        this.add(this.senderLabel5, gbc);
        gbc.gridy = 6;
        gbc.weighty = 1;
        this.add(this.empty2, gbc);
        
    }
    
}
