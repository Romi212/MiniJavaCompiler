package AST.Statements;

import AST.Expressions.ExpressionNode;
import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

public class ReturnNode extends StatementNode{
    private ExpressionNode expression;

    public ReturnNode(Token name, ExpressionNode expression){
        super(name);
        this.expression = expression;

    }

    @Override
    public boolean isCorrect() throws CompilerException {
        MemberType returnType = SymbolTable.getReturnType();

        if (expression != null) {
            expression.setParent(parent);
            if (!expression.isCorrect())
                throw new SemanticalErrorException(expression.getName(), "Return expression is not correct");
            if (!expression.getExpressionType().conformsTo(returnType))
                throw new SemanticalErrorException(expression.getName(), "Return expression does not conform to function return type");

        }else{
            if(!returnType.isVoid() && !returnType.conformsTo((MemberType) null))
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

    public void generate(){
        if(expression != null){
            expression.generate();
        }
        fileWriter.add("STORE 4 ; guardamos el valor de retorno en la variable de retorno");
    }
}
