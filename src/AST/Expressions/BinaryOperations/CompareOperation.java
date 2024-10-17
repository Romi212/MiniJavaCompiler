package AST.Expressions.BinaryOperations;

import SymbolTable.Types.MemberType;
import utils.Token;

public class CompareOperation extends BinaryExpression{

        public CompareOperation(Token operator) {
            super(operator);
        }


        @Override
        public MemberType getExpressionType() {
            return null;
        }
}
