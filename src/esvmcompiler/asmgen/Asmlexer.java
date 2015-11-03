package esvmcompiler.asmgen;

import esvmcompiler.lexer.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by serbis on 29.10.15.
 */
public class Asmlexer {
    public File irf;   //Файл IR над которым оперируем
    private int line = 1;
    private int seqcounter = 0; //Счетчик инструкций
    private char peek = ' ';
    private String scanstr;
    private int chpointer = 0;
    public Hashtable words = new Hashtable();
    public ArrayList<TokenSeq> toklines = new ArrayList<TokenSeq>();
    public Hashtable<String, Integer> labels = new Hashtable<String, Integer>(); //Hash меток

    public Asmlexer(File file) {
        irf = file;
        reserve(new Word("iffalse", Tag.IFFALSE));
        reserve(new Word("goto", Tag.GOTO));
    }

    private void readch() throws IOException {
        try {
            peek = scanstr.charAt(chpointer);
            chpointer++;
        } catch (IndexOutOfBoundsException e) {
            peek = (char) -1;
        }
    }

    private boolean readch(char c) throws IOException{
        readch();
        if (peek != c) return false;
        peek = ' ';
        return true;
    }

    private void reserve(Word t) {
        words.put(t.lexeme, t);
    }

    public void parcefile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(irf));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] l = line.split(":");   //Бьем строку по метке
            if (l.length > 1) { //Если была метка в начале строки, то получится массив из двух частей
                labels.put(l[0], toklines.size()); //Заносим имя метки и адрес на строку
                toklines.add(parceTokenSeq(l[1]));
            } else {
                if (l[0].charAt(0) == 'L' && Character.isDigit(l[0].charAt(1))) {
                    labels.put(l[0], toklines.size()); //Заносим имя метки и адрес на строку
                    toklines.add(parceTokenSeq("eof"));
                } else {
                    toklines.add(parceTokenSeq(l[0]));
                }
            }

        }
    }

    private TokenSeq parceTokenSeq(String line) throws IOException{
        scanstr = line;
        chpointer = 0;
        peek = ' ';
        seqcounter++;
        TokenSeq tokenSeq = new TokenSeq();
        tokenSeq.line = seqcounter;
        for (int i = 0; i < line.length(); i++) {
            Token tok = scan();
            if (tok == null) {
                return tokenSeq;
            }
            tokenSeq.add(tok);
        }

        return tokenSeq;
    }

    private Token scan() throws IOException{
        while (peek != (char) -1) {
            for (; ; readch()) {
                if (peek == ' ' || peek == '\t') continue;
                else if (peek == '\n')
                    line++;
                else break;
            }
            switch (peek) {
                case '&':
                    if (readch('&')) return Word.and;
                    else return new Token('&');
                case '|':
                    if (readch('|')) return Word.or;
                    else return new Token('|');
                case '=':
                    if (readch('=')) return Word.eq;
                    else return new Token('=');
                case '!':
                    if (readch('=')) return Word.ne;
                    else return new Token('!');
                case '<':
                    if (readch('=')) return Word.le;
                    else return new Token('<');
                case '>':
                    if (readch('=')) return Word.ge;
                    else return new Token('>');
            }
            if (Character.isDigit(peek)) {
                int v = 0;
                do {
                    v = 10 * v + Character.digit(peek, 10);
                    readch();
                } while (Character.isDigit(peek));
                if (peek != '.') return new Num(v);
                float x = v;
                float d = 10;
                for (; ; ) {
                    readch();
                    if (!Character.isDigit(peek)) break;
                    x = x + Character.digit(peek, 10) / d;
                    d = d * 10;
                }
                return new Real(x);
            }

            if (Character.isLetter(peek)) {
                StringBuffer b = new StringBuffer();
                do {
                    b.append(peek);
                    readch();
                } while (Character.isLetterOrDigit(peek));
                String s = b.toString();
                Word w = (Word) words.get(s);
                if (w != null) return w;
                if (s.length() > 1) {   //Проверка на сигрунуру временной переменной
                    if (s.charAt(0) == 't' && Character.isDigit(s.charAt(1))) {
                        w = new Word(s, Tag.TMPID);
                        words.put(s, w);
                        return w;
                    }
                }

                w = new Word(s, Tag.ID);
                words.put(s, w);
                return w;
            }
            Token t = new Token(peek);
            peek = ' ';
            return t;
        }

        return null;
    }
}
