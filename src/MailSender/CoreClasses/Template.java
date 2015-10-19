package MailSender.CoreClasses;

import java.util.HashMap;

/**
 * A class that generate a template object.(controler transaction, etc...)
 */
public class Template {

    /**
     * The template's name.
     */
    public String name;
    /**
     * The Template's content
     */
    public String content;

    /**
     * Constructor of class Template.
     * @param name
     * @param content 
     */
    public Template(String name, String content) {
        this.name = (name == null) ? "" : name;
        this.content = Template.parseValidateContent(content);
    }
    
    /**
     * Make the content valid if it is not.
     * @param s the content to parse
     * @return the valid content
     */
    public static String parseValidateContent(String s) {
        String content = (s == null) ? "" : s;
        String templateEOO = "\n\\$\\$template_EOO\n";
        String templateEOF = "\n\\$\\$template_EOF\n";
        if(!content.isEmpty()) {
            String[] splitedContentEOO = content.split(templateEOO);
            String[] splitedContentEOF = content.split(templateEOF);
            if(splitedContentEOO.length == 2) { // if the templateEOO is in the middle
                if(splitedContentEOF.length == 2 
                        || splitedContentEOF.length == 1) {
                    return content;
                }
            }
        }
        content = content.replaceAll("\\$\\$template_EOO", "\n");
        content = content.replaceAll("\\$\\$template_EOF", "\n");
        content = "\n$$template_EOO\n"+content+"\n$$template_EOF\n";
        return content;
    }

    /**
     * Get the name of the template.
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name of the template.
     * @param name 
     */
    public void setName(String name) {
        this.name = (name == null) ? "" : name;
    }

    /**
     * Set the content of the template.
     */
    public void setContent(String content) {
        this.content = Template.parseValidateContent(content);
    }

    /**
     * Get the content of the template
     * @return the content
     */
    public String getContent() {
        return this.content;
    }
    
    /**
     * Get the object of the mail from the content of the template.
     * @return the object of the mail
     */
    public String getObject() {
        String[] splitedContent = this.content.split("\n\\$\\$template_EOO\n");
        if(splitedContent.length != 2) {
            return "";
        }
        return splitedContent[0];
    }
    
    /**
     * Get the clean content : the content of the template 
     * without the parts where the variables are.
     * @return the clean content
     */
    public String getCleanContent() {
        String[] splitedContent = this.content.split("\n\\$\\$template_EOO\n");
        if(splitedContent.length != 2) {
            return "";
        }
        splitedContent = splitedContent[1].split("\n\\$\\$template_EOF\n");
        if(splitedContent.length == 2 
                || splitedContent.length == 1) {
            return splitedContent[0];
        }
        return "";
    }

    /**
     * Get the list of vars and their values from the content of the template.
     * The vars are : name, surname, mail, domain...
     * @return the vars and their values
     */
    public String[][] getVarsAndValues() {
        String[][] varsAndValues;
        String[] splitTemplate_EOF = this.content.split("\\$\\$template_EOF\n");
        if(splitTemplate_EOF.length != 2) {
            return new String[0][2];
        }
        String metadata = splitTemplate_EOF[1];
        String[] lines = metadata.split("\n");
        varsAndValues = new String[lines.length][2];
        String[] splitedLine;
        String[] tab = new String[2];
        for (int i = 0; i < lines.length; i++) {
            splitedLine = lines[i].split(" ");
            if(splitedLine.length >= 2) {
                tab[0] = splitedLine[0];
                tab[1] = splitedLine[1];
                for (int j = 2; j < splitedLine.length; i++) {
                    tab[1] += splitedLine[j];
                }
                varsAndValues[i] = tab.clone();
            }
        }
        return varsAndValues;
    }

