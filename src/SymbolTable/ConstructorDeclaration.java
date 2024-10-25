package SymbolTable;

import SymbolTable.Types.NullType;
import utils.Token;

public class ConstructorDeclaration extends  MethodDeclaration{

        public ConstructorDeclaration(Token name) {
            super(name, new NullType());
        }

        public String toString(){
            return "["+visibility.getLexeme()+" "+ name.getLexeme() + "("+ parameters +") ]";
        }


}
