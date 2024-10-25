package AST.Statements;

import AST.Expressions.ExpressionNode;
import SymbolTable.Types.BooleanType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public class IfNode extends StatementNode{

        private ExpressionNode condition;
        private StatementNode ifStatement;
        private StatementNode elseStatement;

        public IfNode(ExpressionNode condition, StatementNode ifStatement, StatementNode elseStatement, Token name){
            super(name);
            this.condition = condition;
            condition.setParent(this);
            this.ifStatement = ifStatement;
            ifStatement.setParent(this);
            this.elseStatement = elseStatement;
            if(elseStatement != null){
                elseStatement.setParent(this);
            }
        }

        @Override
        public boolean isCorrect() throws CompilerException {
            if(!condition.isCorrect()) throw new SemanticalErrorException(condition.getName(), "Condition is not correct");
            if(!ifStatement.isCorrect()) throw new SemanticalErrorException(ifStatement.getName(), "If statement is not correct");
            if(elseStatement != null && !elseStatement.isCorrect()) throw new SemanticalErrorException(elseStatement.getName(), "Else statement is not correct");
            if(!condition.getExpressionType().conformsTo(new BooleanType(new Token("rw_boolean","boolean",-1)))) throw new SemanticalErrorException(name, "Condition is not boolean");
            return true;
        }

        public String toString(){
            String toReturn = "IfNode(" + condition.toString() + "){ \n " + ifStatement.toString() + "}"  ;
            if( elseStatement != null){
                toReturn += "else{ \n " + elseStatement.toString() + "}";
            }
            return toReturn;

        }
}
