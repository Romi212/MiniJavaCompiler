package AST.Statements;

import AST.Expressions.ExpressionNode;
import utils.Exceptions.CompilerException;
import utils.Token;

public class WhileNode extends  StatementNode{
    private ExpressionNode condition;
    private StatementNode statement;

    public WhileNode(ExpressionNode condition, StatementNode statement, Token name) {
        super(name);
        this.condition = condition;
        condition.setParent(this);
        this.statement = statement;
        statement.setParent(this);
    }

    @Override
    public boolean isCorrect() throws CompilerException {
        System.out.println("WhileNode"+ condition.toString());
        return condition.isCorrect() && statement.isCorrect();
    }

    public String toString(){
        return "WhileNode(" + condition.toString() + "){ \n " + statement.toString() + "}";
    }
}
