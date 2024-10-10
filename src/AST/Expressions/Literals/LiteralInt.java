package AST.Expressions.Literals;

import SymbolTable.Types.IntegerType;
import SymbolTable.Types.MemberType;
import utils.Token;

public class LiteralInt extends LiteralValue{
    public LiteralInt(Token value){
        super(value);
    }

    @Override
    public MemberType getExpressionType() {
        return new IntegerType(new Token("rw_int", "int", -1));
    }
}
