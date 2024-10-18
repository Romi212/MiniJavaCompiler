package AST.Expressions.Access;

import SymbolTable.MemberDeclaration;
import SymbolTable.Types.MemberType;
import utils.Token;

public class AccessNewObject extends AccessMember{
    public AccessNewObject(Token name){
        super();
        setName(name);
    }
    @Override
    public boolean isCorrect() {
        return true;
    }

    @Override
    public MemberType getExpressionType() {
        return null;
    }

    @Override
    public void setMember(MemberDeclaration hasMember) {

    }
}
