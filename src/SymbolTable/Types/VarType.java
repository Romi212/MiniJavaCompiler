package SymbolTable.Types;

import utils.Token;

public class VarType extends MemberType{
    private Token name;
    private MemberType type;

    public VarType(Token name){
        this.name = name;

    }


    public MemberType getType(){
        return this.type;
    }


}
