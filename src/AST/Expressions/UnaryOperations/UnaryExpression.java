package AST.Expressions.UnaryOperations;

import AST.Expressions.ExpressionNode;
import SymbolTable.Types.IntegerType;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

abstract public class UnaryExpression extends ExpressionNode {

        protected ExpressionNode expression;
        protected Token operator;

        public UnaryExpression(Token operator){
            setName(operator);
            this.operator = operator;
        }

        public void addExpression(ExpressionNode expression){
            this.expression = expression;
            expression.setParent(parent);
        }



        @Override
        public MemberType getExpressionType() {
            return expression.getExpressionType();
        }
}
