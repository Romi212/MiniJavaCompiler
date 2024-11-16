package AST.Expressions.Literals;

import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberObjectType;
import SymbolTable.Types.MemberType;
import utils.Token;
import utils.fileWriter;

public class LiteralString extends LiteralValue{
    public LiteralString(Token value){
        super(value);
    }

    @Override
    public MemberType getExpressionType() {
        return new MemberObjectType(new Token("id_class", "String", -1));
    }

    public void generate(){
        fileWriter.add("string@"+SymbolTable.getIndex()+": DW \""+value.getLexeme()+"\", 0");
    }
}
