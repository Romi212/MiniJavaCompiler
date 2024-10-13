package AST.Expressions.Access;

import AST.Expressions.ExpressionNode;
import SymbolTable.Types.MemberType;
import utils.Token;

public class AccessPE extends AccessMember{

    ExpressionNode exp;
    public AccessPE(ExpressionNode name){
        super(null);
        exp = name;
    }
    @Override
    public boolean isCorrect() {
        return true;
    }

    @Override
    public MemberType getExpressionType() {
        return null;
    }

    public String toString(){
        return exp.toString();
    }
}
