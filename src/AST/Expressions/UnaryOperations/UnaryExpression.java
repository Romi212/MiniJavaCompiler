package AST.Expressions.UnaryOperations;

import AST.Expressions.ExpressionNode;
import SymbolTable.Types.MemberType;
import utils.Token;

abstract public class UnaryExpression extends ExpressionNode {

        protected ExpressionNode expression;
        protected Token operator;

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
