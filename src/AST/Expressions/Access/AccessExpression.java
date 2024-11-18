package AST.Expressions.Access;

import AST.Expressions.ExpressionNode;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

import java.util.ArrayList;

public abstract class AccessExpression extends ExpressionNode {


    ArrayList<ExpressionNode> parameters;

    public AccessExpression(Token name){
        super(name);
    }
    public abstract boolean isCorrect() throws SemanticalErrorException;
    public void addParameter(ExpressionNode parameter){

        parameters.add(parameter);

    }

    public int getParametersSize(){
        return parameters.size();
    }
    public abstract MemberType getExpressionType();

    public String toString(){
        return name.getLexeme();
    }



}
