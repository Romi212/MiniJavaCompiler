package SymbolTable.Types;

import utils.Exceptions.SemanticalErrorException;
import utils.Token;

import java.util.ArrayList;
import java.util.HashMap;

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

    public ArrayList<MemberObjectType> getAttributes() {
        return null;
    }
}
