package AST.Expressions.Access;

import AST.Expressions.ExpressionNode;
import SymbolTable.Types.MemberType;
import utils.Token;

import java.util.ArrayList;

public abstract class AccessMember extends AccessExpression{



    ChainedMembers chain;

    public AccessMember(Token name){
        super(name);
        parameters = new ArrayList<>();
    }


    public void addChain(ChainedMembers chain){
        this.chain = chain;
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

        if(chain != null){
            toReturn += "." + chain.toString();
        }
        return toReturn;
    }
}
