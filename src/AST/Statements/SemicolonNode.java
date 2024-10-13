package AST.Statements;

public class SemicolonNode extends StatementNode{

    public SemicolonNode(){
        super(null);
    }

    @Override
    public boolean isCorrect() {
        return true;
    }

    public String toString(){
        return "SemicolonNode";
    }
}
