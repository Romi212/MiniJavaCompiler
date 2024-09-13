package utils;

public class SyntaxErrorException extends Throwable {

    public SyntaxErrorException(String expected, String found, int line, int column){
        super("Syntax error: Expected "+expected+" but found "+found+" at line "+line+", column "+column);
    }

}
