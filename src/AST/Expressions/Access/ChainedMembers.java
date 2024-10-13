package AST.Expressions.Access;

import AST.Expressions.ExpressionNode;
import SymbolTable.Types.MemberType;
import utils.Token;

import java.util.ArrayList;

public class ChainedMembers extends AccessExpression{

    ChainedMembers next;
    ArrayList<ExpressionNode> parameters;

    public ChainedMembers(Token name){
        super(name);
    }

    public void setNext(ChainedMembers next){
        this.next = next;
    }

    @Override
    public boolean isCorrect() {
        return true;
    }

    @Override
    public MemberType getExpressionType() {
        return null;
    }

    public String toString(){
        String toReturn = name.getLexeme();

        if(parameters != null){
            toReturn += "(";
            for(ExpressionNode parameter : parameters){
                toReturn += parameter.toString() + ",";
            }
            toReturn += ")";
        }

        if(next != null){
            toReturn += "."+next.toString();
        }
        return toReturn;
    }
}
