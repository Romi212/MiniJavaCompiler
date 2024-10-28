package AST.Statements;

import AST.Expressions.ExpressionNode;
import AST.Expressions.Literals.LiteralValue;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

import java.util.ArrayList;

public class CaseNode extends StatementNode{
    private LiteralValue expression;
    private StatementNode statement;

    public CaseNode(Token name, LiteralValue expression, StatementNode s){
        super(name);
        this.expression = expression;
        statement = s;
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
        //if(expression == null && statement == null) throw new SemanticalErrorException(name, "Default case statement cant be empty");
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
}
