package SymbolTable.Types;

import AST.Expressions.Access.AccessMember;
import AST.Expressions.Access.AccessMethod;
import AST.Expressions.Access.AccessVar;
import SymbolTable.Attributes.AttributeDeclaration;
import SymbolTable.MemberDeclaration;
import SymbolTable.MethodDeclaration;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class WildCardType extends MemberType{

    public WildCardType(Token name){
        this.name = name;
    }

    @Override
    public MemberDeclaration hasMember(AccessMember link) throws SemanticalErrorException {
        return null;
    }

    @Override
    public MethodDeclaration hasMethod(AccessMethod accessMethod) throws SemanticalErrorException {
        return null;
    }

    @Override
    public AttributeDeclaration hasAttribute(AccessVar accessVar) throws SemanticalErrorException {
        return null;
    }

    @Override
    public boolean conformsTo(MemberType type) throws SemanticalErrorException {
        return true;
    }

    @Override
    public boolean conformsTo(String type) throws SemanticalErrorException {
        return true;
    }
}
