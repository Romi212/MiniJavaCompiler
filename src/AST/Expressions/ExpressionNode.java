package AST.Expressions;

import AST.Statements.StatementNode;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;

public abstract class ExpressionNode extends StatementNode {

        public ExpressionNode(){
            super(null);
        }

        abstract public boolean isCorrect() throws SemanticalErrorException;

        abstract public MemberType getExpressionType();

        public boolean isStatement() {
            return false;
        }

    public boolean isAssignable() {
            return false;
    }
}
