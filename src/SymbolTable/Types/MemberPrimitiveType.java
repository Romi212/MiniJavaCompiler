package SymbolTable.Types;

import AST.Expressions.Access.AccessMember;
import SymbolTable.MemberDeclaration;
import utils.Exceptions.SemanticalErrorException;

public abstract class MemberPrimitiveType extends MemberType {

    public MemberDeclaration hasMember(AccessMember link) throws SemanticalErrorException{
        throw new SemanticalErrorException(this.name, "Primitive type member "+this.name.getLexeme()+" has no members");
    }
}
