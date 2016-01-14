package esvmcompiler.asmgen;

import esvmcompiler.bytecodegen.Bytecodegen;
import esvmcompiler.commands.*;
import esvmcompiler.files.Files;
import esvmcompiler.inter.Id;
import esvmcompiler.lexer.*;
import esvmcompiler.main.Infos;
import esvmcompiler.symbols.Env;
import esvmcompiler.symbols.Type;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Транслятор трехадресного кода в ассебмлер. Структура класа:
 * -Парсер
 * -Методы парсера
 * -Методы кодоподготовки
 * -Вспомогательные методы
 * -Частные разделения методов
 */
public class Asmgen {
    private File irf;
    private Token look;
    private int tacounter; //Текущя абслоютная позиция обрабатываемой tac инструкции
    private int varcounter = 9999; //Счетчик переменных (первые десять тысяч зарезервированы под временные)
    private Env env; //Экземпляр таблицы символов по исходникам
    private int tokpointer = 0; //Указатель на номер текущего токена
    private Hashtable<String, Var> varatt = new Hashtable<String, Var>(); //Hash достижимости
    private ArrayList<TokenSeq> toklines = new ArrayList<TokenSeq>(); //Построчные поток инструкций
    private Hashtable<String, Integer> labels = new Hashtable<String, Integer>(); //Hash меток
    private ArrayList<TempVar> tempVars = new ArrayList<TempVar>(); //Массив временных переменных
    private ArrayList<BaseBlock> baseBlocks = new ArrayList<BaseBlock>(); //Массив базовых блоков кода
    private ArrayList<Token> nowin; //Текущая парсируемая инструкция
    private Hashtable<String, Integer> tmpvarRelation = new Hashtable<String, Integer>(); //Таблица соотношений переменных


    public Asmgen(File file, Env sym) throws IOException{
        env = sym;
        Asmlexer asmlexer = new Asmlexer(file);
        asmlexer.parcefile();
        toklines = asmlexer.toklines;
        labels = asmlexer.labels;
        irf = asmlexer.irf;
    }

    public void generate() throws IOException{
        Files.setOutFile(new File("/home/serbis/Projects/Outline/EsvmCompiler/" + Infos.classname + ".asm"));
        getVarsAttain(); //Составляем карту достижимости переменных
        fillTempVarArray(10000); //Создаем временные переменные
        splitToBaseblockds(); //Размбивает поток инструкций на базовые блоки
        parceAsm(); //Парсим ассемблерный од
        Files.closeFile();

        //Запускаем генератор байт кода
        Bytecodegen bytecodegen = new Bytecodegen();
        bytecodegen.generate(new File("/home/serbis/Projects/Outline/EsvmCompiler/" + Infos.classname + ".asm"));
    }

    public void parceAsm() throws IOException{
        for (int i = 0; i < baseBlocks.size(); i++) {
            for (int j = 0; j < baseBlocks.get(i).seq.size(); j++) {
                nowin = baseBlocks.get(i).seq.get(j).seq;
                tacounter = baseBlocks.get(i).seq.get(j).line;
                tokpointer = 0;
                look = nowin.get(0);
                switch (look.tag) {
                    case Tag.ID:    //Елси это идентефикатор
                        Token tok = look;
                        move();
                        match('='); //за которым идет знак присваиваня
                        assign(tok); //передаем управления в метод управления присваиваниями
                        break;

                    case Tag.TMPID: //Если инедтефикатор это временная переменная
                        Token toka = look; //И выполняем создаем подставную ВП
                        move();
                        match('='); //за которым идет знак присваиваня
                        assign(toka); //передаем управления в метод управления присваиваниями
                        break;

                    case Tag.IFFALSE:
                        iffalse();
                        break;

                    case Tag.GOTO:
                        gotoo();
                        break;
                }
            }
           // freeTmpVar(); //Освобождаем умершие временные переменные
        }
        Files.closeFile();

    }

    /**
     * НАЧАЛО - БЛОК МЕТОДОВ ПАРСЕРА
     */


    /**
     * Генерирует код безусловного перехода
     *
     */
    public void gotoo() throws IOException {
        move();
        int offset = calcGoto(tacounter, Instruction.count + 1, look.toString());
        Jmp.gen(offset);
    }

