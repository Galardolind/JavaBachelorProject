package MailSender.CoreClasses;

import javax.swing.JTextArea;

/**
 * Class that represent the status of a Smtp object.
 */
public class SmtpStatus {
    /**
     * The progress.
     */
    int progress;
    
    /**
     * The number total of mails.
     */
    int nbMailTotal;
    
    /**
     * The current number of mails.
     */
    int nbMailAct;
    
    /**
     * The number of errors.
     */
    int errors;
    
    /**
     * The start time.
     */
    long startTime;
    
    /**
     * The time.
     */
    long time;
    
    /**
     * The current time.
     */
    long current_time;
    
    /**
     * True if running, false otherwise.
     */
    boolean run;
    
    /**
     * True if stopped, false otherwise.
     */
    boolean stop;
    
    /**
     * True if done, false otherwise.
     */
    boolean done = false;
    
    /**
     * The frame.
     */
    JTextArea frame;

    /**
     * Constructor of class SmtpStatus.
     * @param progress
     * @param nbMailTotal
     * @param nbMailAct
     * @param run
     * @param stop 
     */
    public SmtpStatus(int progress, int nbMailTotal, int nbMailAct, boolean run, boolean stop) {
        this.progress = progress;
        this.nbMailTotal = nbMailTotal;
        this.nbMailAct = nbMailAct;
        this.run = run;
        this.stop = stop;
    }
    
    /**
     * Constructor of class SmtpStatus.
     * @param total the number total of mails
     */
    public SmtpStatus(int total){
            this.progress = 0;
            this.nbMailTotal = total;
            this.nbMailAct = 0;
            this.run = true;
            this.stop = false;
    }
    
    /**
     * Constructor of class SmtpStatus.
     */
    public SmtpStatus(){
    }
    
    /**
     * Get the start time.
     * @return the start time
     */
    public long getStartTime(){
        return startTime;
    }
    
    /**
     * Set the start time.
     * @param time the start time to replace with.
     */
    public void setStartTime(long time){
        startTime= time;
    }
    
    /**
     * Set the output of the frame.
     * @param frame the output to replace with
     */
    public void setFrameOutput(JTextArea frame){
        this.frame = frame;
    }
    
    /**
     * Insert text in the current output.
     * @param add the string to insert
     */
    public void insertOutput(String add){
        if(this.frame != null){
            this.frame.append(add);
            this.frame.setCaretPosition(this.frame.getText().length() -1);
        }
    }

    /**
     * Get the current number of mails.
     * @return the current number of mails
     */
    public int getNbMailAct() {
        return nbMailAct;
    }

    /**
     * Get the number total of mails.
     * @return the number total of mails
     */
    public int getNbMailTotal() {
        return nbMailTotal;
    }

    /**
     * Get the progress of the Smtp.
     * @return the progress
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Get the field run.
     * @return true if running, false otherwise
     */
    public boolean isRun() {
        return run;
    }

    /**
     * Get the field stop.
     * @return true if stopped, false otherwise
     */
    public boolean isStop() {
        return stop;
    }

    /**
     * Set the current number of mails.
     * @param nbMailAct the current number of mails to replace with
     */
    public void setNbMailAct(int nbMailAct) {
        this.nbMailAct = nbMailAct;
    }

    /**
     * Set the number total of mails.
     * @param nbMailTotal the number total of mails to replace with
     */
    public void setNbMailTotal(int nbMailTotal) {
        this.nbMailTotal = nbMailTotal;
    }

    /**
     * Set the progress.
     * @param progress the progress to replace with
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * Set the run field.
     * @param run true if running, false otherwise
     */
    public void setRun(boolean run) {
        this.run = run;
    }

    /**
     * Set the stop field.
     * @param stop true if stopped, false otherwise
     */
    public void setStop(boolean stop) {
        this.stop = stop;
    }    
    
    /**
     * Ending of this thread.
     */
    public void end(){
        this.done = true;
    }
    
    /**
     * Get the done field.
     * @return true if the work is done, false otherwise
     */
    public boolean getDone(){
        return done;
    }

    /**
     * Set the errors field.
     * @param errors the number of mails that haven't been correctly sent.
     */
    public void setErrors(int errors) {
        this.errors = errors;
    }

    /**
     * Get the errors field.
     * @return the number of mails that haven't been correctly sent.
     */
    public int getErrors() {
        return errors;
    }   

    /**
     * Get the time field.
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * Set the current time.
     * @param current_time the current time
     */
    public void setCurrentTime(long current_time) {
        this.current_time = current_time;
    }

    /**
     * Get the current time.
     * @return the current time
     */
    public long getCurrentTime() {
        return current_time;
    }

    /**
     * Set the time field.
     * @param time the time
     */
    public void setTime(long time) {
        this.time = time;
    }
}
