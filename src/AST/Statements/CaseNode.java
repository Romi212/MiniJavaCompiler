package AST.Statements;

import AST.Expressions.ExpressionNode;
import AST.Expressions.Literals.LiteralValue;

import java.util.ArrayList;

public class CaseNode extends StatementNode{
    private LiteralValue expression;
    private StatementNode statement;

    public CaseNode(LiteralValue expression, StatementNode s){
        super(null);
        this.expression = expression;
        statement = s;
    }

    public void setStatement(StatementNode statement){
        this.statement = statement;
    }

    @Override
    public boolean isCorrect() {
        boolean correct = expression.isCorrect();

        return correct;
    }

    public String toString(){
        return "CaseNode(" + expression.toString() + "): " + statement.toString();
    }
}
