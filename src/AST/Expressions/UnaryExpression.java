package AST.Expressions;

import SymbolTable.Types.MemberType;
import utils.Token;

public class UnaryExpression extends ExpressionNode{

        private ExpressionNode expression;
        private Token operator;

        public UnaryExpression(Token operator){
            this.operator = operator;
        }

        public void addExpression(ExpressionNode expression){
            this.expression = expression;
        }

        @Override
        public boolean isCorrect() {
            return expression.isCorrect();
        }

        @Override
        public MemberType getExpressionType() {
            return expression.getExpressionType();
        }
}
