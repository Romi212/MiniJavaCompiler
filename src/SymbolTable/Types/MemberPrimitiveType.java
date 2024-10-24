package SymbolTable.Types;

import AST.Expressions.Access.AccessMember;
import AST.Expressions.Access.AccessMethod;
import AST.Expressions.Access.AccessVar;
import SymbolTable.Attributes.AttributeDeclaration;
import SymbolTable.MemberDeclaration;
import SymbolTable.MethodDeclaration;
import utils.Exceptions.SemanticalErrorException;

public abstract class MemberPrimitiveType extends MemberType {

    public MemberDeclaration hasMember(AccessMember link) throws SemanticalErrorException{
        throw new SemanticalErrorException(this.name, "Primitive type member "+this.name.getLexeme()+" has no members");
    }
    public MethodDeclaration hasMethod(AccessMethod link) throws SemanticalErrorException{
        throw new SemanticalErrorException(this.name, "Primitive type member "+this.name.getLexeme()+" has no members");
    }
    public AttributeDeclaration hasAttribute(AccessVar link) throws SemanticalErrorException{
        throw new SemanticalErrorException(this.name, "Primitive type member "+this.name.getLexeme()+" has no members");
    }
}
