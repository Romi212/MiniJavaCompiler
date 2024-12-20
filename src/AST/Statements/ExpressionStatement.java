package AST.Statements;

import AST.Expressions.ExpressionNode;
import SymbolTable.SymbolTable;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.fileWriter;

public class ExpressionStatement extends StatementNode{


    private ExpressionNode expression;

    public ExpressionStatement(ExpressionNode expression) {
        super(null);
        if(expression!= null) setName(expression.getName());
        this.expression = expression;
    }


    public void setExpression(ExpressionNode expression){
        this.expression = expression;
        this.name = expression.getName();
    }

    @Override
    public boolean isCorrect() throws CompilerException {
        if(expression == null) throw new SemanticalErrorException(name,"Expression statement has no expression");
        expression.setParent(parent);
        SymbolTable.setStar(false);
        if(expression.isCorrect()){

            if(!expression.isStatement()) {
                throw new SemanticalErrorException(expression.getName(), "Expression is not a statement");
            }
        }
        SymbolTable.setStar(true);
        return true;
    }

    public String toString(){
        return expression.toString();
    }

    public void generate(){
        expression.generate();
        if(!expression.getExpressionType().isVoid()) fileWriter.add("POP");
    }
}
