package esvmcompiler.asmgen;

import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Cmp extends Instruction {
    public Cmp() {}



    public static void gen(int first, int second) {
        count++;
        Files.append(String.valueOf("Cmp(" + first + ", " + second +  ");\n"));
    }

}