    /**
     * Генерирует код условного перехода
     *
     */
    private void iffalse() throws IOException {
        move();
        Token tokOp1 = look; //первый операнд
        move();
        Token op = look; //оператор
        move();
        Token tokOp2 = look; //второй операнд
        int firstTmpVarNum = -1;
        int secondTmpVarNum = -1;

        if ((tokOp1.tag == Tag.NUM || tokOp1.tag == Tag.REAL) &&
                (tokOp2.tag == Tag.NUM || tokOp2.tag == Tag.REAL)) { //Если первый и второй опрерторы числа
            String hex1;
            String hex2;
            //varcounter++;
            firstTmpVarNum = getFreeTmpVar(""); //Получаем номер свободной времянки для первого операнда
            if (tokOp1.tag == Tag.NUM) { //Если первый оператор цело
                hex1 = convertNumToHex(tokOp1, Type.Int);
                Db.gen(firstTmpVarNum, 4, 1);   //Генерируем код - создать первую переменную
            } else { //если дробное
                hex1 = convertNumToHex(tokOp2, Type.Float);
                Db.gen(firstTmpVarNum, 4, 2);
            }
            Set.gen(firstTmpVarNum, 4, hex1); //Генерируем код - определить первую переменную
            //varcounter++;
            secondTmpVarNum = getFreeTmpVar(""); //Получаем номер свободной времянки для второго операнда
            if (tokOp2.tag == Tag.NUM) { //Если второй оператор цело
                hex2 = convertNumToHex(tokOp2, Type.Int);
                Db.gen(secondTmpVarNum, 4, 1); //Генерируем код - создать вторую переменную
            } else { //если дробное
                hex2 = convertNumToHex(tokOp2, Type.Float);
                Db.gen(secondTmpVarNum, 4, 2);
            }
            Set.gen(secondTmpVarNum, 4, hex2); //Генерируем код - определить втроую переменную
            Cmp.gen(firstTmpVarNum, secondTmpVarNum); //Генерируем код - сравнить первую и вторую переменную

        } else if (tokOp1.tag == Tag.ID && tokOp2.tag == Tag.ID){ //или если первый и второй оператор id
            Id id1 = env.getByLexeme(tokOp1.toString()); //ищем номер первой переменной
            Id id2 = env.getByLexeme(tokOp1.toString()); //ищем номер второй переменной
            Cmp.gen(id1.asmnum, id2.asmnum);
        } else if ((tokOp1.tag == Tag.NUM || tokOp1.tag == Tag.REAL) && tokOp2.tag == Tag.ID) { //или если первый оператор число а второй id
            Id id2 = env.getByLexeme(tokOp2.toString()); //ищем номер второй переменной
            String hex;
            //varcounter++;
            firstTmpVarNum = getFreeTmpVar(""); //Получаем номер свободной времянки для первого операнда
            if (tokOp1.tag == Tag.NUM) {
                hex = convertNumToHex(tokOp1, Type.Int);
                Db.gen(firstTmpVarNum, 4, 1);
            } else {
                hex = convertNumToHex(tokOp1, Type.Float);
                Db.gen(firstTmpVarNum, 4, 2);
            }
            Set.gen(firstTmpVarNum, 4, hex); //Генерируем код - определить переменную
            Cmp.gen(firstTmpVarNum, id2.asmnum);
        } else if (tokOp1.tag == Tag.ID && (tokOp2.tag == Tag.NUM || tokOp2.tag == Tag.REAL)) { //или если первый оператор id а второй число
            Id id1 = env.getByLexeme(tokOp1.toString()); //ищем номер второй переменной
            String hex;
            //varcounter++;
            secondTmpVarNum = getFreeTmpVar(""); //Получаем номер свободной времянки для второго операнда
            if (tokOp2.tag == Tag.NUM) {
                hex = convertNumToHex(tokOp2, Type.Int);
                Db.gen(secondTmpVarNum, 4, 1);
            } else {
                hex = convertNumToHex(tokOp2, Type.Float);
                Db.gen(secondTmpVarNum, 4, 2);
            }
            Set.gen(secondTmpVarNum, 4, hex); //Генерируем код - определить переменную
            Cmp.gen(id1.asmnum, secondTmpVarNum);
        }
        move();
        match(Tag.GOTO);
        int offset = calcGoto(tacounter, Instruction.count + 1, look.toString()); //Герерация переда
        if (op.toString().equals("==")) {
            Je.gen(offset);
        } else  if (op.toString().equals("!=")) {
            Jne.gen(offset);
        } else  if (op.toString().equals(">")) {
            Jg.gen(offset);
        } else  if (op.toString().equals("<")) {
            Jl.gen(offset);
        } else  if (op.toString().equals(">=")) {
            Jge.gen(offset);
        } else  if (op.toString().equals("<=")) {
            Jle.gen(offset);
        }

        if (firstTmpVarNum != -1) //Освобождаем временные переменные задействованные в сравнении
            tempVars.get(firstTmpVarNum).busy = false;
        if (secondTmpVarNum != -1)
            tempVars.get(secondTmpVarNum).busy = false;
    }

