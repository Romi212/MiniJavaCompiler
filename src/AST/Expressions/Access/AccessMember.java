package AST.Expressions.Access;

import AST.Expressions.ExpressionNode;
import SymbolTable.Types.MemberType;
import utils.Token;

import java.util.ArrayList;

public abstract class AccessMember extends AccessExpression{

    ArrayList<ExpressionNode> parameters;

    public AccessMember(Token name){
        super(name);
        parameters = new ArrayList<>();
    }

    public void addParameter(ExpressionNode parameter){
        parameters.add(parameter);
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
