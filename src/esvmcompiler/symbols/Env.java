package esvmcompiler.symbols;

import esvmcompiler.inter.Id;
import esvmcompiler.lexer.Token;
import esvmcompiler.lexer.Word;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by serbis on 26.10.15.
 */
public class Env {
    private Hashtable table;
    public Env prev;

    public Env(Env n) {
        table = new Hashtable();
        prev = n;
    }

    public void put(Token w, Id i) {
        table.put(w, i);
    }

    public Id get(Token w) {
        for (Env e = this; e != null; e = e.prev) {
            Id found = (Id) (e.table.get(w));
            if (found != null) return found;
        }
        return null;
    }

    /**
     * Возаращает из хеша объект по лексеме а не по объекту
     *
     * @return id
     */
    public Id getByLexeme(String lexeme) {
        Enumeration enumeration = table.keys();
        Word key;
        while (enumeration.hasMoreElements()) {
            key = (Word) enumeration.nextElement();
            if (key.lexeme.equals(lexeme)) {
                return (Id) table.get(key);
            }
        }

        return null;
    }

    public void setByLexeme(String lexeme, int number) {
        Enumeration enumeration = table.keys();
        Word key;
        while (enumeration.hasMoreElements()) {
            key = (Word) enumeration.nextElement();
            if (key.lexeme.equals(lexeme)) {
                Id id = (Id) table.get(key);
                id.asmnum = number;
            }
        }
    }
}
