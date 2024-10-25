package SymbolTable.Types;

public class NullType extends MemberObjectType{

        public NullType(){
            super(null);
        }

        public boolean isCorrect() {
            return true;
        }

        public boolean conformsTo(MemberType type) {
            return type == null;
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
