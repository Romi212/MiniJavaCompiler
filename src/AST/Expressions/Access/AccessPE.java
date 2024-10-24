package AST.Expressions.Access;

import AST.Expressions.ExpressionNode;
import SymbolTable.MemberDeclaration;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Token;

public class AccessPE extends AccessMember{

    ExpressionNode exp;
    public AccessPE(ExpressionNode name){
        super();
        setName(name.getName());
        exp = name;
    }
    @Override
    public boolean isCorrect() throws CompilerException {
        return exp.isCorrect();
    }

    @Override
    public MemberType getExpressionType() {
        return exp.getExpressionType();
    }

    public String toString(){
        return exp.toString();
    }

    @Override
    public void setMember(AccessMember hasMember) {

    }
}
