package AST.Expressions;

import SymbolTable.Types.IntegerType;
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
            setName(operator);
        }

        public void addAccess(ExpressionNode access){
            this.access = access;
            access.setParent(parent);
        }

        public void addExpression(ExpressionNode expression){
            this.expression = expression;
            expression.setParent(parent);
        }
        @Override
        public boolean isCorrect() throws SemanticalErrorException {
            if(access == null) throw new SemanticalErrorException(operator,"Assignment expression has no access");
            if(expression == null) throw new SemanticalErrorException(operator,"Assignment expression has no expression");
            if(!access.isCorrect()) throw new SemanticalErrorException(operator,"Assignment expression has incorrect access");
            if(!access.isAssignable()) throw new SemanticalErrorException(operator,"Assignment expression access is not assignable");
            if(!expression.isCorrect()) throw new SemanticalErrorException(operator,"Assignment expression has incorrect expression");
            if(!expression.getExpressionType().conformsTo(access.getExpressionType())) throw new SemanticalErrorException(operator,"Assignment expression has expressions that do not conform to each other");
            if(operator.getLexeme().equals("+=") || operator.getLexeme().equals("-=")){
                if(!expression.getExpressionType().conformsTo(new IntegerType(new Token("rw_int","int",-1)))) throw new SemanticalErrorException(operator,"Assignment expression is not an integer type, cant increment/decrement");
                if(!access.getExpressionType().conformsTo(new IntegerType(new Token("rw_int","int",-1)))) throw new SemanticalErrorException(operator,"Assignment expression access is not an integer type, cannot be incremented/decremented");
            }
            return true;
        }

    @Override
    public MemberType getExpressionType() {
        return expression.getExpressionType();
    }

    public String toString(){
        return access.toString() + " " + operator.getLexeme() + " " + expression.toString() ;
    }

    public boolean isStatement(){
        return true;
    }
}
