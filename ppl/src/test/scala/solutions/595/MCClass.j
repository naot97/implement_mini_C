.source MCClass.java
.class public MCClass
.super java.lang.Object

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
.var 1 is str Ljava/lang/String; from Label0 to Label1
	ldc "hcmut-1513539"
	astore_1
	aload_1
	invokestatic io/putStringLn(Ljava/lang/String;)V
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
