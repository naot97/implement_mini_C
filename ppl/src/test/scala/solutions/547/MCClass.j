.source MCClass.java
.class public MCClass
.super java.lang.Object

.method public static foo(Ljava/lang/String;)Ljava/lang/String;
Label0:
.var 0 is str Ljava/lang/String; from Label0 to Label1
	invokestatic MCClass/goo()Ljava/lang/String;
	areturn
Label1:
	aconst_null
	areturn
.limit stack 2
.limit locals 1
.end method

.method public static goo()Ljava/lang/String;
Label0:
	ldc "abc"
	areturn
Label1:
	aconst_null
	areturn
.limit stack 2
.limit locals 0
.end method

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
	ldc "bcd"
	invokestatic MCClass/foo(Ljava/lang/String;)Ljava/lang/String;
	invokestatic io/putString(Ljava/lang/String;)V
Label1:
	return
.limit stack 2
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
