package AST.Statements;

import utils.Exceptions.SemanticalErrorException;
import utils.Token;
import utils.fileWriter;

public class BreakStatement extends StatementNode{
    public BreakStatement(Token name){
        super(name);
    }

    public boolean isCorrect() throws SemanticalErrorException {
        if (!parent.isBreakable()) throw new SemanticalErrorException(name, "Break statement outside of loop or switch");
        return true;
    }

    public String toString(){
        return "break";
    }

    public boolean isStatement(){
        return true;
    }

    public void generate(){
        fileWriter.add("JUMP " + parent.getBreakLabel());
    }
}
