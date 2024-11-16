package AST.Statements;

import AST.Expressions.ExpressionNode;
import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

import java.util.ArrayList;

public class SwitchNode extends StatementNode{
    private ExpressionNode expression;
    private ArrayList<CaseNode> cases;
    private CaseNode defaultCase;

    public SwitchNode(Token name){
        super(name);
        this.cases = new ArrayList<>();
    }

    public void setExpression(ExpressionNode expression){
        this.expression = expression;

    }

    public void addCase(CaseNode c){
        if(c.getExpressionType() == null) defaultCase = c;
        else cases.addFirst(c);

    }


    @Override
    public boolean isCorrect()  throws CompilerException {
        if(expression == null) throw new SemanticalErrorException(name,"Switch expression is null");
        expression.setParent(this);
        if(!expression.isCorrect()) throw new SemanticalErrorException(expression.getName(),"Switch expression is not correct");
        MemberType expressionType = expression.getExpressionType();
        if(!expressionType.isOrdinal()) throw new SemanticalErrorException(expression.getName(),"Switch expression is not ordinal");
        boolean correct = true;
        for(CaseNode c : cases){

            c.setParent(this);
            if(!c.isCorrect()) throw new SemanticalErrorException(c.getName(),"Case is not correct");
            if(!c.getExpressionType().conformsTo(expressionType)) throw  new SemanticalErrorException(c.getName(),"Case expression does not conform to switch expression");
        }
        if(defaultCase != null){
            defaultCase.setParent(this);
            if( ! defaultCase.isCorrect()) throw new SemanticalErrorException(defaultCase.getName(),"Default case is not correct");
        }
        SymbolTable.removeLocalVar(getLocalVarSize());
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

    public boolean isBreakable(){
        return true;
    }

    public void generate(){
        String end = getEndLabel();
        expression.generate();
        for(CaseNode c : cases){
            c.setBreakLabel(end);
            c.setLabel("case" + cases.indexOf(c) + name.getLine());
            c.generateExpression();
        }

        if(defaultCase != null){
            defaultCase.generateExpression();
        }

        for (CaseNode c : cases){
            c.generateStatement();
        }
        if(defaultCase != null){
            defaultCase.generateStatement();
        }

        int localVarSize = getLocalVarSize();


        fileWriter.add(end+": FMEM " + (localVarSize+1));



    }
}
