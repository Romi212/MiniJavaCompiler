package AST.Statements;

import AST.Expressions.ExpressionNode;
import utils.Exceptions.CompilerException;

public class ReturnNode extends StatementNode{
    private ExpressionNode expression;

    public ReturnNode(ExpressionNode expression){
        super(null);
        this.expression = expression;
        if(expression != null){
            expression.setParent(this);
        }
    }

    @Override
    public boolean isCorrect() throws CompilerException {
        return (expression == null || expression.isCorrect());
    }

    public String toString(){
        return "ReturnNode(" + expression.toString() + ")";
    }
}
