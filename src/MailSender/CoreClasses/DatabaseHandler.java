package MailSender.CoreClasses;



import Database.CoreClasses.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Handle the program datas in the database 
 * like the templates and the user settings.
 */
public class DatabaseHandler {
    
    /**
     * The DataBase that contains templates, user settings, keywords ...
     */
    private DataBase db;
    
    /**
     * The table of the database that contains templates.
     */
    private InterfaceTable templatesTable;
    
    /**
     * The table of the database that contains the user settings.
     */
    private InterfaceTable settingsTable;
    
    /**
     * The table of the database that contains the pop settings.
     */
    private InterfaceTable popTable;
    
    /**
     * The filter of the mail addresses.
     */
    private MailFilter mailFilter;
    
    /**
     * Constructor of the class DatabaseHandler.
     * @throws ExceptionDataBase if the database throws an exception
     * @throws ExceptionSender if the datas in the database are corrupted
     */
    public DatabaseHandler() throws ExceptionDataBase, ExceptionSender {
        makeDirs(System.getProperty("user.dir") + File.separator+"data"+File.separator+"sendData");
        try {
            this.db = new DataBase(System.getProperty("user.dir") + File.separator+"data"+File.separator+"sendData");
        } catch(Exception e){
            throw new ExceptionDataBase("ExceptionDataBase : the database is corrumpted.");
        }
        if(db == null) throw new ExceptionDataBase("ExceptionDataBase : the database is equal to null.");
        Header head;
        // templates table
        String tableName = "templates";
        Header header = new Header();
        header.addColumn("name",new Properties(Type.STRING, true));
        header.addColumn("content",new Properties(Type.STRING));
        this.templatesTable = this.checkTable(tableName, header);
        head = this.templatesTable.getHeader();
        if(head == null){
            throw new ExceptionSender("DatabaseHandler : checkTable : the header of templatesTable doesn't match");
        }
        if(!head.containsKey("name")){
            throw new ExceptionSender("DatabaseHandler : checkTable : the header of templatesTable doesn't match");
        }
        if(!head.containsKey("content")){
            throw new ExceptionSender("DatabaseHandler : checkTable : the header of templatesTable doesn't match");
        }
        
        // settings table
        tableName = "conf";
        header = new Header();
        header.addColumn("smtpHost", new Properties(Type.STRING));
        header.addColumn("smtpPort", new Properties(Type.STRING));
        header.addColumn("user", new Properties(Type.STRING));
        header.addColumn("pass", new Properties(Type.STRING));
        header.addColumn("delay", new Properties(Type.STRING));
        header.addColumn("auth", new Properties(Type.STRING)); // boolean : "true" / "false"
        header.addColumn("from", new Properties(Type.STRING));
        header.addColumn("host", new Properties(Type.STRING));
        header.addColumn("path", new Properties(Type.STRING));
        this.settingsTable = this.checkTable(tableName, header);
        head = this.settingsTable.getHeader();
        if(head == null){
            throw new ExceptionSender("DatabaseHandler : checkTable : the header of settingsTable doesn't match");
        }
        if(!head.containsKey("smtpHost")){
            throw new ExceptionSender("DatabaseHandler : checkTable : the header of settingsTable doesn't match");
        }
        if(!head.containsKey("smtpPort")){
            throw new ExceptionSender("DatabaseHandler : checkTable : the header of settingsTable doesn't match");
        }
        if(!head.containsKey("user")){
            throw new ExceptionSender("DatabaseHandler : checkTable : the header of settingsTable doesn't match");
        }
        if(!head.containsKey("pass")){
            throw new ExceptionSender("DatabaseHandler : checkTable : the header of settingsTable doesn't match");
        }
        if(!head.containsKey("auth")){
            throw new ExceptionSender("DatabaseHandler : checkTable : the header of settingsTable doesn't match");
        }
        if(!head.containsKey("from")){
            throw new ExceptionSender("DatabaseHandler : checkTable : the header of settingsTable doesn't match");
        }
        if(!head.containsKey("host")){
            throw new ExceptionSender("DatabaseHandler : checkTable : the header of settingsTable doesn't match");
        }
        if(!head.containsKey("path")){
            throw new ExceptionSender("DatabaseHandler : checkTable : the header of settingsTable doesn't match");
        }
        
        tableName = "pop";
        header = new Header();
        header.addColumn("popProvider", new Properties(Type.STRING));
        header.addColumn("popPort", new Properties(Type.STRING));
        header.addColumn("popUser", new Properties(Type.STRING));
        header.addColumn("popPwd", new Properties(Type.STRING));
        this.popTable = this.checkTable(tableName, header);
        head = this.popTable.getHeader();
        if(!head.containsKey("popProvider")){
            throw new ExceptionSender("DatabaseHandler : checkTable : the header of settingsTable doesn't match");
        }
        if(!head.containsKey("popPort")){
            throw new ExceptionSender("DatabaseHandler : checkTable : the header of settingsTable doesn't match");
        }
        if(!head.containsKey("popUser")){
            throw new ExceptionSender("DatabaseHandler : checkTable : the header of settingsTable doesn't match");
        }
        if(!head.containsKey("popPwd")){
            throw new ExceptionSender("DatabaseHandler : checkTable : the header of settingsTable doesn't match");
        }
        mailFilter = new MailFilter(db);
    }
    
