package AST.Expressions.UnaryOperations;

import SymbolTable.Types.IntegerType;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class SignExpression extends UnaryExpression{

    public SignExpression(Token operator) {
        super(operator);
    }


    @Override
    public boolean isCorrect() throws SemanticalErrorException {
        if(expression == null) throw new SemanticalErrorException(operator,"Unary expression has no expression");
        expression.setParent(parent);
        if(!expression.isCorrect()) throw new SemanticalErrorException(operator,"Unary expression has incorrect expression");
        if(!expression.getExpressionType().conformsTo(new IntegerType(new Token("rw_int","int",-1)))) throw new SemanticalErrorException(operator,"Unary expression has expression that does not conform to int");
        return true;
    }

    public String toString(){
        return operator.getLexeme() + " " + expression.toString();
    }
}
