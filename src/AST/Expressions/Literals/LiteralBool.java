package AST.Expressions.Literals;

import SymbolTable.Types.BooleanType;
import SymbolTable.Types.MemberType;
import utils.Token;

public class LiteralBool extends LiteralValue{
    public LiteralBool(Token value){
        super(value);
    }

    @Override
    public MemberType getExpressionType() {
        return new BooleanType(new Token("rw_bool", "boolean", -1));
    }
}
