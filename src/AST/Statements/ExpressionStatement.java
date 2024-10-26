package AST.Statements;

import AST.Expressions.ExpressionNode;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class ExpressionStatement extends StatementNode{


    private ExpressionNode expression;

    public ExpressionStatement(ExpressionNode expression) {
        super(null);
        this.expression = expression;
    }


    public void setExpression(ExpressionNode expression){
        this.expression = expression;
    }

    public void setParent(StatementNode parent){
        this.parent = parent;
        staticContext = parent.isStaticContext();
        expression.setParent(parent);
    }

    @Override
    public boolean isCorrect() throws CompilerException {
        if(expression.isCorrect()){

            if(!expression.isStatement()) {
                throw new SemanticalErrorException(expression.getName(), "Expression is not a statement");
            }
        }
        return true;
    }

    public String toString(){
        return expression.toString();
    }
}
