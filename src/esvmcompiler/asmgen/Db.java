package esvmcompiler.asmgen;

import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Db extends Instruction {
    public Db() {}



    public static void gen(int number, int size, int type) {
        count++;
        Files.append(String.valueOf("Db(" + number + ", " + size + ", " + type + ");\n"));
    }

}
