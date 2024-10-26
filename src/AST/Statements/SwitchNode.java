package AST.Statements;

import AST.Expressions.ExpressionNode;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;

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
        expression.setParent(this);
    }

    public void addCase(CaseNode c){
        if(c.getExpressionType() == null) defaultCase = c;
        else cases.add(c);
        c.setParent(this);
    }

    public void setDefaultCase(CaseNode c){
        defaultCase = c;
        c.setParent(this);
    }

    @Override
    public boolean isCorrect()  throws CompilerException {
        if(expression == null) throw new SemanticalErrorException(name,"Switch expression is null");
        if(!expression.isCorrect()) throw new SemanticalErrorException(expression.getName(),"Switch expression is not correct");
        if(!expression.isAssignable()) throw new SemanticalErrorException(expression.getName(),"Switch expression is not a variable");
        MemberType expressionType = expression.getExpressionType();
        if(!expressionType.isOrdinal()) throw new SemanticalErrorException(expression.getName(),"Switch expression is not ordinal");
        boolean correct = true;
        for(CaseNode c : cases){
            correct = correct && c.isCorrect() && c.getExpressionType().conformsTo(expressionType);
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
