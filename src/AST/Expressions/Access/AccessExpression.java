package AST.Expressions.Access;

import AST.Expressions.ExpressionNode;
import SymbolTable.Types.MemberType;
import utils.Token;

public abstract class AccessExpression extends ExpressionNode {

    Token name;

    public AccessExpression(Token name){
        this.name = name;
    }
    public abstract boolean isCorrect();

    public abstract MemberType getExpressionType();


}
