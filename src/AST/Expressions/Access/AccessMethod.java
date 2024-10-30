package AST.Expressions.Access;

import AST.Expressions.ExpressionNode;
import SymbolTable.MethodDeclaration;
import SymbolTable.SymbolTable;
import SymbolTable.MemberDeclaration;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class AccessMethod extends AccessMember{

    MethodDeclaration method;

    public AccessMethod(){
        super(null);
    }
    @Override
    public boolean isCorrect() throws SemanticalErrorException {
        if(method == null) {
            this.method = SymbolTable.findMethod(this.name,parameters.size());
            if(this.method == null){
                throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" with "+parameters.size()+" parameters not found in current class");
            }

            }
        this.type = method.getType();
        System.out.println("Method: "+this.name.getLexeme());
        for( ExpressionNode e : parameters){
            e.setParent(parent);
            if(!e.isCorrect()) throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" has incorrect parameter");
            MemberType type = e.getExpressionType();
            if(!type.conformsTo(method.getParameterType(parameters.indexOf(e)))) throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" has parameter that does not conform to the method");
        }
        return true;
    }
    @Override
    public MemberType getExpressionType() {
        return type;
    }

    @Override
    public void setMember(AccessMember parent) throws SemanticalErrorException{
        MemberType parentType = parent.getExpressionType();
        MethodDeclaration m = parentType.hasMethod(this);
        if(m == null) throw new SemanticalErrorException( this.name,"Method "+this.name.getLexeme()+" with "+parameters.size()+" not found in "+parent.getExpressionType().getName());

        if (!m.isPublic()) throw new SemanticalErrorException(this.name, "Method cant be accessed because "+this.name.getLexeme()+" is not public");

        for( ExpressionNode e : parameters){
            if(!e.isCorrect()) throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" has incorrect parameter");
            MemberType type = e.getExpressionType();
            if(!type.conformsTo(parentType.transformType(m.getParameterType(parameters.indexOf(e))))) throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" has parameter that does not conform to the method");
        }
        this.method = m;
        this.type = parentType.transformType(method.getType());
    }
    public boolean isStatement(){
        return true;
    }

    public boolean isStatic(){
        return method.isStatic();
    }
}
