import LexicalAnalyzer.LexicalAnalyzerImp;
import utils.LexicalErrorException;
import utils.Token;
import utils.sourcemanager.SourceManagerImpl;

public class Main {
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
        }
    }
}