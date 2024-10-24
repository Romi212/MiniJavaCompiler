package SymbolTable.Types;

import AST.Expressions.Access.AccessMember;
import AST.Expressions.Access.AccessMethod;
import AST.Expressions.Access.AccessVar;
import SymbolTable.Attributes.AttributeDeclaration;
import SymbolTable.MemberDeclaration;
import SymbolTable.MethodDeclaration;
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

    abstract public MethodDeclaration hasMethod(AccessMethod accessMethod) throws SemanticalErrorException;

    abstract public AttributeDeclaration hasAttribute(AccessVar accessVar) throws SemanticalErrorException;
}
