package AST.Statements;

import AST.Expressions.ExpressionNode;
import utils.Token;

public class WhileNode extends  StatementNode{
    private ExpressionNode condition;
    private StatementNode statement;

    public WhileNode(ExpressionNode condition, StatementNode statement, Token name) {
        super(name);
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public boolean isCorrect() {
        return condition.isCorrect() && statement.isCorrect();
    }

    public String toString(){
        return "WhileNode(" + condition.toString() + "){ \n " + statement.toString() + "}";
    }
}
