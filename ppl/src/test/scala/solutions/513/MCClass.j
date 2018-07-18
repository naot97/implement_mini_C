.source MCClass.java
.class public MCClass
.super java.lang.Object
.field static a I

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
	iconst_1
	ineg
	iconst_2
	iconst_4
	ineg
	imul
	iadd
	bipush 6
	iadd
	ineg
	ineg
	putstatic MCClass.a I
	getstatic MCClass.a I
	putstatic MCClass.a I
Label4:
	getstatic MCClass.a I
	bipush 10
	if_icmple Label5
	iconst_1
	goto Label6
Label5:
	iconst_0
Label6:
	ifle Label3
	iconst_0
	putstatic MCClass.a I
Label2:
	getstatic MCClass.a I
	iconst_1
	iadd
	putstatic MCClass.a I
	goto Label4
Label3:
	getstatic MCClass.a I
	invokestatic io/putIntLn(I)V
Label1:
	return
.limit stack 4
.limit locals 1
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
