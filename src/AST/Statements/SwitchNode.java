package AST.Statements;

import AST.Expressions.ExpressionNode;
import utils.Exceptions.CompilerException;

import java.util.ArrayList;

public class SwitchNode extends StatementNode{
    private ExpressionNode expression;
    private ArrayList<CaseNode> cases;
    private CaseNode defaultCase;

    public SwitchNode(){
        super(null);
        this.cases = new ArrayList<>();
    }

    public void setExpression(ExpressionNode expression){
        this.expression = expression;
    }

    public void addCase(CaseNode c){
        cases.add(c);
    }

    public void setDefaultCase(CaseNode c){
        defaultCase = c;
    }

    @Override
    public boolean isCorrect()  throws CompilerException {
        boolean correct = (expression != null) && expression.isCorrect();
        for(CaseNode c : cases){
            correct = correct && c.isCorrect();
        }
        if(defaultCase != null){
            correct = correct && defaultCase.isCorrect();
        }
        return correct;
    }

    public String toString(){
        String result = "SwitchNode(" + expression.toString() + "){ \n";
        for(CaseNode c : cases){
            result += c.toString() + "\n";
        }
        if(defaultCase != null){
            result += defaultCase.toString() + "\n";
        }
        result += "}";
        return result;
    }
}
