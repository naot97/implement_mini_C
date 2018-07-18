.source MCClass.java
.class public MCClass
.super java.lang.Object
.field static a Z

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
.var 1 is b I from Label0 to Label1
Label2:
	iconst_2
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
