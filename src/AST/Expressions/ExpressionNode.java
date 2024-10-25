package AST.Expressions;

import AST.Statements.StatementNode;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;

public abstract class ExpressionNode extends StatementNode {

        public ExpressionNode(){
            super(null);
        }

        abstract public boolean isCorrect() throws CompilerException;

        abstract public MemberType getExpressionType();

        public boolean isStatement() {
            return false;
        }

    public boolean isAssignable() {
            return false;
    }
}
