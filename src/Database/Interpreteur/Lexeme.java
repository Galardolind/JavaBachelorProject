package Database.Interpreteur;

/**
 * @autor : romain
 */
public class Lexeme {

    private String text;
    private int type;

    Lexeme(int type, String text) {
        this.text = text;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return text;
    }
    
    public boolean isAction(){
        if(type == Parser.CREATE || 
                type == Parser.GET ||
                type == Parser.SET || 
                type == Parser.UPDATE ) {
            return true;
        }
        return false;
    }
    
    public boolean isOption(){
        if(type == Parser.OPTION_ADD ||
                type == Parser.OPTION_COUNT ||
                type == Parser.OPTION_CSV ||
                type == Parser.OPTION_DELETE||
                type == Parser.OPTION_DROP||
                type == Parser.OPTION_GET||
                type == Parser.OPTION_JSON||
                type == Parser.OPTION_LIMIT ||
                type == Parser.OPTION_PATH||
                type == Parser.OPTION_SORT||
                type == Parser.OPTION_WITHOUT_DUPLICATE){
            return true;
        }
        return false;
    }
}