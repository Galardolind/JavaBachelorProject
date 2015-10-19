JFLEX = jflex.bat
YACC = byaccj.exe
JAVA = java
JAVAC = javac
JAVADOC = javadoc.exe
DIRECTORY_CLASS = "Interpreteur/"
YACC_FLAGS = -d -v -Jclass=Parser -Jpackage=Interpreteur -J
OUTPUT = "../../../lib/Interpreteur/"

all: Lexer.java Parser.java
	$(JAVAC) -d "./" *.java

jar: 
	jar -cmvf Manifest.mf Interpreteur.jar ${DIRECTORY_CLASS}*.class
	cp Interpreteur.jar ${OUTPUT}

javadoc:	
	${JAVADOC} -d ${OUTPUT}javadoc/ *.java

Lexer.java: Lexer.flex
	$(JFLEX) Lexer.flex

Parser.java: Parser.y
	$(YACC) $(YACC_FLAGS) Parser.y

clean:
	rm  Lexer.java Parser.java ParserTokens.java ParserVal.java ${DIRECTORY_CLASS}*.class *.html *.css
	