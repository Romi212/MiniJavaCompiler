package utils;

public class SyntaxErrorException extends Throwable {

    private String expectedToken;

    private String personalizedMessage;
    private Token foundtoken;

    public SyntaxErrorException(String expected, Token found, String message){
        super("[Error:"+found.getLexeme()+"|"+found.getLine()+"]");
        expectedToken = expected;
        foundtoken = found;
        personalizedMessage = message;

    }

    public String getLongMessage(){
        String longMessage = "Syntax error in line "+foundtoken.getLine()+":" +
                " Expected "+personalizedMessage+"] but found ["+foundtoken.getLexeme()+"("+foundtoken.getToken()+")]";
        return longMessage;
    }
}
