import LexicalAnalyzer.LexicalAnalyzerImp;
import SyntaxAnalyzer.SyntaxAnalyzer;
import SyntaxAnalyzer.SyntaxAnalyzerImp;
import utils.LexicalErrorException;
import utils.SyntaxErrorException;
import utils.sourcemanager.SourceManagerImpl;

public class MainSintactico {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Please provide a file name as an argument.");
            return;
        }

        String fileName = args[0];

        System.out.println(fileName);

        SourceManagerImpl sourceManager = new SourceManagerImpl();
        LexicalAnalyzerImp lexicalAnalyzer = new LexicalAnalyzerImp(sourceManager);
        try {
            sourceManager.open(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        Token token = null;
        boolean error = false;
       do{
           try {
                token = lexicalAnalyzer.nextToken();
                System.out.println(token);

              } catch (LexicalErrorException e) {
                System.out.println(e.getLongMessage());
                System.out.println(e.getMessage());
                error = true;

           }


       }while(token == null || token.getToken() != "EOF");


      if(!error)  System.out.println("[SinErrores]");
        try {
            sourceManager.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerImp(lexicalAnalyzer);
        try {
            System.out.println(syntaxAnalyzer.analyzeSintax());
        } catch (LexicalErrorException e) {
            System.out.println(e.getLongMessage());
        } catch (SyntaxErrorException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getLongMessage());
        }
    }

}