    /**
     * Генерирует код tac иснтрукции присваевания
     *
     * @param nametok переменная получатель 'a' = ...
     * @throws IOException
     */
    private void assign(Token nametok) throws IOException{
        if (look.tag == Tag.ID || look.tag == Tag.NUM || look.tag == Tag.REAL) { // если после = идет число или id
            Token tok = look; //запоминаем тэг
            move(); //пробуем пройти на токен вперед
            if (look != null) { //и если он есть, то значит присваеивается выражение
                assignExpr(nametok, tok);
            } else { //если токена нет, то тогда это одирное присвоение
                if (tok.tag == Tag.NUM || tok.tag == Tag.REAL) { //если присваевается целое или дробнон
                    assignNum(nametok, tok);
                } else if (tok.tag == Tag.ID){ //если присваевается id
                    assignId(nametok, tok);
                } else { //если присваевается веменная переменная
                    //Оставлено как заглушка, так как высказано предположение, что данная ситуация в теории невозможна
                }
            }
        } else if (look.tag == Tag.TMPID) { //или если после = идет временная переменная
            Token tok = look; //запоминаем тэг
            move(); //пробуем пройти на токен вперед
            if (look != null) { //и если он есть, то значит присваеивается выражение
                assignExpr(nametok, tok);
            } else { //если токена нет, то тогда это одирное присвоение
                //Оставлено как заглушка, так как высказано предположение, что данная ситуация в теории невозможна
            }
        }
    }

    /**
     * Гененрирует ассмблерной код при одинарном присваевании числа
     *
     * @param nametok
     * @param tok
     */
    private void assignNum(Token nametok, Token tok) {
        Id id = env.getByLexeme(nametok.toString()); //ищет переменную в таблице символов
        if (id != null) { //и если она там есть
            Type type = id.type; //опрелеляем тип переменной
            String hex = convertNumToHex(tok, id.type); //Клнвертируем чиcло в hex
            if (id.asmnum == -1) { //проверяем что переменной не присвоен номер
                varcounter++; //увеличиваем счетчик переменных на 1
                Db.gen(varcounter, type.width, type.code); //создаем новую переменную
                Set.gen(varcounter, type.width, hex); //присвеваем ей значение
                env.setByLexeme(nametok.toString(), varcounter);//елси это так, присваеваем номер
            } else { //если она уже присваевалась
                Set.gen(id.asmnum, type.width, hex); //то просто присвеваем ей значение
            }

        }
    }

    /**
     * Гененрирует ассмблерной код при одинарном присваевании переменной
     *
     * @param nametok
     * @param tok
     */
    private void assignId(Token nametok, Token tok) {
        Id idl = env.getByLexeme(nametok.toString()); //ищет левую переменную в ТС
        Id idr = env.getByLexeme(tok.toString()); //ищет правую переменную в ТС
        if (idr != null) { //и если она там есть
            if (idl.asmnum == -1) {
                varcounter++;
                Db.gen(varcounter, idl.type.width, idl.type.code); //создаем новую переменную
                env.setByLexeme(nametok.toString(), varcounter); //присваеваем номер
            }
            Pushv.gen(idl.asmnum);
            Pop.gen(idr.asmnum);
        }
    }


