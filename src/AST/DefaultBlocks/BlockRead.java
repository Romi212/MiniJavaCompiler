package AST.DefaultBlocks;

import AST.BLockDeclaration;
import utils.Token;
import utils.fileWriter;

public class BlockRead extends BLockDeclaration {
    public BlockRead(Token name) {
        super(new Token("READ", "READ",-1));
    }

    public void generate(){
        fileWriter.add("READ ; leemos algo");
        fileWriter.add("STORE 3 ; guardamos para devolver");
    }
}
