package AST.Expressions.Literals;

import SymbolTable.Types.MemberType;

public class LiteralNull extends LiteralValue{

        public LiteralNull(){
            super(null);
        }


        @Override
        public MemberType getExpressionType() {
            return null;
        }
}
