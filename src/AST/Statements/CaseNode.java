package AST.Statements;

import AST.Expressions.ExpressionNode;
import AST.Expressions.Literals.LiteralValue;
import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

import java.util.ArrayList;

public class CaseNode extends StatementNode{
    private LiteralValue expression;
    private StatementNode statement;

    private  String label;

    public CaseNode(Token name, LiteralValue expression, StatementNode s){
        super(name);
        this.expression = expression;
        statement = s;

    }

    public void setLabel(String label){
        this.label = label;
    }

    public void setStatement(StatementNode statement){
        this.statement = statement;
    }

    @Override
    public boolean isCorrect() throws CompilerException {

        if(expression != null){
            expression.setParent(this);
            if(!expression.isCorrect()) throw new SemanticalErrorException(expression.getName(), "Case expression is not correct");
        }
        if(statement != null){
            statement.setParent(this);
            if(!statement.isCorrect()) throw new SemanticalErrorException(statement.getName(), "Case statement is not correct");
        }
        SymbolTable.removeLocalVar(getLocalVarSize());
        return true;
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

    public MemberType getExpressionType(){
        if(expression != null){
            return expression.getExpressionType();
        }
        else return null;
    }
    public boolean isBreakable(){
        return true;
    }


    public void generateExpression(){
        if(expression != null){
            fileWriter.add("DUP");
            expression.generate();
            fileWriter.add("EQ ; evaluo si enta en el caso");
            fileWriter.add("BT " + label);
        }else{
            fileWriter.add("JUMP " + label);
        }
    }
    public void generateStatement(){
        fileWriter.add(label + ": NOP");
        statement.setBreakLabel(getBreakLabel());
        statement.generate();
    }

}
