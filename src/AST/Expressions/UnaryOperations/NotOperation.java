package AST.Expressions.UnaryOperations;

import SymbolTable.Types.MemberType;
import utils.Token;

public class NotOperation extends UnaryExpression{

            public NotOperation(Token operator) {
                super(operator);
            }

            @Override
            public boolean isCorrect() {
                return expression.isCorrect();
            }

            @Override
            public MemberType getExpressionType() {
                return expression.getExpressionType();
            }

            public String toString(){
                return operator.getLexeme() + " " + expression.toString();
            }
}
