package AST.Expressions.Literals;

import SymbolTable.Types.CharacterType;
import SymbolTable.Types.MemberType;
import utils.Token;

public class LiteralChar extends LiteralValue{
    public LiteralChar(Token value){
        super(value);
    }

    @Override
    public MemberType getExpressionType() {
        return new CharacterType(new Token("rw_char", "char", -1));
    }


}
