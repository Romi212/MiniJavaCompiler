import LexicalAnalyzer.LexicalAnalyzerImp;
import SymbolTable.SymbolTable;
import SyntaxAnalyzer.SyntaxAnalyzer;
import SyntaxAnalyzer.SyntaxAnalyzerImp;
import utils.Exceptions.CompilerException;
import utils.fileWriter;
import utils.sourcemanager.SourceManagerImpl;

public class MainSemantico2 {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Please provide a file name as an argument.");
            return;
        }

        String fileName = args[0];
       // String outputName = args[1];

        //System.out.println(fileName +" output: "+ outputName);

        //fileWriter.createFile(outputName);
       // fileWriter.add("HOLA");
        SourceManagerImpl sourceManager = new SourceManagerImpl();
        LexicalAnalyzerImp lexicalAnalyzer = new LexicalAnalyzerImp(sourceManager);
        try {
            sourceManager.open(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SymbolTable.createTable();
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerImp(lexicalAnalyzer);

        try {
            syntaxAnalyzer.analyzeSintax(4);

        } catch (CompilerException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getLongMessage());
        }

        fileWriter.closeWriter();
        //Muestra una string con la tabla de simbolos
        //System.out.println(SymbolTable.showString());
    }

}