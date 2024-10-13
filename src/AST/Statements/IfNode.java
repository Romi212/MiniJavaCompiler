package AST.Statements;

import AST.Expressions.ExpressionNode;
import utils.Token;

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
        public boolean isCorrect() {
            return condition.isCorrect() && ifStatement.isCorrect() && elseStatement.isCorrect();
        }

        public String toString(){
            String toReturn = "IfNode(" + condition.toString() + "){ \n " + ifStatement.toString() + "}"  ;
            if( elseStatement != null){
                toReturn += "else{ \n " + elseStatement.toString() + "}";
            }
            return toReturn;

        }
}
