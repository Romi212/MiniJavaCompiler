package AST.Expressions.Access;

import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;

public class ChainedExpression extends AccessExpression{

    AccessMember first;

    Link chain;
    public ChainedExpression(){
        super(null);
    }

    public void setFirst(AccessMember first){
        this.first = first;
        System.out.println("Setting name to "+first.getName());
        setName(first.getName());
    }

    public void addNext(Link chain){
        this.chain = chain;
    }

    @Override
    public boolean isCorrect() throws SemanticalErrorException {
        boolean isCorrect = first.isCorrect();
        isCorrect = (staticContext == first.isStatic()) && isCorrect;
        if(chain != null){
            isCorrect = isCorrect && chain.isCorrect(first);

        }
        return isCorrect;
    }

    @Override
    public MemberType getExpressionType() {

        if(chain == null){
            return first.getExpressionType();
        }
        return chain.getExpressionType();
    }

    public String toString(){

        return first.toString() + (chain != null ? chain.toString() : "");
    }

    @Override
    public boolean isStatement() {
        if(chain == null){
            return first.isStatement();
        }
        return chain.isStatement();
    }

    public boolean isAssignable(){
        if(chain == null){
            return first.isAssignable();
        }
        return chain.isAssignable();
    }
}
