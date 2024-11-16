package AST.Expressions.Access;

import AST.Expressions.ExpressionNode;
import SymbolTable.MemberDeclaration;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class AccessPE extends AccessMember{

    ExpressionNode exp;
    public AccessPE(ExpressionNode name){
        super(name.getName());
        exp = name;
    }
    @Override
    public boolean isCorrect() throws SemanticalErrorException {
        exp.setParent(parent);
        if(!exp.isCorrect()) throw new SemanticalErrorException(name,"Expression is not correct");
        return true;
    }

    @Override
    public MemberType getExpressionType() {
        return exp.getExpressionType();
    }

    public String toString(){
        return exp.toString();
    }

    @Override
    public void setMember(AccessMember hasMember) {

    }

    public boolean isStatic(){
        return exp.isStatic();
    }

    public void generate(){
        exp.generate();
    }
}
