package MailRecuperator.GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * The class {@code StatusPanel} is an extension of the JPanel that deals with 
 * the display of the progress bar and the text log, as well as the functions 
 * of pausing, resuming, and terminating the search or searches.
 */
public class StatusPanel extends JPanel{
    
    /**
     * The progress bar.
     */
    JProgressBar statusBar;
    /**
     * The button that pauses/resumes a current search.
     */
    JButton tooglePauseSearchButton;
    /**
     * The button that terminates a current search.
     */
    JButton stopSearchButton;
    /**
     * The zone displaying the text log.
     */
    JTextArea statusMsgs;
    /**
     * The scrollable container for the text log display.
     */
    JScrollPane statusViewer;
    /**
     * The stream that will fill the log.
     */
    PrintStream out;
    
    /**
     * Class constructor.
     */
    public StatusPanel() {
        
        /**
         * Initializes the progress bar.
         */
        this.statusBar = new JProgressBar();
        this.statusBar.setPreferredSize(new Dimension(this.statusBar.getWidth(), 30));
        
        /**
         * Initializes the pause/resume button.
         */
        this.tooglePauseSearchButton = new JButton("Pause/Resume Search");
        
        /**
         * Initializes the stop button.
         */
        this.stopSearchButton = new JButton("Stop Search");
        
        /**
         * Initializes the text display zone and its containers.
         */
        this.statusMsgs = new JTextArea();
        this.statusMsgs.setFocusable(false);
        this.statusViewer = new JScrollPane(this.statusMsgs);
        
        /**
         * Placing all the components in their place.
         */
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        gbc.weightx = 1;
        gbc.weighty = 0;
        this.add(this.statusBar, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0;
        this.add(this.tooglePauseSearchButton, gbc);
        gbc.gridx = 2;
        this.add(this.stopSearchButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        gbc.weighty = 1;
        this.add(this.statusViewer, gbc);

        /**
         * Incorporate the stream into the text log.
         */
        this.out = new PrintStream(new TextAreaOutputStream(statusMsgs));
        System.setOut(this.out);
    }
    
}
