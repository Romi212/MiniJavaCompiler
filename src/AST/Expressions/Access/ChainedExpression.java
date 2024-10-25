package AST.Expressions.Access;

import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;

public class ChainedExpression extends AccessExpression{

    AccessMember first;

    Link chain;
    public ChainedExpression(){
        super(null);
    }

    public void setFirst(AccessMember first){
        this.first = first;
    }

    public void addNext(Link chain){
        this.chain = chain;
    }

    @Override
    public boolean isCorrect() throws CompilerException {
        boolean isCorrect = first.isCorrect();
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
}
