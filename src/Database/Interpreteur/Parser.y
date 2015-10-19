
%{
import java.io.*;
%}
      
%token GET SET CREATE UPDATE
%token OPTION_CSV OPTION_PATH OPTION_DELETE 
%token OPTION_ADD OPTION_DROP OPTION_COUNT 
%token OPTION_WITHOUT_DUPLICATE OPTION_GET
%token OPTION_SORT OPTION_LIMIT 
%token CONDITION ID JOINTURE OPTION_JSON   

%left JOINTURE
      
%%

commande :
  action expressions 
  | action
  ;

action :
  get
  | set
  | create
  | update 
  ;

get : 
  GET
  | GET OPTION_SORT
  | GET OPTION_COUNT
  | GET OPTION_LIMIT condition 
  | GET OPTION_LIMIT condition condition
  | GET OPTION_WITHOUT_DUPLICATE 
  | GET OPTION_WITHOUT_DUPLICATE condition
  ;

set : 
  SET 
  ;

update : 
  UPDATE 
  | UPDATE OPTION_GET
  | UPDATE OPTION_DELETE
  ;

create : 
  CREATE OPTION_CSV ID
  | CREATE OPTION_JSON ID
  | CREATE OPTION_PATH ID
  | CREATE OPTION_DELETE ID 
  | CREATE OPTION_ADD condition
  | CREATE OPTION_DROP

conditions :
  conditions condition
  | condition
  ;

condition : 
    CONDITION  
   ;

expressions :
  expressions expression 
  | expression
  ;

expression :
  ID conditions
  | ID
  | expression jointure 
  ;

jointure : 
  JOINTURE expression 
  ;



%%

  private Lexer lexer;

  public Lexer getLexer(){
    return lexer;
  }


  private int yylex () {
    int yyl_return = -1;
    try {
      yylval = new ParserVal(0);
      yyl_return = lexer.yylex();
    }
    catch (IOException e) {
      System.err.println("IO error :"+e);
    }
    return yyl_return;
  }


  public void yyerror (String error) {
    System.err.println ("Error: " + error);
  }


  public Parser(Reader r) {
    lexer = new Lexer(r, this);
  }

  public Parser(String s)throws IOException {
    File tmp = File.createTempFile("stringToParse", ".tmp");
    FileWriter fw = new FileWriter(tmp);
    fw.write(s, 0, s.length()); 
    fw.close();
    lexer = new Lexer (new FileReader(tmp), this);
  }