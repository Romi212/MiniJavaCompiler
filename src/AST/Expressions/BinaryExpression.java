package AST.Expressions;

import SymbolTable.Types.MemberType;
import utils.Token;

public class BinaryExpression extends ExpressionNode{

    private ExpressionNode left;
    private ExpressionNode right;
    private Token operator;

    public BinaryExpression(Token operator){
        this.operator = operator;
    }

    public void addLeft(ExpressionNode left){
        this.left = left;
    }

    public void addRight(ExpressionNode right){
        this.right = right;
    }

    @Override
    public boolean isCorrect() {
        return left.isCorrect() && right.isCorrect();
    }

    @Override
    public MemberType getExpressionType() {
        return null;
    }
}
