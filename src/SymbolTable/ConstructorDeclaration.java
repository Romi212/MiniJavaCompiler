package SymbolTable;

import utils.Token;

public class ConstructorDeclaration extends  MethodDeclaration{

        public ConstructorDeclaration(Token name) {
            super(name, SymbolTable.decideType(new Token("rw_void", "void", -1)));
        }

        public String toString(){
            return "["+visibility.getLexeme()+" "+ name.getLexeme() + "("+ parameters +") ]";
        }


}
