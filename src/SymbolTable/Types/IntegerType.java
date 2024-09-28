package SymbolTable.Types;

public class IntegerType extends MemberPrimitiveType {;
    private static IntegerType instance = null;
    private IntegerType() {
        name = "rw_int";
    }

    public static IntegerType getInstance() {
        if (instance == null) {
            instance = new IntegerType();
        }
        return instance;
    }
}
