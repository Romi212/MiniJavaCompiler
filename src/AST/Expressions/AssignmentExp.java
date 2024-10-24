package AST.Expressions;

import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
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
            access.setParent(this);
        }

        public void addExpression(ExpressionNode expression){
            this.expression = expression;
            expression.setParent(this);
        }
        @Override
        public boolean isCorrect() throws CompilerException {
            return access!= null && expression != null && access.isCorrect() && expression.isCorrect();
        }

    @Override
    public MemberType getExpressionType() {
        return expression.getExpressionType();
    }

    public String toString(){
        return access.toString() + " " + operator.getLexeme() + " " + expression.toString() ;
    }
}
