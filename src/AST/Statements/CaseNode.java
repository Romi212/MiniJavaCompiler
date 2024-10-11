package AST.Statements;

import AST.Expressions.ExpressionNode;

import java.util.ArrayList;

public class CaseNode extends StatementNode{
    private ExpressionNode expression;
    private StatementNode statement;

    public CaseNode(ExpressionNode expression){
        super(null);
        this.expression = expression;
    }

    public void setStatement(StatementNode statement){
        this.statement = statement;
    }

    @Override
    public boolean isCorrect() {
        boolean correct = expression.isCorrect();

        return correct;
    }
}
