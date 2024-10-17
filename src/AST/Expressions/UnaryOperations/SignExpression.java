package AST.Expressions.UnaryOperations;

import SymbolTable.Types.MemberType;
import utils.Token;

public class SignExpression extends UnaryExpression{

    public SignExpression(Token operator) {
        super(operator);
    }




    public String toString(){
        return operator.getLexeme() + " " + expression.toString();
    }
}
