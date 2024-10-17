package AST.Statements;

import AST.Expressions.ExpressionNode;
import utils.Exceptions.CompilerException;
import utils.Token;

public class AssignmentStatement extends StatementNode{


    private ExpressionNode expression;

    public AssignmentStatement(ExpressionNode expression) {
        super(null);
        this.expression = expression;
    }


    public void setExpression(ExpressionNode expression){
        this.expression = expression;
    }

    @Override
    public boolean isCorrect() throws CompilerException {
        return expression.isCorrect();
    }

    public String toString(){
        return expression.toString();
    }
}
