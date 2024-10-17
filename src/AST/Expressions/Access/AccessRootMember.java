package AST.Expressions.Access;

import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Token;

public class AccessRootMember extends AccessMember{
    public AccessRootMember(Token name){
        super(name);
    }


    @Override
    public boolean isCorrect() throws CompilerException {
        return true;
    }

    @Override
    public MemberType getExpressionType() {
        return null;
    }
}
