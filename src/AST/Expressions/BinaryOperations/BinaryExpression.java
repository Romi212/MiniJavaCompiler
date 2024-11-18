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
        super(operator);
        this.operator = operator;

    }

    public void addLeft(ExpressionNode left){
        this.left = left;
    }

    public void addRight(ExpressionNode right){
        this.right = right;
    }



    public boolean isStatic(){
        return left.isStatic() && right.isStatic();
    }

    abstract public MemberType getExpressionType();

    public String toString(){
        return left.toString() + " " + operator.getLexeme() + " " + right.toString();
    }

    public boolean precedes(Token operator){
        return true;
    }

    public BinaryExpression extendAST(ExpressionNode leftExp, BinaryExpression operation) {
        if(precedes(operation.getName()))
            return super.extendAST(leftExp, operation);
        else{
            BinaryExpression leftNode = this.left.extendAST(leftExp, operation);
            this.left = leftNode;
        }
        return this;
    }
}
