package AST.Expressions.Access;

import AST.Expressions.ExpressionNode;
import SymbolTable.Types.MemberType;
import utils.Token;

import java.util.ArrayList;

public class AccessChainedMember extends AccessMember{

    public AccessChainedMember(Token name){
        super(name);
    }


    @Override
    public boolean isCorrect() {
        return true;
    }

    @Override
    public MemberType getExpressionType() {
        return null;
    }
}
