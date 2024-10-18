package AST.Expressions.Access;

import AST.Expressions.ExpressionNode;
import SymbolTable.MemberDeclaration;
import SymbolTable.Types.MemberType;
import utils.Token;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AccessMember extends AccessExpression{



    MemberType type;

    public AccessMember(){
        super(null);
        parameters = new ArrayList<>();
    }
    public void setName(Token name){
        this.name = name;
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


        return toReturn;
    }

    public Token getName() {
        return name;
    }


    abstract  public void setMember(MemberDeclaration hasMember);
}
