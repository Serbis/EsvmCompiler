package esvmcompiler.asmgen;

import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Jl extends Instruction {
    public Jl() {}



    public static void gen(int pos) {
        count++;
        Files.append(String.valueOf("Jl(" + pos +  ");\n"));
    }

}
