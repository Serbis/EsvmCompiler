Db(10000, 4, 1);
Set(10000, 4, 00000005);
Set(10000, 4, 00000003);
Db(10001, 4, 2);
Set(10001, 4, 00000001);
Pushv(10001);
Pop(10000);
Pushv(10000);
Pushv(10001);
Add();
Db(0, 4, 2);
Pop(0);
Pushv(10001);
Push(1, 00000002);
Add();
Db(1, 4, 2);
Pop(1);
Pushv(0);
Pushv(1);
Mul();
Pop(10000);
Db(0, 4, 1);
Set(0, 4, 00000001);
Cmp(10000, 0);
Je(28);
Pushv(10001);
Pop(10000);
Jmp(23);
Set(10000, 4, 00000001);
