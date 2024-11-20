package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class fileWriter {

    private static String OUTPUT_FILE ;
    private static BufferedWriter writer;
    private static File file ;

    // Method to create a new file if it does not exist and initialize BufferedWriter
    public static void createFile(String name) {
        File file2 = null;
        OUTPUT_FILE = name;
        try {
            file2 = new File(OUTPUT_FILE);
            if (!file2.exists()) {
                file2.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(OUTPUT_FILE, false));
            add(".CODE\n" +
                    "PUSH simple_heap_init\n CALL");
        } catch (IOException e) {
            e.printStackTrace();
        }
        file = file2;
    }

    // Method to add a sentence to the file
    public static void add(String sentence) {
        try {
            if(sentence.equals(".DATA") || sentence.equals(".CODE")) writer.newLine();
            writer.write(sentence);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  static void addHeapRoutines(){
        add("simple_heap_init: RET 0 ; Retorna inmediatamente\n"+
        "simple_malloc: LOADFP	; Inicialización unidad\n"+
        "LOADSP \nSTOREFP ; Finaliza inicialización del RA \n"+
        "LOADHL	; hl \nDUP	; hl \n"+
        "PUSH 1	; 1\n"+
        "ADD	; hl+1\n"+
        "STORE 4 ; Guarda el resultado (un puntero a la primer celda de la región de memoria)\n"+
        "LOAD 3	; Carga la cantidad de celdas a alojar (parámetro que debe ser positivo)\n"+
        "ADD\n"+
                "STOREHL ; Mueve el heap limit (hl). Expande el heap\n"+
        "STOREFP\n"+
        "RET 1	; Retorna eliminando el parámetro");

    }


    // Method to close the BufferedWriter
    public static void closeWriter() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printFilePath() {
       // System.out.println("Output file path: " + new File(OUTPUT_FILE).getAbsolutePath());
    }

}