    /**
     * Гененрирует ассмблерной код при присваевании выражения
     *
     */
    public void assignExpr(Token nametok, Token tok) throws IOException {
        Token tokOp1 = tok; //первый операнд
        Token op = look; //оператор
        move();
        Token tokOp2 = look; //второй операнд
        Type op1Type = null; //тип данных первого операнда
        Type op2Type = null; //тип второго первого операнда

        if (tokOp1.tag == Tag.NUM || tokOp1.tag == Tag.REAL) { //Если первый оператор число
            pushNumOrFloat(tokOp1);
            if (tokOp1.tag == Tag.NUM)  //запомниаем тип данных первого операнда
                op1Type = Type.Int;
            else
                op1Type = Type.Float;
        } else if (tokOp1.tag == Tag.ID){ //или если первый оператор id
            Id id = env.getByLexeme(tokOp1.toString()); //ищем номер переменной
            Pushv.gen(id.asmnum);
            op1Type = id.type;  //запомниаем тип данных первого операнда
        } else { //или если первый оператор временная переменная
            Enumeration enumeration = tmpvarRelation.keys(); //Ищем какой номер временной переменной в соотношении
            String key;
            while (enumeration.hasMoreElements()) {
                key = (String) enumeration.nextElement();
                if (key.equals(tokOp1.toString())) {
                    Pushv.gen(tmpvarRelation.get(key));
                    break;
                }
            }
        }
        if (tokOp2.tag == Tag.NUM || tokOp2.tag == Tag.REAL) { //Если второй оператор число
            pushNumOrFloat(tokOp2);
            if (tokOp1.tag == Tag.NUM)  //запомниаем тип данных второго операнда
                op2Type = Type.Int;
            else
                op1Type = Type.Float;
        } else if (tokOp1.tag == Tag.ID){ //или если второй оператор id
            Id id = env.getByLexeme(tokOp2.toString()); //ищем номер переменной
            Pushv.gen(id.asmnum);
            op2Type = id.type;  //запомниаем тип данных первого операнда
        } else { //или если второй оператор временная переменная
            Enumeration enumeration = tmpvarRelation.keys(); //Ищем какой номер временной переменной в соотношении
            String key;
            while (enumeration.hasMoreElements()) {
                key = (String) enumeration.nextElement();
                if (key.equals(tokOp2.toString())) {
                    Pushv.gen(tmpvarRelation.get(key));
                    break;
                }
            }
        }

        if (op.toString().equals("+")) { //Перебираем оперторы и генерируем нужный
            Add.gen();
        } else if (op.toString().equals("-")) {
            Sub.gen();
        } else if (op.toString().equals("/")) {
            Mul.gen();
        } else {
            Div.gen();
        }

        Id id = env.getByLexeme(nametok.toString()); //ищет левую переменную в ТС
        if (id != null) {
            if (id.asmnum == -1) {
                varcounter++; //тут
                Db.gen(varcounter, id.type.width, id.type.code); //создаем новую переменную
                env.setByLexeme(nametok.toString(), varcounter); //присваеваем номер
            }
            Pop.gen(id.asmnum); //извлеваем результат оперции из стека в переменную
        } else { //Если id=null значит это продукция tac, или иначе говоря временная переменная
            int tmpn = getFreeTmpVar(nametok.toString()); //Получаем номер свободной времянки
            tmpvarRelation.put(nametok.toString(), tmpn); //Заносим в таблицу соотношений
            if (op1Type == Type.Float || op2Type == Type.Float) //Если хоть один операнд float
                Db.gen(tmpn, Type.Float.width, Type.Float.code); //создаем новую переменную c тип float
            else //Иначе это целое число
                Db.gen(tmpn, Type.Int.width, Type.Int.code); //создаем новую переменную c тип float
            Pop.gen(tmpn); //извлеваем результат оперции из стека во временную переменную
        }

    }

    /**
     * КОНЕЦ - БЛОК МЕТОДОВ ПАРСЕРА
     *
     */

    /**
     * НАЧАЛО - МЕТОДЫ КОДОПОГОТОВКИ
     *
     */

