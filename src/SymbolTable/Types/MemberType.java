package SymbolTable.Types;

import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public abstract class MemberType {

    protected Token name;

    public String getName(){
        return name.getLexeme();
    }

    public boolean isCorrect() throws SemanticalErrorException {
        return true;
    }

    public Token getToken() {
        return name;
    }
}
