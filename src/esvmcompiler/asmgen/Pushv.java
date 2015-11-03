package esvmcompiler.asmgen;

import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Pushv extends Instruction {
    public Pushv() {}



    public static void gen(int number) {
        count++;
        Files.append(String.valueOf("Pushv(" + number +  ");\n"));
    }

}
