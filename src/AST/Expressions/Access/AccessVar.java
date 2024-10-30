package AST.Expressions.Access;

import AST.LocalVar;
import SymbolTable.Attributes.AttributeDeclaration;
import SymbolTable.Parameters.ParameterDeclaration;
import SymbolTable.SymbolTable;
import SymbolTable.MemberDeclaration;
import SymbolTable.Types.MemberType;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class AccessVar extends AccessMember{


    AttributeDeclaration attribute;

    boolean isStatic = true;

    public AccessVar() {
        super(null);
    }

    public boolean isCorrect() throws SemanticalErrorException{
        LocalVar var = parent.getLocalVar(this.name.getLexeme());
        if(var != null){
            this.type = var.getType();

            return true;
        }
        ParameterDeclaration par = SymbolTable.visibleParameter(this.name);
        if(par == null){
            AttributeDeclaration a = SymbolTable.visibleAttribute(this.name);
            this.attribute = a;
            if(a!= null) {
                type = a.getType();
                isStatic = a.isStatic();
            }
        }else {
            this.type = par.getType();
        }

        if(type == null) throw new SemanticalErrorException(this.name,"Variable "+this.name.getLexeme()+" not found in current context");

        return true;
    }

    public MemberType getExpressionType(){
        return this.type;
    }

    @Override
    public void setMember(AccessMember parent) throws SemanticalErrorException{
        MemberType parentType = parent.getExpressionType();
        AttributeDeclaration a = parentType.hasAttribute(this);
        if(a == null) throw new SemanticalErrorException( this.name,"Attribute "+this.name.getLexeme()+" not found in "+parent.getExpressionType().getName());
        this.attribute = a;

        if (!a.isPublic()) throw new SemanticalErrorException(this.name, "Attribute cant be accessed because "+this.name.getLexeme()+" is not public");
        isStatic = a.isStatic();
        this.type = parentType.transformType(a.getType());
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
        return isStatic;
    }

}
