package esvmcompiler.commands;

import esvmcompiler.asmgen.Instruction;
import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Sub extends Instruction {
    public final static byte code = 2;
    public final static String asm = "Sub";

    public Sub() {}

    public static void gen() {
        count++;
        Files.append(String.valueOf("Sub();\n"));
    }

    public static void genRaw(int number, int size, String hex) {

    }

}
