
package MailRecuperator.CoreClasses;

import Database.CoreClasses.*;
import java.io.File;
import java.util.regex.Pattern;

/**
 * This Object contains all Database System and can be call to get otherManager, or set regex.
 */
public class SettingManager {
    
    /**
     * System DataBase object.
     */
    private DataBase db;
    /**
     * @see KeyWordManager
     */
    private KeyWordManager kwm;
    /**
     * @see BlackListManager
     */
    private BlackListManager blm;
    /**
     * @see RegexManager
     */
    private RegexManager rm;
    /**
     * @see UrlFilter
     */
    private UrlFilter urlf;
    /**
     * Path of the Email Database
     */
    private String emailDatabasePath;
    
    /**
     * Initialise all database manager objects, if one problem is encounter
     * (malformed database etc..) during the initialisation ,the program delete all the Database
     * and try to create a new one, if the problem persist the program will be shut down.
     * 
     * @throws ExceptionRecuperation If the program can't re-create the Database if she corrupted.
     */
    public SettingManager() throws ExceptionRecuperation{
        String path = System.getProperty("user.dir") + File.separator+"data"+File.separator+"recuperatorData";
        makeDirs(path);
        try {
            this.db = new DataBase(path);
            this.kwm = new KeyWordManager(this.db);
            this.blm = new BlackListManager(this.db);
            this.rm = new RegexManager(this.db);
            this.urlf = new UrlFilter(this.db);
            this.emailDatabasePath = System.getProperty("user.dir");
            
        } catch (Exception ex) {
            clearDatabase();
            makeDirs(path);
            try{
                this.db = new DataBase(path);
                this.kwm = new KeyWordManager(this.db);
                this.blm = new BlackListManager(this.db);
                this.rm = new RegexManager(this.db);
                this.urlf = new UrlFilter(this.db);
                this.emailDatabasePath = System.getProperty("user.dir");
            } catch(ExceptionDataBase exc){
                throw new ExceptionRecuperation("Setting Manager : "+exc);
            }
            
        }
    }
    
    /**
     * Create a new fresh directory if isn't exist.
     * 
     * @param path Where the directory is created.
     * @throws ExceptionRecuperation The program Can't create new directory. 
     */
    private void makeDirs(String path) throws ExceptionRecuperation{
        File testDirectory = new File(path);
        if(!(testDirectory.exists() && testDirectory.isDirectory())){
            boolean mkdirs = testDirectory.mkdirs();  
            if (!mkdirs){
                throw new ExceptionRecuperation("SettingManager : makeDirs : Impossible to create System Database directory");
            }
        }
    }
    
    /**
     * Delete All Files in Database directory.
     */
    public void clearDatabase(){
        File RecupDirectory = new File(System.getProperty("user.dir")+File.separator +"data"+File.separator +"recuperatorData");
        if(RecupDirectory.exists()){
            for(File todel : RecupDirectory.listFiles()){
                todel.delete();
            }
            RecupDirectory.delete();
        }
    }
    
    /**
     * 
     * @return KeyWordManager of the program. 
     */
    public KeyWordManager getKeyWordManager(){
        return kwm;
    }
    
    /**
     * 
     * @return BlackListManager of the program.
     */
    public BlackListManager getBlackListManager(){
        return blm;
    }
    
    /**
     * 
     * @return RegexManager of the program.
     */
    public RegexManager getRegexManager(){
        return rm;
    }
    
    /**
     * 
     * @return email Database path.
     */
    public String getEmailDatabasePath(){
        return this.emailDatabasePath;
    }
    
    /**
     * Set the new email database path.
     * 
     * @param dbPath New Path of Database.
     */
    public void setEmailDatabasePath(String dbPath) {
        this.emailDatabasePath = dbPath;
    }
    
    /**
     * Delete url historic.
     */
    public void deleteHistoric(){
        this.urlf.resetFilter();
    }
    
    /**
     * Set regex to all EmailRecuperation if its valid java regex.
     * 
     * @param regex New Regex.
     * @return true if all are good, false otherwise.
     */
    public boolean setRegex(String regex){
        try {
                Pattern emailRegex = Pattern.compile(regex);
                EmailFinder emf = new EmailFinder();
                emf.setRegex(regex);
                return true;
        } catch(Exception e){
            return false;
        }
        
    }
}
