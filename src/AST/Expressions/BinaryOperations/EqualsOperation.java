package AST.Expressions.BinaryOperations;

import SymbolTable.Types.BooleanType;
import SymbolTable.Types.IntegerType;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class EqualsOperation extends BinaryExpression{

    public EqualsOperation(Token operator) {
        super(operator);
    }

    @Override
    public boolean isCorrect() throws SemanticalErrorException {
        if(left == null) throw new SemanticalErrorException(operator,"Binary expression has no left expression");
        if(right == null) throw new SemanticalErrorException(operator,"Binary expression has no right expression");
        left.setParent(parent);
        right.setParent(parent);
        if(!left.isCorrect()) throw new SemanticalErrorException(operator,"Binary equals expression has incorrect left expression");
        if(!right.isCorrect()) throw new SemanticalErrorException(operator,"Binary equals expression has incorrect right expression");
        if(!left.getExpressionType().conformsTo(right.getExpressionType()) && !right.getExpressionType().conformsTo(left.getExpressionType())) throw new SemanticalErrorException(operator,"Binary equals expression has expressions that do not conform to each other");
        return true;
    }
    @Override
    public MemberType getExpressionType() {
        return new BooleanType(new Token("rw_boolean","boolean",-1));
    }
}