    /**
     * Разбивает поток токенов на базовые блоки
     *
     */
    public void splitToBaseblockds(){
        BaseBlock baseBlock = new BaseBlock();
        for (int i = 0; i < toklines.size(); i++) {
            for (int j = 0; j < toklines.get(i).size(); j++) {
                if (i == 0) { //Если это первая инструкция то это начало блока
                    baseBlock = new BaseBlock(toklines.get(i));
                    break;
                } else if (labels.contains(i)) { //Если на инструкцию ссылается метка то это начало блока
                    if (i > 0) { //и если это не нулевая инструкция, так как у нее всегда есть метка
                        baseBlocks.add(baseBlock);
                        baseBlock = new BaseBlock(toklines.get(i));
                        break;
                    }
                } else if (j > 0) {
                    if (toklines.get(i).get(j - 1).tag == Tag.GOTO) { //Если инструция заканчивается ссылкой на метку, то след инстр это начало блока
                        baseBlocks.add(baseBlock);
                        baseBlock = new BaseBlock(toklines.get(i + 1));
                        i++;
                        break;
                    }
                } else {
                    baseBlock.seq.add(toklines.get(i));
                }

            }
        }
    }

    /**
     * Опрелеяет достижимость всех идентефикаторов в коде
     *
     */
    private void getVarsAttain() {
        for (int i = 0; i < toklines.size(); i++) {
            for (int j = 0; j < toklines.get(i).size(); j++) {
                Token tok = toklines.get(i).get(j);
                if (tok.tag == Tag.ID || tok.tag == Tag.TMPID) {
                    Var v = (Var) varatt.get(tok.toString());
                    if (v != null) {
                        varatt.get(tok.toString()).death = i;
                    } else {
                        varatt.put(tok.toString(), new Var(tok.toString(), i));
                    }

                }
            }
        }
    }

    /**
     * Заполняет массив временных переменных пустышками
     *
     * @param count количество временных переменных
     */
    private void fillTempVarArray(int count) {
        for (int i = 0; i < count; i++) {
            tempVars.add(new TempVar(i, false));
        }
    }


    /**
     * КОНЕЦ - МЕТОДЫ КОДОПОГОТОВКИ
     *
     */

    /**
     * НАЧАЛО - ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
     *
     */


    /**
     * Конвертирует некоторое число в hex строку
     *
     * @param tok токен числа
     * @param type тип в который конвертируется
     * @return hex строка
     */
    private String convertNumToHex(Token tok, Type type) {
        String hex;
        if (tok.tag == Tag.NUM) { //дальше если это целое
            Num num = (Num) tok;
            hex = Integer.toHexString(num.value).toUpperCase(); //получаем hex цллого числа
        } else { //если дробное
            Real real = (Real) tok;
            int t = Float.floatToRawIntBits(real.value);
            hex = Integer.toHexString(t).toUpperCase(); //получаем float hex
        }
        if (hex.length() < (type.width * 2)) { //добавляем отсутствуюшие нули
            String h = hex;
            for (int i = 0; i < (type.width * 2) - h.length(); i++) {
                hex = "0" + hex;
            }
        }

        return hex;
    }

    /**
     * Вычиляем позцию для перехода по инструкции goto в ассеблерном коде.
     * Работает на приципе просмотра n комманд вперед и муммирования всех
     * их размерностей.
     *
     * @param tacstart номер инструкции от котро идет отсчет в tac
     * @param inststart номер инструкции от которой идет отсчет в asm
     * @param label метка на которую просиходит переход
     * @return номер инструции asm эквивалетный метке
     */
    private int calcGoto(int tacstart, int inststart, String label) {
        int offset = 0;
        Enumeration keys = labels.keys();   //Вычисляем на какой номер tac инструкции сслыается label
        int lpos = -1;
        while (keys.hasMoreElements()) {
            String l = (String) keys.nextElement();
            if (l.equals(label)) {
                lpos = labels.get(l);
                break;
            }
        }

        if (tacstart < lpos) { //Если метка впереди
            for (int i = tacstart; i < lpos; i++) { //Идем в цикле по toklines от start до вычисленной выше позици
                offset += gotoSigSelector(i);
            }
            return inststart + offset; //После цикла плюсуем кол-во + start и возвращаем
        } else { //Если метка позади
            for (int i = tacstart; i > lpos; i--) { //Идем в цикле по toklines от start до вычисленной выше позици
                offset += gotoSigSelector(i);
            }
            return inststart - offset; //После цикла плюсуем кол-во + start и возвращаем
        }
    }

    /**
     * Возвращает номер сигнатуры инструкции tac
     *
     * @param innum номер инструкции в потоке
     * @return номер сигнатуры
     */
    private int getSignature(int innum) {
        return toklines.get(innum).seq.size();
    }

