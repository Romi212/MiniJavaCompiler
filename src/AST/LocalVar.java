package AST;

import SymbolTable.Types.MemberType;
import utils.Token;

public class LocalVar {

    Token name;
    MemberType type;

    public LocalVar(Token name, MemberType type){
        this.name = name;
        this.type = type;
    }

    public Token getName(){
        return name;
    }

    public MemberType getType(){
        return type;
    }
}
