//update: 22/07/2014

package mc.checker
import mc.utils._

trait Kind

case object Function extends Kind {
	override def toString = "Function"
}

case object Parameter extends Kind {
	override def toString = "Parameter"
}
case object Variable extends Kind {
	override def toString = "Variable"
}
case object Identifier extends Kind {
	override def toString = "Identifier"
}


/* These exception classes are used to throw when an error is detected */

/* 
//    k: the kind of the redeclared identifnier; it must be one of the four above kinds
//    n: the name of the redeclared identifier; 
*/
case class Undeclared(val k: Kind,val  n: String) extends RuntimeException {
	override def getMessage: String = "Undeclared "+ k + ": " + n
}
case class Redeclared(val k: Kind,val n: String) extends RuntimeException {
	override def getMessage: String = "Redeclared "+ k + ": " + n
}
case class TypeMismatchInExpression(val exp: Expr) extends RuntimeException {
	override def getMessage: String = "Type Mismatch In Expression: "+ exp
}
case class TypeMismatchInStatement(val stmt: Stmt) extends RuntimeException {
	override def getMessage: String = "Type Mismatch In Statement: "+ stmt
}
case class FunctionNotReturn(val m: String) extends RuntimeException {
	override def getMessage: String = "Function "+ m + "Not Return "
}
case object BreakNotInLoop extends RuntimeException {
	override def getMessage: String = "Break Not In Loop"
}
case object ContinueNotInLoop extends RuntimeException {
	override def getMessage: String = "Continue Not In Loop"
}
case class UnreachableStatement(val stmt: Stmt) extends RuntimeException {
	override def getMessage: String = "Unreachable Statement: "+stmt
}