    /**
     * Get the filter of mail addresses.
     * @return the mail filter
     */
    public MailFilter getMailFilter(){
        return mailFilter;
    }
    
    /**
     * Cleans the database.
     * @throws ExceptionSender if it is impossible to create directories or to delete directories
     */
    public static void cleanBD() throws ExceptionSender{
        File testDirectory = new File(System.getProperty("user.dir") + "/data/sendData");
        if(!(testDirectory.exists() && testDirectory.isDirectory())){
            boolean mkdirs = testDirectory.mkdirs();  
            if (!mkdirs){
                throw new ExceptionSender("DatabaseHandler : impossible to create the directory of the database.");
            }
        }
        try {
            deleteDirectory(testDirectory);
        }
        catch (Exception me){
            throw new ExceptionSender("DeleteFileException : impossible to delete the database.", 1);
        }
    }
    
    /**
     * Delete the directory recursively or the file if path is the path to a file.
     * Do nothing if it is neither a directory or a file.
     * @param path the path to a file or a directory to delete
     */
    static public void deleteDirectory(File path){
        if (path == null)
            return;
        if (path.exists())
        {
            for(File f : path.listFiles())
            {
                if(f.isDirectory()) 
                {
                    deleteDirectory(f);
                    f.delete();
                }
                else
                {
                    f.delete();
                }
            }
            path.delete();
        }
    }
    
    /**
     * Create the directories for the database.
     * @param path null or a path
     * @throws ExceptionSender if it is impossible to create directories
     */
    public void makeDirs(String path) throws ExceptionSender{
        if (path != null){
            File testDirectory = new File(path);
            if(!testDirectory.exists() && testDirectory.isDirectory()){
                boolean mkdirs = testDirectory.mkdirs();  
                if (!mkdirs){
                    throw new ExceptionSender("DatabaseHandler : impossible to create the directory of the database.");
                }
            }
        }
    }
    
    /**
     * Check if a table named tableName exists in the database, and get it.
     * If don't exist, create and return it.
     * @param tableName the name of the table
     * @param h the header of the table if we need to create it
     * @return the table from the database
     * @throws ExceptionDataBase if the creation failed
     * @throws ExceptionSender if the creation failed
     */
    private InterfaceTable checkTable(String tableName, Header h) throws ExceptionDataBase, ExceptionSender {
        InterfaceTable table = this.db.getTableByName(tableName);
        if(table == null) {
            table = new TableJSON(tableName, this.db.getPathDataBase());
            table.setHeader(h);
            this.db.addTable(table);
            this.db.save();
            // check if table created
            if(this.db.getTableByName(tableName) == null) {
                throw new ExceptionDataBase("DatabaseHandler : checkTable : ExceptionDataBase : Fail of the creation of the missing table : "+tableName);
            }
        }
        return table;
    }
    
    // Templates Table requests
    
