package esvmcompiler.lexer;

/**
 * Created by serbis on 26.10.15.
 */
public class Token {
    public final int tag;
    public int tacpos; //При парсинге tac, номер инструкциии

    public Token(int tag) {
        this.tag = tag;
    }
    public String toString() {
        return "" + (char) tag;
    }
}
