package esvmcompiler.bytecodegen;

import esvmcompiler.commands.*;
import esvmcompiler.files.Files;
import esvmcompiler.main.Infos;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Генерирует байт-код и метаданные необходимые для заполнения полей
 * class файла
 */



public class Bytecodegen {
    public Bytecodegen() {

    }

    /**
     * Транслирует входящий поток ассеблерного кода в байт-код
     *
     * @param file файл с ассемблерным ir
     * @return массив байт байт-код
     */
    public void generate(File file) {
        Files.setOutFile(new File("/home/serbis/Projects/Outline/EsvmCompiler/" + Infos.classname + ".byc"));
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        String asmline = readFile(file);
        String[] instructs = asmline.split(";");

        for (int i = 0; i < instructs.length; i++) {
            String[] dup = instructs[i].split("\\(");
            String com = dup[0].replaceAll(" ", "");
            Selector selector = codeSelector(com);
            if (selector != null) {
                bytes.add(selector.code);
            } else {
                error("Unknown instruction on line " + String.valueOf(i));
                return;
            }
            dup[1] = dup[1].replaceAll("\\)", "");
            String[] args = dup[1].split(",");
            int len = args.length;
            if (Objects.equals(args[0], "")) {
                len = 0;
            }
            if (len == selector.args) {
                for (int j = 0; j < len; j++) {
                    String a = args[j].replace(" ", "");
                    byte[] ab = convertArgToBytes(a, selector.code, j);

                    for (byte anAb : ab) {
                        bytes.add(anAb);
                    }
                }
            } else {
                error("Instructions on line " + String.valueOf(i) + " requires " + String.valueOf(selector.args) + " arguments, but found " + args.length);
            }
        }
        Files.append(convertToSimpleArray(bytes)); //Пишем в байткод в файл
        Files.closeFile();

        //Тут должен быть вызов генертора class файла
    }

    public byte[] convertToSimpleArray(ArrayList<Byte> bytes) {
        byte[] bt = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            bt[i] = bytes.get(i);
        }

