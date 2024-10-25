package SymbolTable.Types;

import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class BooleanType extends MemberPrimitiveType{
    public BooleanType(Token name){
        this.name = name;
    }


}
