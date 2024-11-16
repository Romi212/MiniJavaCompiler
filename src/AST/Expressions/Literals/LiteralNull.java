package AST.Expressions.Literals;

import SymbolTable.Types.MemberType;
import SymbolTable.Types.NullType;
import utils.Token;
import utils.fileWriter;

public class LiteralNull extends LiteralValue{

        public LiteralNull(Token value){
            super(value);
        }


        @Override
        public MemberType getExpressionType() {
            return new NullType(value);
        }

        public String toString(){
            return "null";
        }

        public void generate(){
            fileWriter.add("PUSH 0 ; Pushing null");
        }
}