        return bt;
    }

    public String readFile(File file) {
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return sb.toString();
    }

        /**
     * Возращает селектор инструкций с кодом опрарации и количеством ее
     * аргементов
     *
     * @return селектор
     */
    private Selector codeSelector(String inst) {
        Selector selector = new Selector();

        switch (inst) {
            case Add.asm:
                selector.code = Add.code;
                selector.args = 0;
                return selector;

            case Sub.asm:
                selector.code = Sub.code;
                selector.args = 0;
                return selector;

            case Push.asm:
                selector.code = Push.code;
                selector.args = 2;
                return selector;

            case Pop.asm:
                selector.code = Pop.code;
                selector.args = 1;
                return selector;

            case Cmp.asm:
                selector.code = Cmp.code;
                selector.args = 2;
                return selector;

            /*case "Inc":
                selector.code = Inc.code;
                selector.args = 1;
                return selector;

            case "Dec":
                selector.code = Dec.code;
                selector.args = 1;
                return selector;*/

            case Mul.asm:
                selector.code = Mul.code;
                selector.args = 0;
                return selector;

            /*case "IMul":
                selector.code = IMul.code;
                selector.args = 0;
                return selector;

            case "Method":
                selector.code = Method.code;
                selector.args = 1;
                return selector;

            case "Movos":
                selector.code = Movos.code;
                selector.args = 1;
                return selector;

            case "Xchg":
                selector.code = Xchg.code;
                selector.args = 2;
                return selector;

            case "Lea":
                selector.code = Lea.code;
                selector.args = 2;
                return selector;*/

            /*case "Int":
                selector.code = Int.code;
                selector.args = 2;
                return selector;

            case "Loop":
                selector.code = Loop.code;
                selector.args = 2;
                return selector;*/

            case Jmp.asm:
                selector.code = Jmp.code;
                selector.args = 1;
                return selector;

            case Div.asm:
                selector.code = Div.code;
                selector.args = 0;
                return selector;

            /*case "IDiv":
                selector.code = IDiv.code;
                selector.args = 0;
                return selector;*/

            case Je.asm:
                selector.code = Je.code;
                selector.args = 1;
                return selector;

            /*case "Jz":
                selector.code = Jz.code;
                selector.args = 1;
                return selector;
*/
            case Jg.asm:
                selector.code = Jg.code;
                selector.args = 1;
                return selector;

            case Jge.asm:
                selector.code = Jge.code;
                selector.args = 1;
                return selector;

            case Jl.asm:
                selector.code = Jl.code;
                selector.args = 1;
                return selector;

            case Jle.asm:
                selector.code = Jle.code;
                selector.args = 1;
                return selector;

            case Jne.asm:
                selector.code = Jne.code;
                selector.args = 1;
                return selector;

            /*case "Jnge":
                selector.code = Jnge.code;
                selector.args = 1;
                return selector;

            case "Jnl":
                selector.code = Jnl.code;
                selector.args = 1;
                return selector;

            case "Jnle":
                selector.code = Jnge.code;
                selector.args = 1;
                return selector;

            case "Out":
                selector.code = Out.code;
                selector.args = 2;
                return selector;

            case "Inp":
                selector.code = Inp.code;
                selector.args = 2;
                return selector;*/

            case Db.asm:
                selector.code = Db.code;
                selector.args = 3;
                return selector;

            case Set.asm:
                selector.code = Set.code;
                selector.args = 3;
                return selector;

            case Pushv.asm:
                selector.code = Pushv.code;
                selector.args = 1;
                return selector;

            /*case "Outv":
                selector.code = Outv.code;
                selector.args = 2;
                return selector;*/
        }

        return null;
    }

    /**
     * Конвертирует аргмеунт в массив байт в зависимости от того,
     * какой тип у аргумента конкретной инструкции
     *
     * @param arg строковое представление аргумента
     * @param instCode код инструкции
     * @return массив байт пердставления аргумента
     */
    private byte[] convertArgToBytes(String arg, int instCode, int argnum) {
        ByteBuffer byteBuffer;
        switch (instCode) {
            case Push.code:
                if (argnum == 0) {
                    return toByte(arg);
                } else {
                    return toBytes(arg);
                }

            case Pop.code:
                return toShort(arg);

            case Cmp.code:
                return toShort(arg);

            /*case Inc.code:
                return toShort(arg);

            case Dec.code:
                return toShort(arg);

            case Method.code:
                return toShort(arg);

            case Movos.code:
                return toShort(arg);

            case Xchg.code:
                return toShort(arg);

            case Lea.code:
                return toShort(arg);

            case Int.code:
                return toByte(arg);

            case Loop.code:
                if (argnum == 0) {
                    return toShort(arg);
                } else {
                    return toInt(arg);
                }*/

            case Jmp.code:
                return toInt(arg);

            case Je.code:
                return toInt(arg);

            /*case Jz.code:
                return toInt(arg);*/

            case Jg.code:
                return toInt(arg);

            case Jge.code:
                return toInt(arg);

            case Jl.code:
                return toInt(arg);

            case Jle.code:
                return toInt(arg);

            case Jne.code:
                return toInt(arg);

            /*case Jnge.code:
                return toInt(arg);

            case Jnl.code:
                return toInt(arg);

            case Jnle.code:
                return toInt(arg);

            case Out.code:
                if (argnum == 0) {
                    return toByte(arg);
                } else {
                    return toInt(arg);
                }

            case Inp.code:
                if (argnum == 0) {
                    return toByte(arg);
                } else {
                    return toShort(arg);
                }*/

            case Db.code:
                if (argnum == 0) {
                    return toShort(arg);
                } else if (argnum == 1) {
                    return toShort(arg);
                } else {
                    return toByte(arg);
                }

            case Set.code:
                if (argnum == 0) {
                    return toShort(arg);
                } else if (argnum == 1) {
                    return toInt(arg);
                } else {
                    return toBytes(arg);
                }

            case Pushv.code:
                return toShort(arg);

            /*case Outv.code:
                if (argnum == 0) {
                    return toByte(arg);
                } else {
                    return toShort(arg);
                }*/
        }
        return null;
    }

    private byte[] toShort(String arg) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2);
        byteBuffer.putShort(Short.parseShort(arg));
        return byteBuffer.array();
    }

    private byte[] toInt(String arg) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putInt(Integer.parseInt(arg));
        return byteBuffer.array();
    }

    private byte[] toByte(String arg) {
        return new byte[] {Byte.parseByte(arg)};
    }

    /**
     * Конвертирует hex строку в массив байт
     *
     * @param hexs hex строка
     * @return массив байт
     */
    private byte[] toBytes(String hexs) {
        return DatatypeConverter.parseHexBinary(hexs);
    }

    private void error(String s) {
        throw new Error(s);
    }



    /**
     * Описывает селектор инструкции. Содержит в себе количество аргуметов
     * инструкции и ее код.
     *
     */
    private class Selector {
        int args;
        byte code;

        public Selector() {

        }
    }
}

