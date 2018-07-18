.source MCClass.java
.class public MCClass
.super java.lang.Object
.field static a F

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
.var 1 is a F from Label0 to Label1
	iconst_1
	i2f
	fstore_1
Label2:
	iconst_2
	i2f
	fload_1
	invokestatic MCClass/foo(FF)I
	i2f
	invokestatic io/putFloatLn(F)V
Label3:
Label1:
	return
.limit stack 3
.limit locals 2
.end method

.method public static foo(FF)I
Label0:
.var 0 is a F from Label0 to Label1
.var 1 is b F from Label0 to Label1
Label2:
Label4:
Label6:
Label7:
Label5:
	iconst_2
	ineg
	ireturn
Label3:
Label1:
	iconst_0
	ireturn
.limit stack 2
.limit locals 2
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
