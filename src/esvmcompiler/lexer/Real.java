package esvmcompiler.lexer;

/**
 * Created by serbis on 26.10.15.
 */
public class Real extends Token{
    public final float value;

    public Real(float v) {
        super(Tag.REAL);
        value = v;
    }

    public String toString() {
        return "" + value;
    }
}
