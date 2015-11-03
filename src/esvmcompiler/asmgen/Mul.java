package esvmcompiler.asmgen;

import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Mul extends Instruction {
    public Mul() {}



    public static void gen() {
        count++;
        Files.append(String.valueOf("Mil();\n"));
    }

}
