package AST.Expressions.Access;

import SymbolTable.Attributes.AttributeDeclaration;
import SymbolTable.SymbolTable;
import SymbolTable.MemberDeclaration;
import SymbolTable.Types.MemberType;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class AccessVar extends AccessMember{


    AttributeDeclaration attribute;

    public boolean isCorrect() throws SemanticalErrorException{
        MemberType type = SymbolTable.visibleVar(this.name);
        this.type = type;
        return true;
    }

    public MemberType getExpressionType(){
        return this.type;
    }

    @Override
    public void setMember(AccessMember parent) throws SemanticalErrorException{
        AttributeDeclaration a = parent.getExpressionType().hasAttribute(this);
        if(a == null) throw new SemanticalErrorException( this.name,"Attribute "+this.name.getLexeme()+" not found in "+parent.getExpressionType().getName());
        this.attribute = a;
        if(parent.isStatic() && !a.isStatic()) throw new SemanticalErrorException( this.name,"Attribute "+this.name.getLexeme()+" is not static and cannot be called from a static context");
        if (!a.isPublic()) throw new SemanticalErrorException(this.name, "Attribute cant be accessed because "+this.name.getLexeme()+" is not public");

        this.type = a.getType();
    }

    public String toString(){
        if (this.attribute == null) return this.name.getLexeme();
        return attribute.toString();
    }

    public boolean isStatement(){
        return false;
    }

    public boolean isAssignable(){
        return true;
    }

    public boolean isStatic(){
        return attribute!= null && attribute.isStatic();
    }

}
