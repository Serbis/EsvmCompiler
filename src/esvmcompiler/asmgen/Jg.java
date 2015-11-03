package esvmcompiler.asmgen;

import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Jg extends Instruction {
    public Jg() {}



    public static void gen(int pos) {
        count++;
        Files.append(String.valueOf("Jg(" + pos +  ");\n"));
    }

}
