package AST.Expressions.Access;

import SymbolTable.MemberDeclaration;
import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberObjectType;
import SymbolTable.Types.MemberType;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class AccessNewObject extends AccessMember{
    public AccessNewObject(Token name, MemberObjectType type){
        super();
        setName(name);
        this.type = type;
    }
    @Override
    public boolean isCorrect() throws SemanticalErrorException {
        return SymbolTable.hasClass(type.getToken());
    }

    @Override
    public MemberType getExpressionType() {
        return type;
    }

    @Override
    public void setMember(AccessMember hasMember) throws SemanticalErrorException{

    }
}
