package AST.Expressions.Literals;

import AST.Expressions.ExpressionNode;
import SymbolTable.Types.MemberType;
import utils.Token;

public abstract class LiteralValue extends ExpressionNode {

    protected Token value;

    public LiteralValue(Token value){
        this.value = value;
    }

    @Override
    public boolean isCorrect() {
        return true;
    }

    public abstract MemberType getExpressionType();
}
