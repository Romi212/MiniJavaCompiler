package AST.Expressions;

import AST.Statements.StatementNode;
import SymbolTable.Types.MemberType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.SemanticalErrorException;
import utils.Token;

public abstract class ExpressionNode extends StatementNode {

        public ExpressionNode(Token name){
            super(name);
        }

        abstract public boolean isCorrect() throws SemanticalErrorException;

        abstract public MemberType getExpressionType();

        public boolean isStatement() {
            return false;
        }

    public boolean isAssignable() {
            return false;
    }

    public boolean isStatic() {
            return false;
    }
}
