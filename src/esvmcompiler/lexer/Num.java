package esvmcompiler.lexer;

/**
 * Created by serbis on 26.10.15.
 */
public class Num extends Token {
    public final int value;

    public Num(int v) {
        super(Tag.NUM);
        value = v;
    }
    public String toString() {
        return "" + value;
    }
}
