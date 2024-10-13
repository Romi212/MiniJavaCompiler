package AST.Expressions.BinaryOperations;

import SymbolTable.Types.MemberType;
import utils.Token;

public class CompareOperation extends BinaryExpression{

        public CompareOperation(Token operator) {
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
