package MailRecuperator.GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The class {@code InfoPanel} is an extension of the JPanel and deals with the
 * displaying of research stats.
 */
public class InfoPanel extends JPanel {
    
    /**
     * Here the *Label are the name of the stat and *Info is the value 
     * of the stat.
     */
    JLabel visitedPagesLabel;
    JLabel visitedPagesInfo;
    JLabel emailsFoundLabel;
    JLabel emailsFoundInfo;
    JLabel nbKeywordsLabel;
    JLabel nbKeywordsInfo;
    JLabel timeLabel;
    JLabel timeInfo;
    JLabel percentageLabel;
    JLabel percentageInfo;
    
    /**
     * Class constructor.
     */
    public InfoPanel() {
        
        /**
         * Visited pages.
         */
        this.visitedPagesLabel = new JLabel("Nb of Visited Pages : ");
        this.visitedPagesInfo = new JLabel("N/A");
        
        /**
         * Number of emails found.
         */
        this.emailsFoundLabel = new JLabel("Nb of eMails found : ");
        this.emailsFoundInfo = new JLabel("N/A");
        
        /**
         * Number of keywords for this search.
         */
        this.nbKeywordsLabel = new JLabel("Total Keywords : ");
        this.nbKeywordsInfo = new JLabel("N/A");
        
        /**
         * Time elapsed.
         */
        this.timeLabel = new JLabel("Time Elapsed : ");
        this.timeInfo = new JLabel("00:00:00");
        
        /**
         * Number of keywords already searched.
         */
        this.percentageLabel = new JLabel("Keywords Searched : ");
        this.percentageInfo = new JLabel("0%");
        
        /**
         * Placing all the components in their place.
         */
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5,5,20,0);
        gbc.weightx = 0;
        this.add(this.visitedPagesLabel, gbc);
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5,0,20,0);
        gbc.weightx = 0.40;
        this.add(this.visitedPagesInfo, gbc);
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        this.add(this.emailsFoundLabel, gbc);
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.40;
        this.add(this.emailsFoundInfo, gbc);
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        this.add(this.nbKeywordsLabel, gbc);
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5,0,20,5);
        gbc.weightx = 0.20;
        this.add(this.nbKeywordsInfo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0,5,10,0);
        gbc.weightx = 0;
        this.add(this.timeLabel, gbc);
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0,0,10,5);
        gbc.weightx = 0.40;
        this.add(this.timeInfo, gbc);
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        this.add(this.percentageLabel, gbc);
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.40;
        this.add(this.percentageInfo, gbc);
        
    }
    
}
