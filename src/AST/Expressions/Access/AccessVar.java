package AST.Expressions.Access;

import SymbolTable.SymbolTable;
import SymbolTable.MemberDeclaration;
import SymbolTable.Types.MemberType;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class AccessVar extends AccessMember{



    public boolean isCorrect() throws SemanticalErrorException{
        MemberType type = SymbolTable.visibleVar(this.name);
        this.type = type;
        return true;
    }

    public MemberType getExpressionType(){
        return this.type;
    }

    @Override
    public void setMember(MemberDeclaration hasMember) {
        this.type = hasMember.getType();
    }


}
