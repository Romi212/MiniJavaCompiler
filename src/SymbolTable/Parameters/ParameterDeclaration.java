package SymbolTable.Parameters;

import SymbolTable.Types.MemberType;
import utils.Token;

public class ParameterDeclaration {
    private Token name;
    private MemberType type;

    public ParameterDeclaration(Token name, MemberType type){
        this.name = name;
        this.type = type;
    }

    public Token getName() {
        return name;
    }

    public String toString(){
        return "[" + this.name.getLexeme() + " (" + this.type.getName()+")]";
    }


}
