package AST.Expressions.Literals;

import SymbolTable.Types.MemberObjectType;
import SymbolTable.Types.MemberType;
import utils.Token;

public class LiteralString extends LiteralValue{
    public LiteralString(Token value){
        super(value);
    }

    @Override
    public MemberType getExpressionType() {
        return new MemberObjectType(new Token("id_class", "String", -1));
    }
}
