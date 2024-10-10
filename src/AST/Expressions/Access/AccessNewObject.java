package AST.Expressions.Access;

import SymbolTable.Types.MemberType;
import utils.Token;

public class AccessNewObject extends AccessMember{
    public AccessNewObject(Token name){
        super(name);
    }
    @Override
    public boolean isCorrect() {
        return true;
    }

    @Override
    public MemberType getExpressionType() {
        return null;
    }
}
