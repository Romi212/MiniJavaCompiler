package AST.Statements;

import AST.Expressions.ExpressionNode;
import SymbolTable.SymbolTable;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;

public class ReturnNode extends StatementNode{
    private ExpressionNode expression;

    public ReturnNode(ExpressionNode expression){
        super(null);
        this.expression = expression;
        if(expression != null){
            expression.setParent(this);
        }
    }

    @Override
    public boolean isCorrect() throws CompilerException {
        if (expression != null) {
            if (!expression.isCorrect())
                throw new SemanticalErrorException(expression.getName(), "Return expression is not correct");
            if (!expression.getExpressionType().conformsTo(SymbolTable.getReturnType()))
                throw new SemanticalErrorException(expression.getName(), "Return expression does not conform to function return type");

        }else{
            if(!SymbolTable.getReturnType().isVoid())
                throw new SemanticalErrorException(this.getName(), "Method is not void, missing return expression");
        }
        return true;
    }
    public String toString(){
        if(expression == null){
            return "ReturnNode()";
        }
        return "ReturnNode(" + expression.toString() + ")";
    }
}
