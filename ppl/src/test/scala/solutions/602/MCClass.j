.source MCClass.java
.class public MCClass
.super java.lang.Object

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
.var 1 is b [I from Label0 to Label1
	bipush 6
	newarray int
	astore_1
	aload_1
	invokestatic MCClass/foo()[I
	astore_1
	aload_1
	iconst_3
	iaload
	invokestatic io/putIntLn(I)V
Label1:
	return
.limit stack 5
.limit locals 2
.end method

.method public static foo()[I
Label0:
.var 0 is a [I from Label0 to Label1
	iconst_5
	newarray int
	astore_0
	aload_0
	iconst_3
	iconst_3
	iastore
	aload_0
	areturn
Label1:
	aconst_null
	areturn
.limit stack 5
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
