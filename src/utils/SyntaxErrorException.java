package utils;

public class SyntaxErrorException extends Throwable {

    private String expectedToken;
    private Token foundtoken;

    public SyntaxErrorException(String expected, Token found){
        super("[Error:"+found.getLexeme()+"|"+found.getLine()+"]");
        expectedToken = expected;
        foundtoken = found;

    }

    public String getLongMessage(){
        String longMessage = "Syntax error in line "+foundtoken.getLine()+", column "+foundtoken.getColumn()+": Expected ["+expectedToken+"] but found ["+foundtoken.getLexeme()+"("+foundtoken.getToken()+")]";
        return longMessage;
    }
}
