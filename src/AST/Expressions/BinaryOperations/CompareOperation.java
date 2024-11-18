package AST.Expressions.BinaryOperations;

import SymbolTable.Types.BooleanType;
import SymbolTable.Types.IntegerType;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

public class CompareOperation extends BinaryExpression{

        public CompareOperation(Token operator) {
            super(operator);
        }

        @Override
        public boolean isCorrect() throws SemanticalErrorException {
            if(left == null) throw new SemanticalErrorException(operator,"Binary expression has no left expression");
            if(right == null) throw new SemanticalErrorException(operator,"Binary expression has no right expression");
            left.setParent(parent);
            right.setParent(parent);
            if(!left.isCorrect()) throw new SemanticalErrorException(operator,"Binary expression has incorrect left expression");
            if(!right.isCorrect()) throw new SemanticalErrorException(operator,"Binary expression has incorrect right expression");
            if(!left.getExpressionType().conformsTo("int")) throw new SemanticalErrorException(operator,"Binary compare expression has left expression that does not conform to int");
            if(!right.getExpressionType().conformsTo("int")) throw new SemanticalErrorException(operator,"Binary compare expression has right expression that does not conform to int");
            return true;
        }
        @Override
        public MemberType getExpressionType() {
            return new BooleanType(new Token("rw_boolean","boolean",-1));
        }

        public void generate(){
            left.generate();
            right.generate();
            if(operator.getLexeme().equals("<")){
                fileWriter.add("LT");
            }else if(operator.getLexeme().equals(">")){
                fileWriter.add("GT");
            }else if(operator.getLexeme().equals("<=")){
                fileWriter.add("LE");
            }else if(operator.getLexeme().equals(">=")){
                fileWriter.add("GE");
            }
        }

        public boolean precedes(Token operator){
            return !(operator.getLexeme()=="+" || operator.getLexeme()=="-" || operator.getLexeme()=="*" || operator.getLexeme()=="/" || operator.getLexeme()=="%");
        }
}
