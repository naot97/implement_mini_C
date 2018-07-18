.source MCClass.java
.class public MCClass
.super java.lang.Object
.field static a [Ljava/lang/String;
.field static b [Z

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
	getstatic MCClass.a [Ljava/lang/String;
	iconst_4
	aaload
	invokestatic io/putString(Ljava/lang/String;)V
	getstatic MCClass.b [Z
	iconst_3
	baload
	invokestatic io/putBoolLn(Z)V
Label1:
	return
.limit stack 3
.limit locals 1
.end method

.method public static <clinit>()V
Label0:
	iconst_5
	anewarray java/lang/String
	putstatic MCClass.a [Ljava/lang/String;
	iconst_4
	newarray boolean
	putstatic MCClass.b [Z
Label1:
	return
.limit stack 3
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
