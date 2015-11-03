package esvmcompiler.inter;

import esvmcompiler.lexer.Num;
import esvmcompiler.lexer.Token;
import esvmcompiler.lexer.Word;
import esvmcompiler.symbols.Type;

/**
 * Created by serbis on 26.10.15.
 */
public class Constant extends Expr {
    public static final Constant
        True = new Constant(Word.True, Type.Bool),
        False = new Constant(Word.False, Type.Bool);

    public Constant(Token tok, Type p) {
        super(tok, p);
    }

    public Constant(int i) {
        super(new Num(i), Type.Int);
    }

    public void jumping(int t, int f) {
        if (this == True && t != 0)
            emit("goto L" + t);
        else if (this == False && f != 0) {
            emit("goto L" + f);
        }
    }
}
