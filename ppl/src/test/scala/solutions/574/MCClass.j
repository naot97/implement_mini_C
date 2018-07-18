.source MCClass.java
.class public MCClass
.super java.lang.Object
.field static a Z

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
.var 1 is b I from Label0 to Label1
	iconst_0
	istore_1
Label2:
Label4:
	iload_1
	iconst_1
	iadd
	istore_1
	iload_1
	bipush 6
	if_icmpge Label6
	iconst_1
	goto Label7
Label6:
	iconst_0
Label7:
	ifgt Label4
Label5:
	iload_1
	iconst_2
	isub
	istore_1
	getstatic MCClass.a Z
	ifgt Label2
Label3:
	iload_1
	invokestatic io/putIntLn(I)V
	getstatic MCClass.a Z
	invokestatic io/putBoolLn(Z)V
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
