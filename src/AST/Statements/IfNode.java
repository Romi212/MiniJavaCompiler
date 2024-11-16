package AST.Statements;

import AST.Expressions.ExpressionNode;
import SymbolTable.SymbolTable;
import SymbolTable.Types.BooleanType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

public class IfNode extends StatementNode{

        private ExpressionNode condition;
        private StatementNode ifStatement;
        private StatementNode elseStatement;

        public IfNode(ExpressionNode condition, StatementNode ifStatement, StatementNode elseStatement, Token name){
            super(name);
            this.condition = condition;
            this.ifStatement = ifStatement;
            this.elseStatement = elseStatement;

        }

        @Override
        public boolean isCorrect() throws CompilerException {
            condition.setParent(this);
            if(!condition.isCorrect()) throw new SemanticalErrorException(condition.getName(), "Condition is not correct");
            ifStatement.setParent(this);
            if(!ifStatement.isCorrect()) throw new SemanticalErrorException(ifStatement.getName(), "If statement is not correct");
            if(elseStatement != null){
                elseStatement.setParent(this);
                if(!elseStatement.isCorrect()) throw new SemanticalErrorException(elseStatement.getName(), "Else statement is not correct");
            }
            if(!condition.getExpressionType().conformsTo("boolean")) throw new SemanticalErrorException(name, "Condition is not boolean");

            return true;
        }

        public void generate(){
            setBreakLabel(parent.getBreakLabel());
            condition.generate();
            fileWriter.add("BF else@" + name.getLexeme()+name.getLine());
            ifStatement.generate();
            if(elseStatement != null){
                fileWriter.add("JUMP end@" + name.getLexeme()+name.getLine());
                fileWriter.add("else@" + name.getLexeme()+name.getLine() + ": NOP");
                elseStatement.generate();
                fileWriter.add("end@" + name.getLexeme() +name.getLine()+ ": NOP");
            }else{
                fileWriter.add("else@" + name.getLexeme()+name.getLine() + ": NOP");
            }
            int localVarSize = getLocalVarSize();
            if (localVarSize > 0) {
                fileWriter.add("RMEM " + localVarSize);
                SymbolTable.removeLocalVar(localVarSize);
            }


        }

        public String toString(){
            String toReturn = "IfNode(" + condition.toString() + "){ \n " + ifStatement.toString() + "}"  ;
            if( elseStatement != null){
                toReturn += "else{ \n " + elseStatement.toString() + "}";
            }
            return toReturn;

        }

}
