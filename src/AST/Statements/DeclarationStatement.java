package AST.Statements;

import AST.Expressions.ExpressionNode;
import AST.LocalVar;
import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Token;

import java.util.ArrayList;

public class DeclarationStatement extends StatementNode{

    private MemberType type;
    private ArrayList<Token> variables;
    private ExpressionNode expression;

    public DeclarationStatement(Token op) {
        super(op);
        this.variables = new ArrayList<>();
    }

    public void setType(MemberType type){
        this.type = type;
    }

    public void addVariable(Token variable){
        variables.add(variable);
    }

    public void setExpression(ExpressionNode expression){
        this.expression = expression;
        expression.setParent(this);
    }
    @Override
    public boolean isCorrect() throws CompilerException {
        boolean correct = true;
        if(expression!= null) correct = expression.isCorrect();
        if(correct){
            for(Token t : variables){
                LocalVar localVar = new LocalVar(t, type);
                //SymbolTable.addLocalVar(localVar);
                parent.addLocalVar(localVar);
            }
        }
        return correct;
    }

    public String toString(){
        String toReturn = "";
        if(type != null){
            toReturn += type.getToken().getLexeme();
        }
        for(Token t : variables){
            toReturn += t.getLexeme() + " ";
        }
        if(expression != null)  toReturn += "= " + expression.toString() + ";";
        return toReturn;
    }
}
