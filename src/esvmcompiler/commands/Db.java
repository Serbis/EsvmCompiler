package esvmcompiler.commands;

import esvmcompiler.asmgen.Instruction;
import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Db extends Instruction {
    public static final byte code = 31;
    public final static String asm = "Db";

    public Db() {}

    public static void gen(int number, int size, int type) {
        count++;
        Files.append(String.valueOf("Db(" + number + ", " + size + ", " + type + ");\n"));
    }

    public static void genRaw(int number, int size, int type) {

    }

}
