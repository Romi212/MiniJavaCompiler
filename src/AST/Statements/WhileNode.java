package AST.Statements;

import AST.Expressions.ExpressionNode;
import SymbolTable.Types.BooleanType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
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
        if(!condition.isCorrect()) throw new SemanticalErrorException(condition.getName(),"Condition is not correct");
        if(!statement.isCorrect()) throw new SemanticalErrorException(statement.getName(),"Statement is not correct");
        if(!condition.getExpressionType().conformsTo(new BooleanType(new Token("rw_boolean","boolean",-1)))) throw new SemanticalErrorException(condition.getName(),"Condition is not boolean");
        return true;
    }

    public String toString(){
        return "WhileNode(" + condition.toString() + "){ \n " + statement.toString() + "}";
    }

    public boolean isBreakable(){
        return true;
    }
}
