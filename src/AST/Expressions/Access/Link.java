package AST.Expressions.Access;

import AST.Expressions.ExpressionNode;
import SymbolTable.MemberDeclaration;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

import java.util.ArrayList;

public class Link extends AccessExpression{

    Link next;
    AccessMember link;

    public Link(){
        super(null);
    }

    public void setLink(AccessMember link){
        this.link = link;
        setName(link.name);
    }

    public void setNext(Link next){
        this.next = next;
    }


    public boolean isCorrect(AccessMember parent) throws SemanticalErrorException {
        link.setParent(parent);
        link.setMember(parent);
        if(!parent.isStaticClass())link.setHasPrevious(true);
        if(parent.isStaticClass() && !link.isStatic()){
            throw new SemanticalErrorException(link.getName(),"Static class can only access static members");
        }
        link.setWrite(write);
        if(next != null){
            link.setWrite(false);
            next.setWrite(write);
            return next.isCorrect(link);
        }
        return true;
    }

    @Override
    public boolean isCorrect() throws SemanticalErrorException {
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
        String toReturn = link.toString();

        if(next != null){
            toReturn += "."+next.toString();
        }
        return toReturn;
    }

    public boolean isStatement() {
        if(next == null) return link.isStatement();
        else return next.isStatement();
    }

    public boolean isAssignable(){
        if(next == null) return link.isAssignable();
        else return next.isAssignable();
    }

    public void printPop(){
        if(next!= null) next.printPop();
        else if( !link.isAssignable() && !link.getExpressionType().isVoid() && !link.isNewObject()) fileWriter.add("POP");
    }

    public void generate(){
        link.generate();
        if(next != null){
            next.generate();
        }
    }

}
