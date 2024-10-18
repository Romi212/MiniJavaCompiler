package SymbolTable.Types;

import AST.Expressions.Access.AccessMember;
import SymbolTable.MemberDeclaration;
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

    abstract public MemberDeclaration hasMember(AccessMember link) throws SemanticalErrorException;
}
