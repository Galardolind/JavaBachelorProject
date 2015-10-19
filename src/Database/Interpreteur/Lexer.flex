
package Database.Interpreteur;


import java.util.ArrayList;


%%

%byaccj

%{
  /* store a reference to the parser object */
  private Parser yyparser;

    private ArrayList<Lexeme> ls = new ArrayList<>();

    public void addLexeme(Lexeme l){
        ls.add(l);
    }
    public ArrayList<Lexeme> getLexemes(){
        return ls;
    }

  /* constructor taking an additional parser object */
  public Lexer(java.io.Reader r, Parser yyparser) {
    this(r);
    this.yyparser = yyparser;
  }
%}

%class Lexer
%public
%column
%char
%full


ALPHA=[A-Za-z]
DIGIT=[0-9]

GET = "GET"
SET = "SET"
CREATE = "CREATE"
UPDATE = "UPDATE"

OPTION_CSV = "--csv"
OPTION_JSON = "--json"
OPTION_PATH = "--path"
OPTION_DELETE = "--delete"
OPTION_ADD = "--add"
OPTION_DROP = "--drop"
OPTION_COUNT = "--count"
OPTION_SORT ="--sort"
OPTION_LIMIT = "--limit"
OPTION_WITHOUT_DUPLICATE = "--withou-duplicate"
OPTION_GET = "--get"

CONDITION = "("[^)]*")"
JOINTURE = "."
ID = [a-zA-Z0-9]+

ESCAPE = " "|"\n"|"\r"



%% 

<YYINITIAL> {

  {GET} {
    Lexeme l = new Lexeme(Parser.GET, yytext());
    addLexeme(l);
    return Parser.GET;
  }
  {SET} {
    Lexeme l = new Lexeme(Parser.SET, yytext());
    addLexeme(l);
    return Parser.SET;
  }
  {CREATE} {
    Lexeme l = new Lexeme(Parser.CREATE, yytext());
    addLexeme(l);
    return Parser.CREATE;
  }
  {UPDATE} {
    Lexeme l = new Lexeme(Parser.UPDATE, yytext());
    addLexeme(l);
    return Parser.UPDATE;
  }
  {OPTION_CSV} {
    Lexeme l = new Lexeme(Parser.OPTION_CSV, yytext());
    addLexeme(l);
    return Parser.OPTION_CSV;
  }
  {OPTION_JSON} {
    Lexeme l = new Lexeme(Parser.OPTION_JSON, yytext());
    addLexeme(l);
    return Parser.OPTION_JSON;
  }
  {OPTION_GET} {
    Lexeme l = new Lexeme(Parser.OPTION_GET, yytext());
    addLexeme(l);
    return Parser.OPTION_GET;
  }
  {OPTION_PATH} {
    Lexeme l = new Lexeme(Parser.OPTION_PATH, yytext());
    addLexeme(l);
    return Parser.OPTION_PATH;
  }
  {OPTION_WITHOUT_DUPLICATE} {
    Lexeme l = new Lexeme(Parser.OPTION_WITHOUT_DUPLICATE, yytext());
    addLexeme(l);
    return Parser.OPTION_WITHOUT_DUPLICATE;
  }
  {OPTION_DELETE} {
    Lexeme l = new Lexeme(Parser.OPTION_DELETE, yytext());
    addLexeme(l);
    return Parser.OPTION_DELETE;
  }
  {OPTION_ADD} {
    Lexeme l = new Lexeme(Parser.OPTION_ADD, yytext());
    addLexeme(l);
    return Parser.OPTION_ADD;
  }
  {OPTION_DROP} {
    Lexeme l = new Lexeme(Parser.OPTION_DROP, yytext());
    addLexeme(l);
    return Parser.OPTION_DROP;
  }
  {OPTION_COUNT} {
    Lexeme l = new Lexeme(Parser.OPTION_COUNT, yytext());
    addLexeme(l);
    return Parser.OPTION_COUNT;
  }
  {OPTION_SORT} {
    Lexeme l = new Lexeme(Parser.OPTION_SORT, yytext());
    addLexeme(l);
    return Parser.OPTION_SORT;
  }
  {OPTION_LIMIT} {
    Lexeme l = new Lexeme(Parser.OPTION_LIMIT, yytext());
    addLexeme(l);
    return Parser.OPTION_LIMIT;
  }
  {ID} {
    Lexeme l = new Lexeme(Parser.ID, yytext());
    addLexeme(l);
    return Parser.ID;
  }
  {JOINTURE} {
    Lexeme l = new Lexeme(Parser.JOINTURE, yytext());
    addLexeme(l);
    return Parser.JOINTURE;
  }
  {CONDITION} {
    Lexeme l = new Lexeme(Parser.CONDITION, yytext());
    addLexeme(l);
    return Parser.CONDITION;
  }
  {ESCAPE} {
  }
}


