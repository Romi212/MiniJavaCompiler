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
    }

    public void addRight(ExpressionNode right){
        this.right = right;
    }

    public void setParent(ExpressionNode parent){
        this.parent = parent;
        if(left != null) left.setParent(parent);
        if(right != null) right.setParent(parent);
        staticContext = parent.isStaticContext();
    }



    abstract public MemberType getExpressionType();

    public String toString(){
        return left.toString() + " " + operator.getLexeme() + " " + right.toString();
    }
}
