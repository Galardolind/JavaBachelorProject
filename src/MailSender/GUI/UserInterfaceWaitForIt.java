package MailSender.GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * This class provides a waiting system for thread processing.
 * it contains progress bar wich can't be in an indeterminate mode due to some swing 
 * special comportements.
 */
public class UserInterfaceWaitForIt extends JDialog implements Runnable{
    JLabel waitLabel = new JLabel("Wait For it !");
    JProgressBar progressBar = new JProgressBar();
    boolean run = true;
    
    /**
     * Constructor.
     * 
     * Initialize the waiter. it's a thread, launch it in a thread.
     */
    public UserInterfaceWaitForIt() {
        
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);   
        this.setTitle("processing render ...");
        
        progressBar.setIndeterminate(false);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(gbl);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10,30,10,30);
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(waitLabel, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(5,10,5,10);
        this.add(progressBar, gbc);
        this.setMinimumSize(new Dimension(200,100));
        this.setResizable(false);        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int)(((screenSize.getWidth() / 2.0) - (this.getWidth()/2.0))),(int)(((screenSize.getHeight() / 2.0) - (this.getHeight()/2.0))));
    }   
    
    /**
     * realease the thread and quit the frame.
     */
    public void stop(){
        run = false;
    }

    /**
     * Launch the dialog.
     * It's a runnable intance so it's meant to be run in like a thread (not exactly
     * the same approah, tought).
     */
    @Override
    public void run() {
        this.setVisible(true);
        this.update(this.getGraphics());
        while(run){
            try {
                //this.repaint();
                progressBar.setValue((progressBar.getValue() + 1) % 100);
                progressBar.update(this.progressBar.getGraphics());                
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(UserInterfaceWaitForIt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.dispose();
    }
}
