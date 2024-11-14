package SymbolTable;

import SymbolTable.Types.NullType;
import utils.Token;

public class ConstructorDeclaration extends  MethodDeclaration{

        public ConstructorDeclaration(Token name) {
            super(name, new NullType(new Token("null", "null", 0)));
            setLabel("");
        }

        public String toString(){
            return "["+visibility.getLexeme()+" "+ name.getLexeme() + "("+ parameters +") ]";
        }



        public void setLabel(String label){
            this.label = "lblCTR"+parameters.size()+"@"+name.getLexeme();
        }

}
