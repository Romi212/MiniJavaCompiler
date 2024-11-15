package AST.Expressions.Literals;

import SymbolTable.Types.BooleanType;
import SymbolTable.Types.MemberType;
import utils.Token;
import utils.fileWriter;

public class LiteralBool extends LiteralValue{
    public LiteralBool(Token value){
        super(value);
    }

    @Override
    public MemberType getExpressionType() {
        return new BooleanType(new Token("rw_bool", "boolean", -1));
    }

    public void generate(){
        if(this.name.getLexeme().equals("true")){
            fileWriter.add("PUSH 1 ; Pushing true");
        }else{
            fileWriter.add("PUSH 0 ; Pushing false");
        }
    }
}
