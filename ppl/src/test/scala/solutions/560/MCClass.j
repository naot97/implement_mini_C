.source MCClass.java
.class public MCClass
.super java.lang.Object
.field static a I

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
	invokestatic MCClass/foo()I
	invokestatic io/putInt(I)V
Label1:
	return
.limit stack 2
.limit locals 1
.end method

.method public static foo()I
Label0:
	iconst_5
	ireturn
	getstatic MCClass.a I
	invokestatic io/putInt(I)V
Label1:
	iconst_0
	ireturn
.limit stack 2
.limit locals 0
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