    /**
     * Return the complete list of all templates stored in the subfolder templates.
     * @return the template list
     */
    public ArrayList<Template> getTemplateList() {
        ArrayList<Row> rows = this.templatesTable.getAll();
        ArrayList<Template> templates = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            Row row = rows.get(i);
            Template template = new Template(row.get("name"), row.get("content"));
            templates.add(template);
        }
        return templates;
    }
    
    /**
     * Return a template object constructed from a name of template.
     * @param templateName : the name of the template to get
     * @return a template or null if template not found
     */
    public Template getTemplate(String templateName) {
        if(templateName == null || "".equals(templateName)) {
            return null;
        }
        Template template;
        Row row = this.templatesTable.getRowByIndex(templateName);
        if(row == null){
            return null;
        }
        template = new Template(templateName, row.get("content"));
        return template;
    }
    
    /**
     * Return the complete list of the names of all templates stored in the subfolder templates.
     * @return the template list
     */
    public ArrayList<String> getTemplateNameList() {
        ArrayList<Row> rows = this.db.getTableByName("templates").getAll();
        ArrayList<String> templateNames = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            templateNames.add(rows.get(i).get("name"));
        }
        return templateNames;
    }
    
    /**
     * Save the template in the database.
     * @param template the template to save
     * @throws ExceptionSender if it fails
     */
    public void saveTemplate(Template template) throws ExceptionSender {
        if (template == null) {
            throw new ExceptionSender("DatabaseHandler : saveTemplate : the template is equal to null");
        }
        if(template.getName().isEmpty() || template.getContent().isEmpty()){
            return;
        }
        Row row = new Row();
        row.addColumn("name", template.getName());
        row.addColumn("content", template.getContent());
        try {
            this.templatesTable.delRow("name", template.getName());
            this.templatesTable.addRow(row);
            this.templatesTable.save();
        } catch (ExceptionDataBase ex) {
            throw new ExceptionSender("DatabaseHandler : saveTemplate : a problem occured while saving.");
        }
    }
    
    /**
     * Replace the template named oldName by template.
     * @param template the template to replace with
     * @param oldName the name of the old template
     * @throws ExceptionSender if one of the parameters equals to null or oldName is empty
     */
    public void editTemplate(Template template, String oldName) throws ExceptionSender{
        if(template == null || oldName == null){
            throw new ExceptionSender("DatabaseHandler : editTemplate : invalid parameters : null");
        }
        if(oldName.isEmpty()){
            throw new ExceptionSender("DatabaseHandler : editTemplate : oldName is empty");
        }
        deleteTemplate(oldName);
        saveTemplate(template);
    }

    /**
     * Delete template from database.
     * @param templateName the name of the template to delete
     * @throws ExceptionSender if templateName equals to null or if it fails
     */
    public void deleteTemplate(String templateName) throws ExceptionSender {
        if(templateName == null) {
            throw new ExceptionSender("DatabaseHandler : deleteTemplate : templateName is equal to null");
        }
        if (templateName.isEmpty()){
            return;
        }
        try {
            this.templatesTable.delRow("name", templateName);
            this.templatesTable.save();
        } catch (ExceptionDataBase ex) {
            throw new ExceptionSender("DatabaseHandler : deleteTemplate : a problem occured while saving");
        }
    }
    
    // Settings Table requests
    
    /**
     * Save the user settings in the database.
     * @param simpleSettings : the settings to send mails.
     * @return true if success, false otherwise.
     */
    public boolean saveUserSettings(SimpleSettings simpleSettings) {
        if (simpleSettings == null) return false;
        if (simpleSettings.getFields().get("smtpHost").equals("")) return false;
        if (simpleSettings.getFields().get("host").equals("")) return false;
        Row row = new Row();
        HashMap<String,String> fields = simpleSettings.getFields();
        row.addColumn("smtpHost", fields.get("smtpHost"));
        row.addColumn("smtpPort", fields.get("smtpPort"));
        row.addColumn("user", fields.get("user"));
        row.addColumn("pass", fields.get("pass"));
        row.addColumn("delay", fields.get("delay"));
        row.addColumn("auth", fields.get("auth"));
        row.addColumn("from", fields.get("from"));
        row.addColumn("host", fields.get("host"));
        row.addColumn("path", fields.get("path"));
        try {
            if(!this.settingsTable.getAll().isEmpty()) {
                this.settingsTable.dropTable();
                this.settingsTable.save();
            }
            this.settingsTable.addRow(row);
            this.settingsTable.save();
            return true;
        } catch (ExceptionDataBase ex) {
            return false;
        }
    }
    
    /**
     * Load the user settings in database.
     * @return the SimpleSettings instance loaded from the database or null if no match.
     */
    public SimpleSettings loadUserSettings() {
        SimpleSettings simpleSettings;
        ArrayList<Row> rows = this.settingsTable.getAll();
        if(rows.isEmpty()) return null;
        Row row = rows.get(0);
        String smtpHost = row.get("smtpHost");
        if (smtpHost == null) return null;
        simpleSettings = new SimpleSettings(row.get("smtpHost"), 
                row.get("smtpPort"),  
                row.get("user"), 
                row.get("pass"), 
                Integer.parseInt(row.get("delay")), 
                Boolean.parseBoolean(row.get("auth")),
                row.get("path"),
                row.get("from"),
                row.get("host"));
        return simpleSettings;
    }
    
    // pop settings
    
    /**
     * Save the pop user settings in the database.
     * @param PopSettings the settings to pop.
     * @return true if success, false otherwise.
     */
    public boolean saveUserPopSettings(PopSettings popSettings) {
        if (popSettings == null) return false;
        if (popSettings.getFields().get("popUser").equals("")) return false;
        if (popSettings.getFields().get("popProvider").equals("")) return false;
        if (popSettings.getFields().get("popPort").equals("")) return false;
        Row row = new Row();
        HashMap<String,String> fields = popSettings.getFields();     
        row.addColumn("popUser", fields.get("popUser"));
        row.addColumn("popPwd", fields.get("popPwd"));
        row.addColumn("popPort", fields.get("popPort"));
        row.addColumn("popProvider", fields.get("popProvider"));
        try {
            if(!this.popTable.getAll().isEmpty()) {
                this.popTable.dropTable();
                this.popTable.save();
            }
            this.popTable.addRow(row);
            this.popTable.save();
            return true;
        } catch (ExceptionDataBase ex) {
            return false;
        }
    }
    
    /**
     * Load the pop user settings in database.
     * @return the PopSettings instance loaded from the database or null if no match.
     */
    public PopSettings loadUserPopSettings() {
        PopSettings popSettings;
        ArrayList<Row> rows = this.popTable.getAll();
        if(rows.isEmpty()) return null;
        Row row = rows.get(0);
        String popProvider = row.get("popProvider");
        if (popProvider == null) return null;
        popSettings = new PopSettings(row.get("popProvider"), 
                row.get("popPort"),  
                row.get("popUser"), 
                row.get("popPwd"));
        return popSettings;
    }
    
    
}
