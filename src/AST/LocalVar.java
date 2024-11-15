package AST;

import SymbolTable.Types.MemberType;
import utils.Token;

public class LocalVar {

    Token name;
    MemberType type;

    int offset;

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

    public void setOffset(int offset){
        this.offset = offset;
    }

    public int getOffset(){
        return offset;
    }
}
