.source MCClass.java
.class public MCClass
.super java.lang.Object
.field static a [I

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
	getstatic MCClass.a [I
	iconst_0
	iconst_5
	iconst_5
	imul
	bipush 10
	iadd
	iastore
	getstatic MCClass.a [I
	iconst_1
	getstatic MCClass.a [I
	getstatic MCClass.a [I
	iconst_2
	iaload
	iaload
	iastore
	getstatic MCClass.a [I
	iconst_1
	iaload
	invokestatic io/putIntLn(I)V
Label1:
	return
.limit stack 6
.limit locals 1
.end method

.method public static <clinit>()V
Label0:
	iconst_3
	newarray int
	putstatic MCClass.a [I
Label1:
	return
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
