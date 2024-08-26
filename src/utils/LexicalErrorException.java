package utils;

public class LexicalErrorException extends Exception{

    private String lexeme;
    private int line;
    private String explanation;
    public LexicalErrorException(String lexeme, int line, String explanation){
        super("[Error:"+lexeme+"|"+line+"]");
    }

}
