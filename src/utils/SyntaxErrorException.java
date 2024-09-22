package utils;

public class SyntaxErrorException extends Throwable {


    private String personalizedMessage;
    private Token foundtoken;

    public SyntaxErrorException( Token found, String expected){
        super("[Error:"+found.getLexeme()+"|"+found.getLine()+"]");
        foundtoken = found;
        personalizedMessage = expected;

    }

    public String getLongMessage(){
        String longMessage = "Syntax error in line "+foundtoken.getLine()+":" +
                " Expected "+personalizedMessage+" but found ["+foundtoken.getLexeme()+"("+foundtoken.getToken()+")]";
        return longMessage;
    }
}
