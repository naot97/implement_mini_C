.source MCClass.java
.class public MCClass
.super java.lang.Object

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
	invokestatic MCClass/toan()[I
	iconst_4
	iaload
	invokestatic io/putIntLn(I)V
Label1:
	return
.limit stack 3
.limit locals 1
.end method

.method public static toan()[I
Label0:
	invokestatic MCClass/foo()[I
	areturn
Label1:
	aconst_null
	areturn
.limit stack 2
.limit locals 0
.end method

.method public static foo()[I
Label0:
.var 0 is a [I from Label0 to Label1
	iconst_5
	newarray int
	astore_0
	aload_0
	iconst_4
	iconst_5
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
