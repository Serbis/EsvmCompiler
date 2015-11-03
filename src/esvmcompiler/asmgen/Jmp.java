package esvmcompiler.asmgen;

import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Jmp extends Instruction {
    public Jmp() {}



    public static void gen(int pos) {
        count++;
        Files.append(String.valueOf("Jmp(" + pos +  ");\n"));
    }

}
