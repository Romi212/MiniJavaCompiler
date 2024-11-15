package SymbolTable;

import SymbolTable.Types.NullType;
import SymbolTable.Types.VoidType;
import utils.Token;

public class ConstructorDeclaration extends  MethodDeclaration{

        public ConstructorDeclaration(Token name) {
            super(name, new VoidType(new Token("void", "void", 0)));
            setLabel("");
        }

        public String toString(){
            return "["+visibility.getLexeme()+" "+ name.getLexeme() + "("+ parameters +") ]";
        }




        public void setLabel(String label){
            this.label = "lblCTR"+parameters.size()+"@"+name.getLexeme();
        }

}
