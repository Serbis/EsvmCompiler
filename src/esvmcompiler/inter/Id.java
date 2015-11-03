package esvmcompiler.inter;

import esvmcompiler.lexer.Word;
import esvmcompiler.symbols.Type;

/**
 * Created by serbis on 26.10.15.
 */
public class Id extends Expr {
    public int offset;
    public int asmnum = -1;

    public Id(Word id, Type p, int b) {
        super(id, p);
        offset = b;
    }
}
