package AST.Expressions.BinaryOperations;

import SymbolTable.Types.MemberType;
import utils.Token;

public class NumberOperation extends BinaryExpression{

        public NumberOperation(Token operator) {
            super(operator);
        }

        @Override
        public boolean isCorrect() {
            return left.isCorrect() && right.isCorrect();
        }

        @Override
        public MemberType getExpressionType() {
            return null;
        }
}
