package AST.Statements;

import AST.Expressions.ExpressionNode;

public class ReturnNode extends StatementNode{
    private ExpressionNode expression;

    public ReturnNode(ExpressionNode expression){
        super(null);
        this.expression = expression;
    }

    @Override
    public boolean isCorrect() {
        return expression.isCorrect();
    }

    public String toString(){
        return "ReturnNode(" + expression.toString() + ")";
    }
}
