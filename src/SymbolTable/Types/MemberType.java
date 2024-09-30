package SymbolTable.Types;

import utils.Token;

public abstract class MemberType {

    protected Token name;

    public String getName(){
        return name.getLexeme();
    }
}
