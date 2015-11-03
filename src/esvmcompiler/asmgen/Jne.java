package esvmcompiler.asmgen;

import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Jne extends Instruction {
    public Jne() {}



    public static void gen(int pos) {
        count++;
        Files.append(String.valueOf("Jne(" + pos +  ");\n"));
    }

}
