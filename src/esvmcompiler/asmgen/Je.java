package esvmcompiler.asmgen;

import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Je extends Instruction {
    public Je() {}



    public static void gen(int pos) {
        count++;
        Files.append(String.valueOf("Je(" + pos +  ");\n"));
    }

}
