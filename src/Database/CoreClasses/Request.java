package Database.CoreClasses;

import Database.Interpreteur.*;
import java.io.IOException;
import java.util.ArrayList;



/**
 *
 * 
 */
public class Request {
    
    private String cmd;
    
    private Parser p;
    Lexeme action;
    ArrayList<Lexeme> ls ;
    
    private DataBase db ;

    public Request(String cmd, DataBase db) throws IOException {
        this.db = db;
        this.cmd = cmd;
        p = new Parser(cmd);
        p.run();
        ls = p.getLexer().getLexemes(); 
        action = ls.get(0);
    }

    private Request() {
    }
    
    
    
}
