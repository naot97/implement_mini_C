.source MCClass.java
.class public MCClass
.super java.lang.Object

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
.var 1 is a I from Label0 to Label1
.var 2 is i I from Label0 to Label1
	iconst_1
	istore_2
	bipush 10
	istore_1
Label4:
	iload_1
	iconst_0
	if_icmple Label5
	iconst_1
	goto Label6
Label5:
	iconst_0
Label6:
	ifle Label3
Label7:
	iload_1
	iconst_4
	if_icmpne Label9
	iconst_1
	goto Label10
Label9:
	iconst_0
Label10:
	ifle Label11
	goto Label2
Label11:
	iload_2
	iconst_2
	imul
	istore_2
Label8:
Label2:
	iload_1
	iconst_1
	isub
	istore_1
	goto Label4
Label3:
	iload_2
	invokestatic io/putIntLn(I)V
Label1:
	return
.limit stack 5
.limit locals 3
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
