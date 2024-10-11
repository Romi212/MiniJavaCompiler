package AST.Expressions;

import AST.Statements.StatementNode;
import SymbolTable.Types.MemberType;

public abstract class ExpressionNode extends StatementNode {

        public ExpressionNode(){
            super(null);
        }

        abstract public boolean isCorrect();

        abstract MemberType getExpressionType();

}
