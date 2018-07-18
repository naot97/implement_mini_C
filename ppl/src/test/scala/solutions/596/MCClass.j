.source MCClass.java
.class public MCClass
.super java.lang.Object
.field static EPSILON F

.method public static squareRoot(F)F
Label0:
.var 0 is x F from Label0 to Label1
	fload_0
	iconst_1
	i2f
	invokestatic MCClass/testSQ(FF)F
	freturn
Label1:
	ldc 0.0
	freturn
.limit stack 3
.limit locals 1
.end method

.method public static testSQ(FF)F
Label0:
.var 0 is a F from Label0 to Label1
.var 1 is b F from Label0 to Label1
	fload_0
	fload_1
	fdiv
	fload_1
	invokestatic MCClass/closeEnough(FF)Z
	ifle Label2
	fload_1
	freturn
	goto Label3
Label2:
	fload_0
	fload_0
	fload_1
	invokestatic MCClass/betterGuess(FF)F
	invokestatic MCClass/testSQ(FF)F
	freturn
Label3:
Label1:
	ldc 0.0
	freturn
.limit stack 4
.limit locals 2
.end method

.method public static closeEnough(FF)Z
Label0:
.var 0 is a F from Label0 to Label1
.var 1 is b F from Label0 to Label1
	fload_0
	fload_1
	fsub
	iconst_0
	i2f
	fcmpl
	iflt Label2
	iconst_1
	goto Label3
Label2:
	iconst_0
Label3:
	ifle Label4
	fload_0
	fload_1
	fsub
	getstatic MCClass.EPSILON F
	fcmpl
	ifge Label6
	iconst_1
	goto Label7
Label6:
	iconst_0
Label7:
	ireturn
	goto Label5
Label4:
	fload_1
	fload_0
	fsub
	getstatic MCClass.EPSILON F
	fcmpl
	ifge Label8
	iconst_1
	goto Label9
Label8:
	iconst_0
Label9:
	ireturn
Label5:
Label1:
	iconst_0
	ireturn
.limit stack 5
.limit locals 2
.end method

.method public static betterGuess(FF)F
Label0:
.var 0 is a F from Label0 to Label1
.var 1 is b F from Label0 to Label1
	fload_1
	fload_0
	fload_1
	fdiv
	fadd
	iconst_2
	i2f
	fdiv
	freturn
Label1:
	ldc 0.0
	freturn
.limit stack 4
.limit locals 2
.end method

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
.var 1 is a F from Label0 to Label1
.var 2 is b F from Label0 to Label1
.var 3 is c F from Label0 to Label1
.var 4 is delta F from Label0 to Label1
.var 5 is x_1 F from Label0 to Label1
.var 6 is x_2 F from Label0 to Label1
	ldc 0.001
	putstatic MCClass.EPSILON F
	iconst_2
	i2f
	fstore_1
	iconst_5
	i2f
	fstore_2
	iconst_2
	i2f
	fstore_3
	fload_2
	fload_2
	fmul
	iconst_4
	i2f
	fload_1
	fmul
	fload_3
	fmul
	fsub
	fstore 4
	fload 4
	iconst_0
	i2f
	fcmpl
	ifge Label2
	iconst_1
	goto Label3
Label2:
	iconst_0
Label3:
	ifle Label4
Label6:
	ldc "The equation has no solution"
	invokestatic io/putString(Ljava/lang/String;)V
	return
Label7:
	goto Label5
Label4:
Label8:
	fload_2
	fneg
	fload 4
	invokestatic MCClass/squareRoot(F)F
	fsub
	iconst_2
	i2f
	fload_1
	fmul
	fdiv
	fstore 5
	fload_2
	fneg
	fload 4
	invokestatic MCClass/squareRoot(F)F
	fadd
	iconst_2
	i2f
	fload_1
	fmul
	fdiv
	fstore 6
	ldc "x_1 = "
	invokestatic io/putString(Ljava/lang/String;)V
	fload 5
	invokestatic io/putFloat(F)V
	ldc "; x_2 = "
	invokestatic io/putString(Ljava/lang/String;)V
	fload 6
	invokestatic io/putFloat(F)V
Label9:
Label5:
	return
Label1:
	return
.limit stack 5
.limit locals 7
.end method

.method public <init>()V
Label0:
.var 0 is this LMCClass; from Label0 to Label1
	aload_0
	invokespecial java/lang/Object/<init>()V
Label1:
	return
.limit stack 2
.limit locals 1
.end method
