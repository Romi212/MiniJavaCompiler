package AST.Expressions.Access;

import AST.Expressions.ExpressionNode;
import SymbolTable.MethodDeclaration;
import SymbolTable.SymbolTable;
import SymbolTable.MemberDeclaration;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;

public class AccessMethod extends AccessMember{

    MethodDeclaration method;

    public AccessMethod(){
        super();
    }
    @Override
    public boolean isCorrect() throws CompilerException {
        if(method == null) {
            this.method = SymbolTable.findMethod(this.name,parameters.size());
            if(this.method == null){
                throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" with "+parameters.size()+" parameters not found in current class");
            }
            this.type = method.getType();
            }
        for( ExpressionNode e : parameters){
            if(!e.isCorrect()) throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" has incorrect parameter");
            MemberType type = e.getExpressionType();
            if(!type.conformsTo(method.getParameterType(parameters.indexOf(e)))) throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" has parameter that does not conform to the method");
        }
        return true;
    }
    @Override
    public MemberType getExpressionType() {
        return method.getType();
    }

    @Override
    public void setMember(AccessMember parent) throws SemanticalErrorException{
        MethodDeclaration m = parent.getExpressionType().hasMethod(this);
        if(m == null) throw new SemanticalErrorException( this.name,"Method "+this.name.getLexeme()+" with "+parameters.size()+" not found in "+parent.getExpressionType().getName());
        if(parent.isStatic() && !m.isStatic()) throw new SemanticalErrorException( this.name,"Method "+this.name.getLexeme()+" is not static and cannot be called from a static context");
        if (!m.isPublic()) throw new SemanticalErrorException(this.name, "Method cant be accessed because "+this.name.getLexeme()+" is not public");
        this.method = m;
        this.type = method.getType();
    }
    public boolean isStatement(){
        return true;
    }
}
