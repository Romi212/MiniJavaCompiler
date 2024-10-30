package AST.Statements;

import AST.Expressions.ExpressionNode;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class ForStatement extends StatementNode{
    private ExpressionNode init;
    private ExpressionNode condition;
    private ExpressionNode increment;
    private StatementNode body;

    public ForStatement(Token name){
        super(name);
    }

    @Override
    public boolean isCorrect() throws CompilerException {

        if(init == null) throw new SemanticalErrorException(name, "For statement must have an initialization expression");
        init.setParent(this);
        if(!init.isCorrect()) throw new SemanticalErrorException(init.getName(), "Initialization expression is not correct");
        if(!init.isStatement() || init.isStatic()) throw new SemanticalErrorException(init.getName(), "Initialization expression must be an assignment");
        if(condition == null) throw new SemanticalErrorException(name, "For statement must have a condition expression");
        condition.setParent(this);
        if(!condition.isCorrect()) throw new SemanticalErrorException(condition.getName(), "Condition expression is not correct");
        if(!condition.getExpressionType().conformsTo("boolean")) throw new SemanticalErrorException(condition.getName(), "Condition expression must be of type boolean");
        if(increment == null) throw new SemanticalErrorException(name, "For statement must have an increment expression");
        increment.setParent(this);
        if(!increment.isCorrect()) throw new SemanticalErrorException(increment.getName(), "Increment expression is not correct");
        if(!increment.isStatement() || increment.isStatic()) throw new SemanticalErrorException(increment.getName(), "Increment expression must be an assignment");

        if(body == null) throw new SemanticalErrorException(name, "For statement must have a body");
        body.setParent(this);
        if(!body.isCorrect()) throw new SemanticalErrorException(body.getName(), "Body is not correct");

        return true;
    }

    public StatementNode getInit() {
        return init;
    }


    @Override
    public String toString() {
        return "ForStatement{" +
                "init=" + init +
                ", condition=" + condition +
                ", increment=" + increment +
                ", body=" + body +
                '}';
    }

    public void setBody(StatementNode statement) {
        this.body = statement;
    }

    public void setInit(ExpressionNode exp) {
        this.init =  exp;
    }

    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
    }

    public void setIncrement(ExpressionNode increment) {
        this.increment = increment;
    }
}