    /**
     * Сравнивает текущий токе с указанным в параметре. Если не идентичны
     * выводит ошибку
     *
     * @param t токе для сравнения
     * @throws IOException если токены не равны
     */
    private void match(int t) throws IOException {
        if (look.tag == t)
            move();
        else
            error("syntax error");
    }

    /**
     * Выбирает новый ток для рассмотрения
     *
     * @throws IOException
     */
    private void move() throws IOException {
        try {
            tokpointer++;
            look = nowin.get(tokpointer);
        } catch (Exception e){
            look = null;
        }
    }

    /**
     * Выводи ошибку
     *
     * @param s строка с ошибкой
     */
    private void error(String s) {
        throw new Error(s);
    }

    /**
     * Возращает номер первой свободной временной переменной
     *
     * @return код временной переменной или -1 если их нет
     */
    private int getFreeTmpVar(String name) {
        freeTmpVars();
        for (int i = 0; i < tempVars.size(); i++) {
            if (!tempVars.get(i).busy) {
                tempVars.get(i).busy = true;
                tempVars.get(i).name = name;
                return i;
            }
        }

        return -1;
    }

    /**
     * Высвобождает умершие временые переменные. Вызывается после каждой tac
     * инструкции, для проверки достижимости временных переменных и их
     * высвобождения
     *
     */
    private void freeTmpVars() {
        ArrayList<Var> exvar = new ArrayList<Var>();
        Enumeration h = varatt.elements();
        while (h.hasMoreElements()) { //Ищем в массиве хеше varattain переменные котые цикл жизни которых заканчился
            Var var = (Var) h.nextElement();
            if (var.death < tacounter && !var.destroy) {
                exvar.add(var);
                var.destroy = true;
            }
        }
        for (Var anExvar : exvar) { //Ищем в массиве временнных переменных соответвие по имени
            for (TempVar tempVar : tempVars) {
                if (anExvar.name.equals(tempVar.name)) { //Если оно найдено
                    tempVar.busy = false; //помечаем переменную как свободну
                    tempVar.name = ""; //Убираем у нее метку имени
                    h = tmpvarRelation.keys(); //и удалем из хеша соотношений имя/машинный номер
                    while (h.hasMoreElements()) {
                        String name = (String) h.nextElement();
                        if (name.equals(anExvar.name)) {
                            tmpvarRelation.remove(name);
                        }
                    }

                }
            }
        }
    }

    /**
     * КОНЕЦ - ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
     *
     */

    /**
     * НАЧАЛО - ЧАСТНЫЕ РАЗДЕЛЕНИЯ МЕТОДОВ
     *
     */

    /**
     * Частное разделение метода calcGoto
     *
     * @param i
     * @return
     */
    private int gotoSigSelector(int i) {
        int offset = 0;
        switch (getSignature(i)) {//Берем сигнатуру
            case 3: //Проверяем если это сигнатура 3 или 5 то проверяем существует ли переменная
                Id id = env.getByLexeme(toklines.get(i).seq.get(0).toString()); //ищет правую переменную в ТС
                offset += 2;
                if (id == null) { //и если ее там нет
                    offset += 1;
                }
                break;
            case 5:
                Id id1 = env.getByLexeme(toklines.get(i).seq.get(0).toString()); //ищет правую переменную в ТС
                offset += 4;
                if (id1 == null) { //и если ее там нет
                    offset += 1;
                }
                break;

            case 6: //Проверяем если это сигнатура 6, опредялем кол-во чиловых операндов и в зав. от их кол-ва плюсуемся
                offset += 2;
                if (toklines.get(i).seq.get(1).tag != Tag.ID) {
                    offset += 2;
                }
                if (toklines.get(i).seq.get(3).tag != Tag.ID) {
                    offset += 2;
                }
                break;

            case 2: //Проверяем если это сигнатура 2, то просто плюсуемся
                offset += 1;
                break;
        }

        return offset;
    }

    /**
     * Генерирует команду Push на числовой операнд
     *
     * @param operand взрляший операнд
     */
    private void pushNumOrFloat(Token operand) {
        String hex = convertNumToHex(operand, Type.Int);
        int type;
        if (operand.tag == Tag.NUM) {
            type = 1;
        } else {
            type = 2;
        }
        Push.gen(type, hex);
    }
}
