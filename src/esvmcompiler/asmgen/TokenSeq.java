package esvmcompiler.asmgen;

import esvmcompiler.lexer.Token;

import java.util.ArrayList;

/**
 * Created by serbis on 29.10.15.
 */
public class TokenSeq {
    public ArrayList<Token> seq = new ArrayList<Token>();
    public int line;

    public TokenSeq() {};

    public void add(Token token) {
        seq.add(token);
    }

    public void set(int pos, Token tok) {
        seq.set(pos, tok);
    }

    public int size() {
        return seq.size();
    }

    public Token get(int i) {
        return seq.get(i);
    }
}
