package esvmcompiler.inter;

import esvmcompiler.lexer.Word;
import esvmcompiler.symbols.Type;

/**
 * Created by serbis on 26.10.15.
 */
public class Temp extends Expr{
    private static int count = 0;
    private int number = 0;

    public Temp(Type p) {
        super(Word.temp, p);
        number = ++count;
    }

    public String toString() {
        return "t" + number;
    }
}
