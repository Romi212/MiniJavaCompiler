package utils.Exceptions;

import utils.Token;

public class SemanticalErrorException extends CompilerException{

        private String error;
        private Token found;

        public SemanticalErrorException(Token found, String error){
            super("[Error:"+found.getLexeme()+"|"+found.getLine()+"]");
            this.found = found;
            this.error = error;
        }

        public String getLongMessage(){
            return "Semantic error: "+ error+ " in line "+ found.getLine()+": "+found.getLexeme();
        }
}
