.source MCClass.java
.class public MCClass
.super java.lang.Object
.field static x I

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
.var 1 is a [I from Label0 to Label1
	iconst_5
	newarray int
	astore_1
.var 2 is b [I from Label0 to Label1
	iconst_4
	newarray int
	astore_2
	iconst_2
	invokestatic MCClass/foo(I)[I
	iconst_3
	getstatic MCClass.x I
	iadd
	aload_1
	aload_2
	iconst_2
	iaload
	iaload
	iconst_3
	iadd
	iastore
Label1:
	return
.limit stack 8
.limit locals 3
.end method

.method public static foo(I)[I
Label0:
.var 0 is d I from Label0 to Label1
.var 1 is a [I from Label0 to Label1
	iconst_5
	newarray int
	astore_1
	aload_1
	iload_0
	iconst_5
	iastore
	aload_1
	areturn
Label1:
	aconst_null
	areturn
.limit stack 5
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
