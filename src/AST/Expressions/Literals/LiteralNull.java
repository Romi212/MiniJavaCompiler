package AST.Expressions.Literals;

import SymbolTable.Types.MemberType;
import SymbolTable.Types.NullType;
import utils.Token;

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
}
