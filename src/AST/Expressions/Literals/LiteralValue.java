package AST.Expressions.Literals;

import AST.Expressions.ExpressionNode;
import SymbolTable.Types.MemberType;
import utils.Token;
import utils.fileWriter;

public abstract class LiteralValue extends ExpressionNode {

    protected Token value;

    public LiteralValue(Token value){
        super(value);
        this.value = value;

    }

    @Override
    public boolean isCorrect() {
        return true;
    }

    public abstract MemberType getExpressionType();

    public String toString(){
        return value.getLexeme();
    }

    public void generate(){
        fileWriter.add("PUSH "+value.getLexeme()+" ; Literal Value");
    }
}
