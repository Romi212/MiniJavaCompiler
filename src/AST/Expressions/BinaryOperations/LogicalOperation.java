package AST.Expressions.BinaryOperations;

import SymbolTable.Types.BooleanType;
import SymbolTable.Types.IntegerType;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class LogicalOperation extends BinaryExpression {


    public LogicalOperation(Token operator) {
        super(operator);
    }

    @Override
    public boolean isCorrect() throws SemanticalErrorException {
        if(left == null) throw new SemanticalErrorException(operator,"Binary expression has no left expression");
        if(right == null) throw new SemanticalErrorException(operator,"Binary expression has no right expression");
        if(!left.isCorrect()) throw new SemanticalErrorException(operator,"Binary expression has incorrect left expression");
        if(!right.isCorrect()) throw new SemanticalErrorException(operator,"Binary expression has incorrect right expression");
        if(!left.getExpressionType().conformsTo(new IntegerType(new Token("rw_boolean","boolean",-1)))) throw new SemanticalErrorException(operator,"Binary expression has left expression that does not conform to int");
        if(!right.getExpressionType().conformsTo(new IntegerType(new Token("rw_boolean","boolean",-1)))) throw new SemanticalErrorException(operator,"Binary expression has right expression that does not conform to int");
        return true;
    }
    @Override
    public MemberType getExpressionType() {
        return new BooleanType(new Token("rw_boolean","boolean",-1));
    }


}
