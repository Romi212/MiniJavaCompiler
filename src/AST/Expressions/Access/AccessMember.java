package AST.Expressions.Access;

import AST.Expressions.ExpressionNode;
import SymbolTable.MemberDeclaration;
import SymbolTable.Types.MemberType;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AccessMember extends AccessExpression{



    MemberType type;


    boolean hasPrevious = false;

    public AccessMember(Token name){
        super(name);
        parameters = new ArrayList<>();
    }


    public void setHasPrevious(boolean prev){
        hasPrevious = prev;
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



    abstract  public void setMember(AccessMember hasMember) throws SemanticalErrorException;

    public boolean isStatic(){
        return false;
    }
    public boolean isStaticClass(){
        return false;
    }
}
