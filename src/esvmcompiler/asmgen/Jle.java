package esvmcompiler.asmgen;

import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Jle extends Instruction {
    public Jle() {}



    public static void gen(int pos) {
        count++;
        Files.append(String.valueOf("Jle(" + pos +  ");\n"));
    }

}
