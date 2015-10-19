package MailSender.GUI;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

/**
 * This class Provides a html previewer based on UserInterfaceHtmlPreviewPanel
 * The generated dialog is modal and disable the calling frame, preventing
 * concurencials matters
 *

 *
 */
public class UserInterfaceHtmlPreviewDialog extends JDialog {

    /**
     * Contains owner : may be null
     */
    Frame frame;
    /**
     * Panel of the previewer
     */
    UserInterfaceHtmlPreviewPanel previewer;
    /**
     * Contains the scrollPane of the previewArea.
     */
    JScrollPane scrollPane;

    /**
     * Constructor.
     *
     * Initialize the preview dialog. this only initialise the component it's
     * necessary to call showDialog to display the dialog.
     *
     * @param frame Owner of the dialog
     */
    public UserInterfaceHtmlPreviewDialog(Frame frame) {
        super(frame, "Default Settings", true);
        this.frame = frame;
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    /**
     * Display the dialog, with a waiting dialog preventing freeze like bug
     * warned in @see UserInterfaceHtmlPreviewPanel placing it in a thread for 
     * parallel rendering.
     * 
     * @param content String of text/html mimeType
     */
    public void ShowDialog(String content) {
        UserInterfaceWaitForIt wait = new UserInterfaceWaitForIt();
        (new Thread(wait)).start();
        this.previewer = new UserInterfaceHtmlPreviewPanel("text/html", content);
        this.scrollPane = new JScrollPane(previewer);
        this.add(scrollPane);
        this.pack();
        this.setMinimumSize(new Dimension(600, 600));
        this.setMaximumSize(new Dimension(900, 700));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int) (((screenSize.getWidth() / 2.0) - (this.getWidth() / 2.0))), (int) (((screenSize.getHeight() / 2.0) - (this.getHeight() / 2.0))));
        wait.stop();
        setVisible(true);
    }
}