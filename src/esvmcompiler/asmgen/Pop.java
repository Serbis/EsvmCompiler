package esvmcompiler.asmgen;

import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Pop extends Instruction {
    public Pop() {}



    public static void gen(int number) {
        count++;
        Files.append(String.valueOf("Pop(" + number +  ");\n"));
    }

}
