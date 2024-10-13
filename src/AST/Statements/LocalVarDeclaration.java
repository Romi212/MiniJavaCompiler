package AST.Statements;

import AST.Expressions.ExpressionNode;
import utils.Token;

public class LocalVarDeclaration extends StatementNode{

    private Token type;
    private ExpressionNode initialization;

    public LocalVarDeclaration(Token var) {
        super(var);

    }

    public void setExpression(ExpressionNode expression){
        this.initialization = expression;
    }

    @Override
    public boolean isCorrect() {
        return true;
    }

    public String toString(){
        String toReturn = "var "+ this.name;
        if(initialization != null){
            toReturn += " = " + initialization.toString();
        }
        return toReturn+";";
    }
}
