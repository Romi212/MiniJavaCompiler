package SymbolTable.Types;

public class BooleanType extends MemberPrimitiveType{
    private static BooleanType instance = null;
    private BooleanType() {
        name = "rw_bool";
    }

    public static BooleanType getInstance() {
        if (instance == null) {
            instance = new BooleanType();
        }
        return instance;
    }
}
