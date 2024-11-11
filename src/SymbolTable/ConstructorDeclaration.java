package SymbolTable;

import SymbolTable.Types.NullType;
import utils.Token;

public class ConstructorDeclaration extends  MethodDeclaration{

        public ConstructorDeclaration(Token name) {
            super(name, new NullType(new Token("null", "null", 0)));
        }

        public String toString(){
            return "["+visibility.getLexeme()+" "+ name.getLexeme() + "("+ parameters +") ]";
        }

        public void generate() {
            //TODO
        }

}
