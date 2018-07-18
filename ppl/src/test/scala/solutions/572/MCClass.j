.source MCClass.java
.class public MCClass
.super java.lang.Object

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
.var 1 is a Z from Label0 to Label1
	iconst_1
	iconst_2
	if_icmpge Label2
	iconst_1
	goto Label3
Label2:
	iconst_0
Label3:
	ifle Label6
	iconst_1
	goto Label7
Label6:
	iconst_2
	iconst_3
	if_icmple Label4
	iconst_1
	goto Label5
Label4:
	iconst_0
Label5:
Label7:
	ifle Label10
	iconst_1
	goto Label11
Label10:
	iconst_3
	iconst_4
	if_icmpne Label8
	iconst_1
	goto Label9
Label8:
	iconst_0
Label9:
Label11:
	istore_1
	iload_1
	invokestatic io/putBoolLn(Z)V
Label1:
	return
.limit stack 7
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
