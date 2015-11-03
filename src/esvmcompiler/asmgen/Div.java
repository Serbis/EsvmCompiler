package esvmcompiler.asmgen;

import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Div extends Instruction {
    public Div() {}



    public static void gen() {
        count++;
        Files.append(String.valueOf("Div();\n"));
    }

}
