.source MCClass.java
.class public MCClass
.super java.lang.Object

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
.var 1 is a I from Label0 to Label1
.var 2 is b [I from Label0 to Label1
	bipush 10
	newarray int
	astore_2
Label2:
.var 3 is c [I from Label2 to Label3
	iconst_3
	newarray int
	astore_3
.var 4 is d I from Label2 to Label3
	aload_3
	iconst_1
	iconst_1
	iastore
	aload_3
	iconst_1
	iaload
	invokestatic io/putIntLn(I)V
	iconst_2
	iconst_4
	if_icmpgt Label4
	iconst_1
	goto Label5
Label4:
	iconst_0
Label5:
Label3:
Label1:
	return
.limit stack 6
.limit locals 5
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
