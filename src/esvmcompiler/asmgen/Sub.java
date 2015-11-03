package esvmcompiler.asmgen;

import esvmcompiler.files.Files;

/**
 * Created by serbis on 30.10.15.
 */
public class Sub extends Instruction {
    public Sub() {}



    public static void gen() {
        count++;
        Files.append(String.valueOf("Sub();\n"));
    }

}
