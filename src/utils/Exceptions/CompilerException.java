package utils.Exceptions;

public abstract class CompilerException extends Exception{

    public CompilerException(String message){
        super(message);
    }


    abstract public String getLongMessage();
}
