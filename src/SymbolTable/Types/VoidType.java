package SymbolTable.Types;

import utils.Token;

public class VoidType extends MemberPrimitiveType{

    public VoidType(Token name){
        this.name = name;
    }

    public boolean isOrdinal() {
        return false;
    }
    public boolean isVoid(){
        return true;
    }

}
