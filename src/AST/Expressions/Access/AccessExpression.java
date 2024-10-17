package AST.Expressions.Access;

import AST.Expressions.ExpressionNode;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Token;

import java.util.ArrayList;

public abstract class AccessExpression extends ExpressionNode {

    Token name;
    ArrayList<ExpressionNode> parameters;

    public AccessExpression(Token name){
        this.name = name;
    }
    public abstract boolean isCorrect() throws CompilerException;
    public void addParameter(ExpressionNode parameter){
        parameters.add(parameter);
    }


    public abstract MemberType getExpressionType();

    public String toString(){
        return name.getLexeme();
    }


}
