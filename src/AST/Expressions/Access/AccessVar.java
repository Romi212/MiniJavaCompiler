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
        if (!a.isPublic()) throw new SemanticalErrorException(this.name, "Attribute cant be accessed because "+this.name.getLexeme()+" is not public");
        this.type = a.getType();
    }


}
