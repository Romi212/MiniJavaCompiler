package utils.Exceptions;

public class LexicalErrorException extends CompilerException{

    private String lexeme;
    private int line;
    private int column;
    private String codeLine;
    private String explanation;
    public LexicalErrorException(String lexeme, int line, int column, String codeLine, String explanation){
        super("[Error:"+lexeme+"|"+line+"]");
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
        this.codeLine = codeLine;
        this.explanation = explanation;
    }

    public String getLongMessage(){
        String longMessage = "Lexical error in line "+line+", column "+column+": ["+lexeme+"] "+explanation;
        longMessage += "\nDetail: "+ codeLine;
        longMessage += "\n"+"        "+ " ".repeat(column-1) + "^";
        return longMessage;
    }

}
