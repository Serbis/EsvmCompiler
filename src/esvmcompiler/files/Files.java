package esvmcompiler.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by serbis on 27.10.15.
 */
public class Files {
    private static FileOutputStream fos;

    public Files() {}

    public static void setOutFile(File file) {
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void append(String s) {
        try {
            fos.write(s.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void append(byte[] bytes) {
        try {
            fos.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeFile() {
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
