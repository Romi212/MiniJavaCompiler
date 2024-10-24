package AST.Statements;

import AST.Expressions.ExpressionNode;
import AST.Expressions.Literals.LiteralValue;
import utils.Exceptions.CompilerException;

import java.util.ArrayList;

public class CaseNode extends StatementNode{
    private LiteralValue expression;
    private StatementNode statement;

    public CaseNode(LiteralValue expression, StatementNode s){
        super(null);
        this.expression = expression;
        if(expression!= null) {
            expression.setParent(this);
        }
        statement = s;
        statement.setParent(this);
    }

    public void setStatement(StatementNode statement){
        this.statement = statement;
        statement.setParent(this);
    }

    @Override
    public boolean isCorrect() throws CompilerException {
        boolean correct = (expression==null || expression.isCorrect()) && statement!= null  && statement.isCorrect();

        return correct;
    }

    public String toString(){

        String toReturn;
        if(expression!= null){
            toReturn = "CaseNode(" + expression.toString() + "): " + statement.toString();
        }else{
            toReturn = "default: " + statement.toString();
        }
        return toReturn;
    }
}