    /**
     * Like getVarsAndValues but return a HashMap.
     * Get the list of vars and their values from the content of the template.
     * The vars are : name, surname, mail, domain...
     * @return the vars and their values in a HashMap.
     */
    public HashMap<String, String> getVarsAndValuesHash() {
        HashMap<String, String> result = new HashMap<>();
        String[] splitTemplate_EOF = this.content.split("\\$\\$template_EOF\n");
        if(splitTemplate_EOF.length != 2) {
            return result;
        }
        String template_EOF = splitTemplate_EOF[1];
        String[] lines = template_EOF.split("\n");
        String[] splitedLine;
        String[] tab = new String[2];
        for (int i = 0; i < lines.length; i++) {
            splitedLine = lines[i].split(" ");
            if(splitedLine.length >= 2) {
                tab[0] = splitedLine[0];
                tab[1] = splitedLine[1];
                for (int j = 2; j < splitedLine.length; i++) {
                    tab[1] += splitedLine[j];
                }
                result.put(tab[0], tab[1]);
            }
            else {
                result.put(splitedLine[0],"");
            }
        }
        
        return result;
    }
    
    /**
     * Replace the vars in the content of the template.
     * @param arg the vars and their values = result of the fonction getVarsAndValuesHash.
     * @return the content of the template with the vars replaced by their values
     */
    public String replaceVarsHash(HashMap<String, String> arg1) {        
        
        HashMap<String, String> vars = getVarsAndValuesHash(); //contient var_name et var_surname (par defaut)
        HashMap<String, String> arg = arg1; //contient au moins var_mail var_servermail et var_domain
        
        //verification of conformity of HashMap
        if(arg == null) arg = new HashMap<>();
        if (vars.isEmpty()) {
            if(arg.isEmpty()) return this.content;
        }
        String newContent = this.content;
        String regexKey;
        
        String[] alpha = {"var_name", "var_surname", "var_mail", "var_servermail", "var_domain"};
        
        for(int i = 0; i < alpha.length; i++){
            regexKey = "\\$\\$"+ alpha[i];
            
            if(arg1.containsKey(alpha[i]) && (!"".equals(arg1.get(alpha[i])))){     
                newContent = newContent.replaceAll(regexKey, arg1.get(alpha[i]));
            }
            else if(vars.containsKey("$$" + alpha[i])) {
                newContent = newContent.replaceAll(regexKey, vars.get("$$" + alpha[i]));
            }
            else {
            }
        }
        return newContent.split("\\$\\$template_EOF\n")[0].split("\\$\\$template_EOO\n")[1];
    }
    
    /**
     * Take a mail address and return the informations extracted with a process 
     * as a HashMap of vars and values.
     * @param mail a mail address
     * @return the HashMap of the vars and their values
     */
    public HashMap<String,String> getVarsFromMail(String mail, String from){
        HashMap<String, String> result = new HashMap<>();
        String mailName;
        String domainName;
        String[] processedMail;
        
        if (mail.length() == 0) {
            return result;
        }
        //process regex with the mail
        
        String[] splittedMail = mail.split("@");
        if(splittedMail.length == 2) {
            // we take everything before the "@"
            mailName = splittedMail[0];

            // we take the domain name
            domainName = splittedMail[1];
            result.put("var_domainName", domainName);

            processedMail = mailName.split(".");
            // if before the "@", the mail address is as "prenom.nom"
            if(processedMail.length == 2) {
                // if there isn't any number
                if(mailName.split("[0-9]").length <= 1) {
                    // if there is more than 2 letters
                    if(processedMail[0].length() > 1) {
                        // we save the firstname
                        result.put("var_name", processedMail[0]);
                    }
                    // if there is more than 2 letters
                    if(processedMail[1].length() > 1) {
                        // we save the name
                        result.put("var_surname", processedMail[1]);
                    }
                }
            }
        }
        // put the mail in the hashmap
        result.put("var_mail", mail);
        result.put("var_servermail", from);
        return result;
    }
    
    /**
     * Replace the vars in the content of the template by the informations found 
     * in the mail address with the function getVarsFromMail.
     * @param mail the mail address
     * @return the content with replacements by vars
     */
    public String getMessageFromMail(String mail, String from){
        HashMap<String,String> vars = getVarsFromMail(mail, from);
        return replaceVarsHash(vars);
    }
    
    /**
     * A String representation of this object.
     * @return a string representation of this object
     */
    @Override
    public String toString(){
        String result = "";
        if(this.name.length() > 25){
            result += this.name.substring(0, 25) + "...";
        }else {
            result += this.name;
        }
        return result;
    }
}