package utils.Exceptions;

public class MethodReturnedValue extends CompilerException{
    public MethodReturnedValue(String message){
        super(message);
    }

    @Override
    public String getLongMessage() {
        return null;
    }


}
