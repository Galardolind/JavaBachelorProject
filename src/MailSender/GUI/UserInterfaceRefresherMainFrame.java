package MailSender.GUI;

import MailSender.CoreClasses.Smtp;
import MailSender.CoreClasses.SmtpStatus;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is a refresher. a thread observing an another thread, updating 
 * an Interface. it's an home made Observer for an home made worker(swingworker).
 * 
 * This one has been done for an smtp worker (Smtp object).
 */
abstract class UserInterfaceRefresherMainFrame extends Thread {    
    
    /**
     * Frame calling.
     */
    UserInterfaceMainFrame mainFrame;
    
    /**
     * Worker and bridge
     * smtpStatus is the bridge, connected to frame, woker, refresher
     */
    SmtpStatus status;
    Smtp actualWorker;
    
    /**
     * states and flags
     */
    boolean refresherCanStart = false;
    boolean chain = false;
    boolean working = false;
    boolean quit;

    /**
     * Constructor.
     * 
     * this create a new Thread without starting it. 
     * woker and status are needed before launching.
     * 
     * @param mainFrame 
     */
    public UserInterfaceRefresherMainFrame(UserInterfaceMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.quit = false;
    }
    
    /**
     * quit the refresher. 
     */
    public void quit() {
        this.quit = true;
    }

    /**
     * set the actual worker of the refresher.
     * 
     * @param worker 
     */
    public void setWorker(Smtp worker) {
        this.actualWorker = worker;
        this.mainFrame.progressBar.setMinimum(0);

        this.mainFrame.progressBar.setMaximum(worker.getNbMailTotal());
        this.mainFrame.upload_tab.progressbar.setMinimum(0);
        this.mainFrame.upload_tab.progressbar.setMaximum(worker.getNbMailTotal());
    }

    /**
     * set the actual status (SmtpStatus);
     * @param ss 
     */
    public void setStatus(SmtpStatus ss) {
        this.status = ss;
    }

    /**
     * run the Thread (need to be used from start() to start a new Thread).
     * handle the thread comportement using the status.
     */
    @Override
    public void run() {
        while (true) {
            if (this.quit) {
                return;
            }
            if (refresherCanStart) {
                initInterface();
                try {
                    actualWorker.start();
                    working = true;
                    while (actualWorker.isAlive()) {
                        if (this.quit) {
                            actualWorker.getStatus().setStop(true);
                        }
                        try {
                            doWorking(false);
                            this.sleep(10); // refresh delay
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    //refresh one lastTime.
                    doWorking(true);
                } catch (Exception e) {
                    //the worker has crashed. 
                }
                working = false;
                refresherCanStart = false; //avoid incovenient
                clean(); //clear the working
            } else {
                if (doUnloaded() && chain) {
                    refresherCanStart = true;
                    actualWorker.getStatus().setRun(true);
                }
                try {
                    this.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(UserInterfaceRefresherMainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /**
     * Clean the refreshing status, worker.
     */
    public abstract void clean();

    /**
     * Interface updating at each frame when working
     */
    public abstract void doWorking(boolean boolTest);

    /**
     * Interface updating when no worker running
     * @return boolean
     */
    public abstract boolean doUnloaded();
    
    /**
     * init the interface
     */
    public abstract void initInterface();
    
    /**
     * Interface updating at the end of the actual worker.
     */
    public abstract void doEnd();
}