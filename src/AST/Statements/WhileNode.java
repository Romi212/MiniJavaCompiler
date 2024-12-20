package AST.Statements;

import AST.Expressions.ExpressionNode;
import SymbolTable.SymbolTable;
import SymbolTable.Types.BooleanType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

public class WhileNode extends  StatementNode{
    private ExpressionNode condition;
    private StatementNode statement;

    public WhileNode(ExpressionNode condition, StatementNode statement, Token name) {
        super(name);
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public boolean isCorrect() throws CompilerException {
        if(condition == null) throw new SemanticalErrorException(name,"Condition is null");
        if(statement == null) throw new SemanticalErrorException(name,"Statement is null");
        condition.setParent(this);
        statement.setParent(this);
        if(!condition.isCorrect()) throw new SemanticalErrorException(condition.getName(),"Condition is not correct");
        if(!statement.isCorrect()) throw new SemanticalErrorException(statement.getName(),"Statement is not correct");
        if(!condition.getExpressionType().conformsTo("boolean")) throw new SemanticalErrorException(condition.getName(),"Condition is not boolean");
        return true;
    }

    public String toString(){
        return "WhileNode(" + condition.toString() + "){ \n " + statement.toString() + "}";
    }

    public boolean isBreakable(){
        return true;
    }

    public void generate(){
        String start = "start@" + name.getLexeme() + name.getLine();

        fileWriter.add(start + ": NOP");
        condition.generate();
        fileWriter.add("BF " + getEndLabel());
        statement.setBreakLabel(getEndLabel());
        statement.generate();
        fileWriter.add("JUMP " + start);

        int localVarSize = getLocalVarSize();

        fileWriter.add(getEndLabel()+": FMEM " + localVarSize);
        SymbolTable.removeLocalVar(localVarSize);


    }
}
