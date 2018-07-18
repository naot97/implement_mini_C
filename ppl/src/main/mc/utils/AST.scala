package mc.utils

/**
 * @author nhphung
 */


trait AST {
        def accept(v: Visitor, param: Any) =  v.visit(this,param)
}

case class Program(val decl: List[Decl]) extends AST {
        override def toString = "Program(" + decl.mkString("List(",",",")") + ")"
        override def accept(v: Visitor, param: Any) = v.visitProgram(this,param)
}


trait Decl extends AST 
case class VarDecl(val variable: Id, val varType: Type) extends Decl {
        override def toString = "VarDecl(" + variable + "," + varType + ")"
        override def accept(v: Visitor, param: Any) = v.visitVarDecl(this,param)
}
case class FuncDecl(val name: Id, val param: List[VarDecl], val returnType: Type,val body: Stmt) extends Decl {
        override def toString = "FuncDecl(" + name + "," + param.mkString("List(",",",")")  + "," +  returnType +"," + body + ")"
        override def accept(v: Visitor, param: Any) = v.visitFuncDecl(this,param)
}

/*        TYPE        */
trait Type extends AST
object IntType extends Type {
        override def toString = "IntType"
        override def accept(v: Visitor, param: Any) = v.visitIntType(this,param)
}
object FloatType extends Type {
        override def toString = "FloatType"
        override def accept(v: Visitor, param: Any) = v.visitFloatType(this,param)
}
object BoolType extends Type {
        override def toString = "BoolType"
        override def accept(v: Visitor, param: Any) = v.visitBoolType(this,param)
}
object StringType extends Type {
        override def toString = "StringType"
        override def accept(v: Visitor, param: Any) = v.visitStringType(this,param)
}
object VoidType extends Type {
        override def toString = "VoidType"
        override def accept(v: Visitor, param: Any) = v.visitVoidType(this,param)
}
case class ArrayType(val dimen: IntLiteral, val eleType: Type) extends Type {
        override def toString = "ArrayType(" + dimen + "," + eleType + ")"
        override def accept(v: Visitor, param: Any) = v.visitArrayType(this,param)
}
case class ArrayPointerType(val eleType:Type) extends Type {
        override def toString = "ArrayPointerType(" + eleType + ")"
        override def accept(v: Visitor, param: Any) = v.visitArrayPointerType(this,param)
}
/*        expr        */
trait Expr extends Stmt 
case class BinaryOp(op: String, left: Expr, right: Expr) extends Expr {
        override def toString = "BinaryOp(" + op + "," + left + "," + right + ")"
        override def accept(v: Visitor, param: Any) = v.visitBinaryOp(this,param)
}
case class UnaryOp(op: String, body: Expr) extends Expr {
        override def toString = "UnaryOp(" + op + "," + body + ")"
        override def accept(v: Visitor, param: Any) = v.visitUnaryOp(this,param)
}

case class CallExpr(val method: Id, val params: List[Expr]) extends Expr {
        override def toString = "CallExpr("  + method + "," + params.mkString("List(",",",")")+ ")"
        override def accept(v: Visitor, param: Any) = v.visitCallExpr(this,param)
}


//LHS ------------------------
trait LHS extends Expr
case class Id(val name: String) extends LHS {
        override def toString = "Id(" + name + ")"
        override def accept(v: Visitor, param: Any) = v.visitId(this,param)
}
// element access
case class ArrayCell(arr: Expr, idx: Expr) extends LHS {
        override def toString = "ArrayCell(" + arr + "," + idx + ")"
        override def accept(v: Visitor, param: Any) = v.visitArrayCell(this,param)
}

/*        STMT        */
trait Stmt extends AST
case class Block(val decl: List[Decl], val stmt: List[Stmt]) extends Stmt {
        override def toString = "Block(" +  decl.mkString("List(",",",")") + "," + stmt.mkString("List(",",",")") + ")"
        override def accept(v: Visitor, param: Any) = v.visitBlock(this,param)
}

case class If(val expr: Expr, val thenStmt: Stmt, val elseStmt: Option[Stmt]) extends Stmt {
        override def toString = "If(" + expr + "," + thenStmt + (if (elseStmt == None) ",None" else  ",Some(" + elseStmt.get +")") + ")"
        override def accept(v: Visitor, param: Any) = v.visitIf(this,param)
}

case class For(val expr1:Expr,  val expr2: Expr, val expr3: Expr, val loop: Stmt) extends Stmt {
        override def toString = "For("  + expr1 + "," + expr2 + "," + expr3 + "," + loop + ")"
        override def accept(v: Visitor, param: Any) = v.visitFor(this,param)
}

object Break extends Stmt {
        override def toString = "Break"
        override def accept(v: Visitor, param: Any) = v.visitBreak(this,param)
}
object Continue extends Stmt {
        override def toString = "Continue"
        override def accept(v: Visitor, param: Any) = v.visitContinue(this,param)
}
case class Return(val expr: Option[Expr]) extends Stmt {
        override def toString = "Return(" + (if (expr == None) "None" else "Some("+expr.get+")") + ")"
        override def accept(v: Visitor, param: Any) = v.visitReturn(this,param)
}
case class Dowhile(val sl:List[Stmt],val exp:Expr) extends Stmt {
        override def toString = "Dowhile("+sl.mkString("List(",",",")")+","+ exp+")"
        override def accept(v: Visitor, param: Any) = v.visitDowhile(this,param)
}


/*        LITERAL        */
trait Literal extends Expr
case class IntLiteral(val value: Int) extends Literal {
        override def toString = "IntLiteral(" + value + ")"
        override def accept(v: Visitor, param: Any) = v.visitIntLiteral(this,param)
}
case class FloatLiteral(val value: Float) extends Literal {
        override def toString = "FloatLiteral(" + value + ")"
        override def accept(v: Visitor, param: Any) = v.visitFloatLiteral(this,param)
}
case class StringLiteral(val value: String) extends Literal {
        override def toString = "StringLiteral(" + value + ")"
        override def accept(v: Visitor, param: Any) = v.visitStringLiteral(this,param)

}
case class BooleanLiteral(val value: Boolean) extends Literal {
        override def toString = "BooleanLiteral(" + value + ")"
        override def accept(v: Visitor, param: Any) = v.visitBooleanLiteral(this,param)
}


