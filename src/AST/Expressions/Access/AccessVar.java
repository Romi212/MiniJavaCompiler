package AST.Expressions.Access;

import SymbolTable.SymbolTable;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class AccessVar extends AccessRootMember{
    public AccessVar(Token name){
        super(name);
    }

    public boolean isCorrect() throws SemanticalErrorException{
        if(!SymbolTable.isLocalVar(this.name))
            if(!SymbolTable.isParameter(this.name))
                if(!SymbolTable.isAtribute(this.name))
                    throw new SemanticalErrorException(name, "Variable " + this.name + " not found");
        return true;
    }
}
