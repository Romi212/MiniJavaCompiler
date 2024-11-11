package AST.Expressions.Access;

import SymbolTable.SymbolTable;
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
        setName(first.getName());
    }

    public void addNext(Link chain){
        this.chain = chain;
    }

    @Override
    public boolean isCorrect() throws SemanticalErrorException {
        first.setParent(parent);
        if (!first.isCorrect()) throw new SemanticalErrorException(name, "First expression is not correct");
        if(SymbolTable.staticContext() && !first.isStatic()) throw new SemanticalErrorException(name,"Member "+this.name.getLexeme()+" is not static and cannot be called from a static context");
        if(chain != null){
            chain.isCorrect(first);

        }
        return true;
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

    public boolean isStatic(){
        return first.isStatic();

    }

    @Override
    public void generate() {
        first.generate();
        if(chain != null){
            chain.generate();
        }
    }
}
