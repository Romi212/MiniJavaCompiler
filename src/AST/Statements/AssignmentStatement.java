package AST.Statements;

import AST.Expressions.ExpressionNode;
import utils.Exceptions.CompilerException;
import utils.Token;

public class AssignmentStatement extends StatementNode{


    private ExpressionNode expression;

    public AssignmentStatement(ExpressionNode expression) {
        super(null);
        this.expression = expression;
        if( expression!= null) expression.setParent(this);
    }


    public void setExpression(ExpressionNode expression){
        this.expression = expression;
        expression.setParent(this);
    }

    @Override
    public boolean isCorrect() throws CompilerException {
        return expression.isCorrect();
    }

    public String toString(){
        return expression.toString();
    }
}
