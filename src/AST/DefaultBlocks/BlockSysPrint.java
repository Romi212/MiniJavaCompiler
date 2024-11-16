package AST.DefaultBlocks;

import AST.BLockDeclaration;
import utils.fileWriter;

public class BlockSysPrint extends BLockDeclaration{

    private boolean enter;
    private String type;


    public BlockSysPrint(String type, boolean enter){
        super(null);
        this.enter = enter;
        this.type = type;
    }

    public void generate(){

        switch (type) {
            case "int":
                fileWriter.add("LOAD 3 ; carga el parametro");
                fileWriter.add("IPRINT ; imprime el entero");
                break;
            case "bool":
                fileWriter.add("LOAD 3 ; carga el parametro");
                fileWriter.add("BPRINT ; imprime el booleano");
                break;
            case "string":
                fileWriter.add("LOAD 3 ; carga el parametro");
                fileWriter.add("SPRINT ; imprime el string");
                break;
            case "char":
                fileWriter.add("LOAD 3 ; carga el parametro");
                fileWriter.add("CPRINT ; imprime el caracter");
                break;
            default:
                break;
        }

        if(enter){
            fileWriter.add("PRNLN ; imprime el salto de linea");
        }
    }
}
