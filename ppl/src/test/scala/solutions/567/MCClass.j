.source MCClass.java
.class public MCClass
.super java.lang.Object
.field static a I

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
	iconst_1
	iconst_2
	invokestatic MCClass/foo(II)I
	invokestatic io/putInt(I)V
Label1:
	return
.limit stack 3
.limit locals 1
.end method

.method public static foo(II)I
Label0:
.var 0 is a I from Label0 to Label1
.var 1 is b I from Label0 to Label1
	iload_0
	iload_1
	if_icmpne Label2
	iconst_1
	goto Label3
Label2:
	iconst_0
Label3:
	ifle Label4
	iconst_1
	ireturn
	goto Label5
Label4:
	iconst_0
	ireturn
Label5:
Label1:
	iconst_0
	ireturn
.limit stack 3
.limit locals 2
.end method

.method public static foo2(II)[Ljava/lang/String;
Label0:
.var 0 is a I from Label0 to Label1
.var 1 is b I from Label0 to Label1
Label1:
	aconst_null
	areturn
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
