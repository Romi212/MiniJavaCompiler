package AST.Expressions.UnaryOperations;

import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Token;

public class NotOperation extends UnaryExpression{

            public NotOperation(Token operator) {
                super(operator);
            }


            public String toString(){
                return operator.getLexeme() + " " + expression.toString();
            }
}
