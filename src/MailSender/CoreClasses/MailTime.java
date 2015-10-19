package MailSender.CoreClasses;

/**
 * Tool class for time.
 */
public class MailTime {
    /**
     * Return the time in float.
     * @param timeByMail the number of mail
     * @return the speed of mail sending
     */
    public static float getSpeedMails(long timeByMail){
        if(timeByMail < 0.1){
            return 0;
        }
        return (float) (1000.0/timeByMail);
    }
    
    /**
     * Compute the time left to send nb mail from speed.
     * @param nb number of mails left to send
     * @param speed speed of mail sending
     * @return the time left to finish sending all the mails
     */
    public static long restTime(int nb, float speed){
        return (long) (nb / (speed * 1.0)) * 1000;        
    }
    
    /**
     * Return a string to describe the time atime in hour, minutes and seconds.
     * @param atime the time to describe
     * @return the description of the time
     */
    public static String timePrint(long atime){
        String result; 
        long time = atime / 1000;  
        String seconds = Integer.toString((int)(time % 60));  
        String minutes = Integer.toString((int)((time % 3600) / 60));  
        String hours = Integer.toString((int)(time / 3600));  
        for (int i = 0; i < 2; i++) {  
            if (seconds.length() < 2) {  
                seconds = "0" + seconds;  
            }  
            if (minutes.length() < 2) {  
                minutes = "0" + minutes;      
            }  
            if (hours.length() < 2) {  
                hours = "0" + hours;  
            }  
        }  
        result = hours + "h " + minutes + "m " + seconds + "s";
        return result;
    }
}
