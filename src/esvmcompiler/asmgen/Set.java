package esvmcompiler.asmgen;

import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Set extends Instruction {
    public Set() {}



    public static void gen(int number, int size, String hex) {
        count++;
        Files.append(String.valueOf("Set(" + number + ", " + size + ", " + hex + ");\n"));
    }

}
