package esvmcompiler.asmgen;

/**
 * Created by serbis on 29.10.15.
 */
public class TempVar {
    public int number;
    public boolean busy;

    public TempVar() {}

    public TempVar(int number, boolean busy) {
        number = number;
        busy = busy;
    }
}
