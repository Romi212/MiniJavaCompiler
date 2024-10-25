package AST.Expressions.BinaryOperations;

import AST.Expressions.ExpressionNode;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Token;

abstract public class BinaryExpression extends ExpressionNode {

    protected ExpressionNode left;
    protected ExpressionNode right;
    protected Token operator;

    public BinaryExpression(Token operator){
        this.operator = operator;
        setName(operator);
    }

    public void addLeft(ExpressionNode left){
        this.left = left;
        left.setParent(parent);
    }

    public void addRight(ExpressionNode right){
        this.right = right;
        right.setParent(parent);
    }



    abstract public MemberType getExpressionType();

    public String toString(){
        return left.toString() + " " + operator.getLexeme() + " " + right.toString();
    }
}
