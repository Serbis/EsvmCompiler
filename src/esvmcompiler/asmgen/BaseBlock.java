package esvmcompiler.asmgen;

import java.util.ArrayList;

/**
 * Created by serbis on 29.10.15.
 */
public class BaseBlock {
    public ArrayList<TokenSeq> seq = new ArrayList<TokenSeq>();

    public BaseBlock() {

    }

    public BaseBlock(TokenSeq tokenSeq) {
        seq.add(tokenSeq);
    }
}
