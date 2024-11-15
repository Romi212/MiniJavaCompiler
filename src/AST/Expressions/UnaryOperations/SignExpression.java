package AST.Expressions.UnaryOperations;

import SymbolTable.Types.IntegerType;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

public class SignExpression extends UnaryExpression{

    public SignExpression(Token operator) {
        super(operator);
    }


    @Override
    public boolean isCorrect() throws SemanticalErrorException {
        if(expression == null) throw new SemanticalErrorException(operator,"Unary expression has no expression");
        expression.setParent(parent);
        if(!expression.isCorrect()) throw new SemanticalErrorException(operator,"Unary expression has incorrect expression");
        if(!expression.getExpressionType().conformsTo(new IntegerType(new Token("rw_int","int",-1)))) throw new SemanticalErrorException(operator,"Unary sign expression has expression that does not conform to int");
        return true;
    }

    public void generate(){
        expression.generate();
        //if(operator.getLexeme().equals("+")){
        //    fileWriter.add("PUSHI 1");
        //    fileWriter.add("MUL");
        //}else
        if(operator.getLexeme().equals("-")){
            fileWriter.add("NEG");

        }
    }
    public String toString(){
        return operator.getLexeme() + " " + expression.toString();
    }
}
