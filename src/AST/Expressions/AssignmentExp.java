package AST.Expressions;

import SymbolTable.Types.MemberType;
import utils.Token;

public class AssignmentExp extends ExpressionNode{

        private Token operator;
        private ExpressionNode expression;
        private ExpressionNode access;



        public AssignmentExp(Token operator){
            this.operator = operator;
        }

        public void addAccess(ExpressionNode access){
            this.access = access;
        }

        public void addExpression(ExpressionNode expression){
            this.expression = expression;
        }
        @Override
        public boolean isCorrect() {
            return true;
        }

    @Override
    public MemberType getExpressionType() {
        return expression.getExpressionType();
    }

    public String toString(){
        return access.toString() + " " + operator.getLexeme() + " " + expression.toString() ;
    }
}
