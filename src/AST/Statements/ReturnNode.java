package AST.Statements;

import AST.Expressions.ExpressionNode;
import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class ReturnNode extends StatementNode{
    private ExpressionNode expression;

    public ReturnNode(Token name, ExpressionNode expression){
        super(name);
        this.expression = expression;
        if(expression != null){
            expression.setParent(this);
        }
    }

    @Override
    public boolean isCorrect() throws CompilerException {
        MemberType returnType = SymbolTable.getReturnType();
        if(returnType.conformsTo(null))
            throw new SemanticalErrorException(this.getName(), "Invalid return statement in constructor function");
        if (expression != null) {
            if (!expression.isCorrect())
                throw new SemanticalErrorException(expression.getName(), "Return expression is not correct");
            if (!expression.getExpressionType().conformsTo(returnType))
                throw new SemanticalErrorException(expression.getName(), "Return expression does not conform to function return type");

        }else{
            if(!returnType.isVoid())
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
