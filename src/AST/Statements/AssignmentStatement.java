package AST.Statements;

import AST.Expressions.ExpressionNode;
import utils.Token;

import java.util.ArrayList;

public class AssignmentStatement extends StatementNode{

    private Token type;
    private ArrayList<Token> variables;
    private ExpressionNode expression;

    public AssignmentStatement(Token op) {
        super(op);
        this.variables = new ArrayList<>();
    }

    public void setType(Token type){
        this.type = type;
    }

    public void addVariable(Token variable){
        variables.add(variable);
    }

    public void setExpression(ExpressionNode expression){
        this.expression = expression;
    }
    @Override
    public boolean isCorrect() {
        return expression.isCorrect();
    }

    public String toString(){
        String toReturn = "";
        if(type != null){
            toReturn += type.getLexeme();
        }
        for(Token t : variables){
            toReturn += t.getLexeme() + " ";
        }
        if(expression != null)  toReturn += "= " + expression.toString() + ";";
        return toReturn;
    }
}
