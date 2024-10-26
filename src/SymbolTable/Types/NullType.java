package SymbolTable.Types;

import utils.Token;

public class NullType extends MemberObjectType{

        public NullType(Token name){
            super(name);
        }

        public boolean isCorrect() {
            return true;
        }

        public boolean conformsTo(MemberType type) {
            return (type == null) || !type.isOrdinal() && !type.isVoid();
        }

        public boolean isVoid() {
            return false;
        }

        public boolean isOrdinal() {
            return false;
        }

        public String toString(){
            return "NullType";
        }
}
