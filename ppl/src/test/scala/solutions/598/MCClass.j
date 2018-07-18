.source MCClass.java
.class public MCClass
.super java.lang.Object

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
	invokestatic MCClass/foo()I
Label1:
	return
.limit stack 2
.limit locals 1
.end method

.method public static foo()I
Label0:
.var 0 is a I from Label0 to Label1
	iconst_1
	istore_0
Label2:
Label4:
	iload_0
	iconst_1
	iadd
	istore_0
	iload_0
	bipush 10
	if_icmple Label6
	iconst_1
	goto Label7
Label6:
	iconst_0
Label7:
	ifle Label8
	goto Label3
Label8:
	sipush 999
	ireturn
Label5:
	iload_0
	bipush 100
	if_icmpge Label9
	iconst_1
	goto Label10
Label9:
	iconst_0
Label10:
	ifgt Label2
Label3:
Label1:
	iconst_0
	ireturn
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
