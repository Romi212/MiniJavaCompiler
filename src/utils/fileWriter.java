package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class fileWriter {

    private static final String OUTPUT_FILE = "output.celvm";
    private static BufferedWriter writer;
    private static File file = createFile();

    // Method to create a new file if it does not exist and initialize BufferedWriter
    public static File createFile() {
        File file = null;
        try {
            file = new File(OUTPUT_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(OUTPUT_FILE, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    // Method to add a sentence to the file
    public static void add(String sentence) {
        try {
            writer.write(sentence);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  static void addHeapRoutines(){
        add("simple_heap_init: \n" +
        "RET 0	; Retorna inmediatamente"+
        "simple_malloc: \n  LOADFP	; Inicialización unidad"+
        "LOADSP \n STOREFP ; Finaliza inicialización del RA "+
        "LOADHL	; hl \n DUP	; hl "+
        "PUSH 1	; 1"+
        "ADD	; hl+1"+
        "STORE 4 ; Guarda el resultado (un puntero a la primer celda de la región de memoria)"+
        "LOAD 3	; Carga la cantidad de celdas a alojar (parámetro que debe ser positivo)"+
        "ADD"+
                "STOREHL ; Mueve el heap limit (hl). Expande el heap"+
        "STOREFP"+
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
        System.out.println("Output file path: " + new File(OUTPUT_FILE).getAbsolutePath());
    }

}