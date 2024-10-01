package SymbolTable.Types;

import SymbolTable.SymbolTable;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class MemberObjectType extends MemberType{

    public MemberObjectType(Token name){
        this.name = name;
    }

    public boolean isCorrect() throws SemanticalErrorException {
        return SymbolTable.hasClass(this.name);
    }
}
