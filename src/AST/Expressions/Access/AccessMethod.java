package AST.Expressions.Access;

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
        if(method != null) return true;
        this.method = SymbolTable.findMethod(this.name,parameters.size());
        if(this.method == null){
            throw new SemanticalErrorException(name,"Method "+this.name.getLexeme()+" with "+parameters.size()+" parameters not found in current class");
        }
        this.type = method.getType();
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
        this.method = m;
        this.type = method.getType();
    }
}
