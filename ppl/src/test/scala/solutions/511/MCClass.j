.source MCClass.java
.class public MCClass
.super java.lang.Object
.field static a I

.method public static main([Ljava/lang/String;)V
Label0:
.var 0 is args [Ljava/lang/String; from Label0 to Label1
	iconst_1
	putstatic MCClass.a I
	getstatic MCClass.a I
	iconst_0
	if_icmplt Label2
	iconst_1
	goto Label3
Label2:
	iconst_0
Label3:
	ifle Label4
	getstatic MCClass.a I
	iconst_5
	if_icmple Label6
	iconst_1
	goto Label7
Label6:
	iconst_0
Label7:
	ifle Label8
	ldc "lon hon 5"
	invokestatic io/putString(Ljava/lang/String;)V
	goto Label9
Label8:
	ldc "be hon 5 lon hon -1"
	invokestatic io/putString(Ljava/lang/String;)V
Label9:
	goto Label5
Label4:
	getstatic MCClass.a I
	iconst_5
	ineg
	if_icmpge Label10
	iconst_1
	goto Label11
Label10:
	iconst_0
Label11:
	ifle Label12
	ldc "be hon -5"
	invokestatic io/putString(Ljava/lang/String;)V
	goto Label13
Label12:
	ldc "lon hon -5 be hon 0"
	invokestatic io/putString(Ljava/lang/String;)V
Label13:
Label5:
Label1:
	return
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
