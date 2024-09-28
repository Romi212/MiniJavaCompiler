package SymbolTable.Types;

public class CharacterType extends MemberPrimitiveType{
    private static CharacterType instance = null;
    private CharacterType() {
        name = "rw_char";
    }

    public static CharacterType getInstance() {
        if (instance == null) {
            instance = new CharacterType();
        }
        return instance;
    }
}
