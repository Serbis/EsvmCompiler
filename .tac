L1:L3:
i = i + 1L5:
t1 = i * 4
t2 = a [ t1 ] 
if t2 < vgoto L3L4:
j = j - 1L7:
t3 = j * 4
t4 = a [ t3 ] 
if t4 > vgoto L4L6:
iffalse i >= j goto L8L9:
goto L2L8:
x = a [ i * 4 ] L10:
t5 = i * 4
t6 = j * 4
t7 = a [ t6 ] 
a [ t5 ] = t7L11:
t8 = j * 4
a [ t8 ] = x
goto L1L2: