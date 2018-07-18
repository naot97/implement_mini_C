.source MCClass.java
.class public MCClass
.super java.lang.Object
.field static a I

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
.var 1 is i I from Label0 to Label1
	iconst_1
	putstatic MCClass.a I
	iconst_1
	istore_1
Label4:
	iload_1
	bipush 10
	if_icmpge Label5
	iconst_1
	goto Label6
Label5:
	iconst_0
Label6:
	ifle Label3
	getstatic MCClass.a I
	iconst_2
	imul
	putstatic MCClass.a I
Label2:
	iload_1
	iconst_1
	iadd
	istore_1
	goto Label4
Label3:
	getstatic MCClass.a I
	invokestatic io/putIntLn(I)V
Label1:
	return
.limit stack 4
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
