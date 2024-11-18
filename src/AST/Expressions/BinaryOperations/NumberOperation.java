package AST.Expressions.BinaryOperations;

import AST.Expressions.ExpressionNode;
import SymbolTable.Types.IntegerType;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

public class NumberOperation extends BinaryExpression{

        public NumberOperation(Token operator) {
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
            if(!left.getExpressionType().conformsTo("int")) throw new SemanticalErrorException(operator,"Binary int expression has left expression that does not conform to int");
            if(!right.getExpressionType().conformsTo("int")) throw new SemanticalErrorException(operator,"Binary int expression has right expression that does not conform to int");
            return true;
        }
        @Override
        public MemberType getExpressionType() {
            return new IntegerType(new Token("rw_int","int",-1));
        }

        public void generate(){
            left.generate();
            right.generate();
            if(operator.getLexeme().equals("+")){
                fileWriter.add("ADD");
            }else if(operator.getLexeme().equals("-")){
                fileWriter.add("SUB");
            }else if(operator.getLexeme().equals("*")){
                fileWriter.add("MUL");
            }else if(operator.getLexeme().equals("/")){
                fileWriter.add("DIV");
            }else if(operator.getLexeme().equals("%")){
                fileWriter.add("MOD");
            }
        }



        public boolean precedes(Token operator){

            if (operator.getLexeme().equals("*") || operator.getLexeme().equals("/") || operator.getLexeme().equals("%")) return false;
            if((this.operator.getLexeme().equals("+") || this.operator.getLexeme().equals("-"))&& (operator.getLexeme().equals("+")|| operator.getLexeme().equals("-"))) return false;
            return true;
        }

}
