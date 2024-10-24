package AST.Expressions.Access;

import AST.Expressions.ExpressionNode;
import SymbolTable.MemberDeclaration;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

import java.util.ArrayList;

public class Link extends AccessExpression{

    Link next;
    AccessMember link;

    public Link(){
        super(null);
    }

    public void setLink(AccessMember link){
        this.link = link;
    }

    public void setNext(Link next){
        this.next = next;
    }


    public boolean isCorrect(AccessMember parent) throws CompilerException {
        link.setMember(parent);

        if(next != null){
            return next.isCorrect(link);
        }
        return true;
    }

    @Override
    public boolean isCorrect() throws CompilerException {
        return false;
    }

    @Override
    public MemberType getExpressionType() {

        if(next != null){
            return next.getExpressionType();
        }
        return link.getExpressionType();
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
