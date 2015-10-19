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

public class RecuperatorPanel extends JPanel {
    
    JButton recuperatorButton;
    JLabel recuperatorLabel1;
    JLabel recuperatorLabel2;
    JLabel recuperatorLabel3;
    JLabel recuperatorLabel4;
    JLabel recuperatorLabel5;
    JPanel empty1;
    JPanel empty2;
    Image recuperatorIcon;
    
    public RecuperatorPanel() {
        
        this.recuperatorButton = new JButton();
        
        this.empty1 = new JPanel();
        this.empty2 = new JPanel();
        
        try {
            this.recuperatorIcon = ImageIO.read(getClass().getResource("mailrecuperator.png"));
            this.recuperatorButton.setIcon(new ImageIcon(this.recuperatorIcon));
        } catch (IOException ex) {
            Logger.getLogger(RecuperatorPanel.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        this.recuperatorLabel1 = new JLabel("Email Recuperator");
        Font font = new Font(this.recuperatorLabel1.getFont().getFontName(), Font.BOLD, 12);
        this.recuperatorLabel1.setFont(font);
        this.recuperatorLabel2 = new JLabel("This software will enable you to run a search using");
        this.recuperatorLabel3 = new JLabel("several search engines, which results will be analysed");
        this.recuperatorLabel4 = new JLabel("by a custom parser to find eMail adresses (and much more)");
        this.recuperatorLabel5 = new JLabel("and associate these with the keyword used for the search");
        
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
        this.add(this.recuperatorButton, gbc);
        gbc.gridx = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0;
        gbc.weighty = 1;
        this.add(this.empty1, gbc);
        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(30,30,0,30);
        this.add(this.recuperatorLabel1, gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(0,30,0,30);
        this.add(this.recuperatorLabel2, gbc);
        gbc.gridy = 3;
        this.add(this.recuperatorLabel3, gbc);
        gbc.gridy = 4;
        this.add(this.recuperatorLabel4, gbc);
        gbc.gridy = 5;
        gbc.insets = new Insets(0,30,30,30);
        this.add(this.recuperatorLabel5, gbc);
        gbc.gridy = 6;
        gbc.weighty = 1;
        this.add(this.empty2, gbc);
        
    }
    
}
