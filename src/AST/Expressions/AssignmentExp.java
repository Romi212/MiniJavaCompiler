package AST.Expressions;

import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
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
            if(access == null) throw new SemanticalErrorException(operator,"Assignment expression has no access");
            if(expression == null) throw new SemanticalErrorException(operator,"Assignment expression has no expression");
            if(!access.isCorrect()) throw new SemanticalErrorException(operator,"Assignment expression has incorrect access");
            if(!expression.isCorrect()) throw new SemanticalErrorException(operator,"Assignment expression has incorrect expression");
            if(!expression.getExpressionType().conformsTo(access.getExpressionType())) throw new SemanticalErrorException(operator,"Assignment expression has expressions that do not conform to each other");
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
