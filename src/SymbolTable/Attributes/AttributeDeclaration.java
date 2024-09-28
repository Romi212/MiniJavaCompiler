package SymbolTable.Attributes;

import SymbolTable.Types.MemberType;
import utils.Token;

public class AttributeDeclaration {

    private Token name;
    private MemberType type;

    public AttributeDeclaration(Token name, MemberType type){
        this.name = name;
        this.type = type;
    }

    public Token getName(){
        return this.name;
    }
    public MemberType getType(){
        return this.type;
    }
    public String toString(){
        return "[" + this.name.getLexeme() + " (" + this.type.getName()+")]";
    }
}
