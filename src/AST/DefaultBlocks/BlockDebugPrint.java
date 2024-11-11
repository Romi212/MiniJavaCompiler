package AST.DefaultBlocks;

import AST.BLockDeclaration;
import utils.fileWriter;

public class BlockDebugPrint extends BLockDeclaration{

    public BlockDebugPrint(){
        super(null);
    }

    public void generate(){
        fileWriter.add("LOAD 3 ; carga el parametro");
        fileWriter.add("IPRINT ; imprime el entero");
    }
}
