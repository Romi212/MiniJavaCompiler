package AST.Expressions.Literals;

import SymbolTable.Types.CharacterType;
import SymbolTable.Types.MemberType;
import utils.Token;
import utils.fileWriter;

public class LiteralChar extends LiteralValue{
    public LiteralChar(Token value){
        super(value);
    }

    @Override
    public MemberType getExpressionType() {
        return new CharacterType(new Token("rw_char", "char", -1));
    }


    public void generate(){
        fileWriter.add("PUSH "+(int)this.name.getLexeme().charAt(1)+" ; Pushing char");
    }

}
