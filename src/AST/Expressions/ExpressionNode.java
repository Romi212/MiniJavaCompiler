package AST.Expressions;

import SymbolTable.Types.MemberType;

public abstract class ExpressionNode {

        abstract public boolean isCorrect();

        abstract MemberType getExpressionType();

}
