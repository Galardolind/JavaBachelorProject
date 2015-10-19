package MailSender.CoreClasses;

import java.util.Date;

/**
 * A class that represent a sample of a mail to send.
 */
public class MailSample {
    String from = "";
    String replyTo = "";
    String to = "";
    String subject = "";
    Date date = new Date();
    String content = "";

    /**
     * Gets the content of the mail.
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the date of the mail.
     * @param date the date to replace with
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Get the mail address from who the mail is sent.
     * @return the sender
     */
    public String getFrom() {
        return from;
    }

    /**
     * Get the mail address for reply to.
     * @return the reply to
     */
    public String getReplyTo() {
        return replyTo;
    }

    /**
     * Get the subject of the mail.
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Set the content of the mail.
     * @param content the content to replace with
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get the date of the mail.
     * @return the date of the mail
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the sender of the mail.
     * @param from the sender mail address
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Set the reply to.
     * @param replyTo the replyTo to replace with
     */
    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    /**
     * Set the subject of the mail.
     * @param subject the subject of the mail to replace with
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Set the recipient of the mail.
     * @param to the recipient of the mail to replace with
     */
    public void setTo(String to) {
        this.to = to;
    }   
}