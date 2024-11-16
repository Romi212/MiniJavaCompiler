package AST.Expressions.Access;

import SymbolTable.MemberDeclaration;
import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberObjectType;
import SymbolTable.Types.MemberType;
import utils.Token;
import utils.fileWriter;

public class AccessThis extends AccessMember{

    public AccessThis(Token name){
        super(name);
        this.type = new MemberObjectType(SymbolTable.getCurrentClass().getName());
    }
    @Override
    public boolean isCorrect() {
        return true;
    }

    @Override
    public MemberType getExpressionType() {
        return type;
    }

    @Override
    public void setMember(AccessMember hasMember) {

    }

    public void generate(){
        fileWriter.add("LOAD 3");
    }
}
