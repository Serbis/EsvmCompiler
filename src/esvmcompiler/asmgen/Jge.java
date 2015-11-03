package esvmcompiler.asmgen;

import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Jge extends Instruction {
    public Jge() {}



    public static void gen(int pos) {
        count++;
        Files.append(String.valueOf("Jge(" + pos +  ");\n"));
    }

}
