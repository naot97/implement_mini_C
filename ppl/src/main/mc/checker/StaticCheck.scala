
// 1513539

package mc.checker
/**
 * @author nhphung
 */

import mc.parser._
import mc.utils._
import java.io.{File, PrintWriter}

import mc.codegen.Val
import org.antlr.v4.runtime.ANTLRFileStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree._

import scala.collection.JavaConverters._

trait Val

case class Symbol(name : String,typ : Type, value : Val)
case class FunctionType(input:List[Type],output:Type) extends Type


class StaticChecker(ast:AST) extends BaseVisitor {

	var currentFunction : FuncDecl = null
    def check() = 
    	visit(
    		ast, 
    		List(
    			Symbol("getInt", FunctionType(List(), IntType), null),
    			Symbol("putInt", FunctionType(List(IntType), VoidType) , null),
    			Symbol("putIntLn", FunctionType(List(IntType), VoidType) , null),
    			Symbol("getFloat", FunctionType( List(),FloatType) , null),
    			Symbol("putFloat", FunctionType(List(FloatType), VoidType) , null),
    			Symbol("putFloatLn", FunctionType(List(FloatType), VoidType) , null),
    			Symbol("putBool", FunctionType(List(BoolType), VoidType) , null),
    			Symbol("putBoolLn", FunctionType(List(BoolType), VoidType) , null),
    			Symbol("putString", FunctionType(List(StringType), VoidType) , null),
    			Symbol("putStringLn", FunctionType(List(StringType), VoidType) , null),
    			Symbol("putLn", FunctionType(List(IntType), VoidType) , null)
    		)
    	)

    def find[T](name:String,lst:List[T],func:T=>String):Option[T] = 
    	lst match {
    		case List() => None
    		case head::tail => if (name == func(head)) Some(head) else find(name,tail,func)	
		}

	def getSymbolOfDecl(x : Decl)  = 
    	if (x.isInstanceOf[VarDecl]) Symbol(x.asInstanceOf[VarDecl].variable.name, x.asInstanceOf[VarDecl].varType, null)
    	else 
    	{
    		val temp = x.asInstanceOf[FuncDecl]
    		Symbol(temp.name.name,FunctionType( temp.param.map( _.varType ) , temp.returnType ), null)
    	}

    def lookupToInsert(x : Decl, lst : List[Symbol], k : Kind ) : List[Symbol] = {
    		val temp : Symbol = getSymbolOfDecl(x)
			if (find[Symbol](temp.name, lst, _.name).isEmpty) temp :: lst
			else throw Redeclared(k, temp.name)
		}
		
	def checkParaOrReturnType(x : Type, y : Type) : Boolean =
		(x,y) match {
			case (FloatType,  FloatType | IntType )  => true
			case (IntType,IntType)   => true
			case (BoolType, BoolType)  => true
			case (StringType, StringType) => true
			case  (p1 : ArrayPointerType, p2 : ArrayPointerType  ) =>
				x.asInstanceOf[ArrayPointerType].eleType ==
				y.asInstanceOf[ArrayPointerType].eleType  
			case  (p : ArrayPointerType, a : ArrayType  ) => 
				x.asInstanceOf[ArrayPointerType].eleType ==
				y.asInstanceOf[ArrayType].eleType 
			case (_,_) => false 
			}

	override def visitIntLiteral(ast: IntLiteral, c: Any): Type = IntType
	override def visitFloatLiteral(ast: FloatLiteral, c: Any): Type = FloatType
  	override def visitStringLiteral(ast: StringLiteral, c: Any): Type = StringType
  	override def visitBooleanLiteral(ast: BooleanLiteral, c: Any): Type = BoolType

	override def visitBinaryOp(ast : BinaryOp, c: Any) : Type =
		ast.op match {
			case "+" | "-" | "*" | "/" =>  
				(ast.left.accept(this, c) , ast.right.accept(this, c)) match {
					case (IntType,FloatType) => FloatType
					case (FloatType,IntType) => FloatType
					case (IntType,IntType) =>	 IntType
					case (FloatType,FloatType) => FloatType
					case (_ , _)  => throw TypeMismatchInExpression(ast)
			}
			case "<" | ">" | "<=" | ">=" => 
				(ast.left.accept(this, c) , ast.right.accept(this, c)) match {
					case (IntType | FloatType,IntType | FloatType) => BoolType
					case (_, _) => throw TypeMismatchInExpression(ast)
			}
			case "==" | "!=" => 
				(ast.left.accept(this, c) , ast.right.accept(this, c)) match {
					case (IntType,IntType)  => BoolType
					case (BoolType,BoolType)  => BoolType
					case (_, _) 	=> throw TypeMismatchInExpression(ast)	
			}
			case "=" => {
				(ast.left.accept(this, c) , ast.right.accept(this, c)) match {
					case (FloatType,  FloatType | IntType )  => FloatType
					case (IntType,IntType)   => IntType
					case (BoolType, BoolType)  => BoolType
					case (StringType, StringType) => StringType
					case (_, _) => throw TypeMismatchInExpression(ast) 
				}
			}
			case  "&&" | "||" => 
				(ast.left.accept(this, c) , ast.right.accept(this, c)) match {
					case (BoolType,BoolType) => BoolType
					case ( _,_)  => throw TypeMismatchInExpression(ast) 
			}
			case "%" => 
				(ast.left.accept(this, c) , ast.right.accept(this, c)) match {
					case (IntType, IntType) => IntType
					case (_, _) => throw TypeMismatchInExpression(ast)  
			}
			case _ => throw TypeMismatchInExpression(ast)
		}

	override def visitUnaryOp(ast: UnaryOp, c: Any): Type =
		ast.op match {
			case "-" =>
				ast.body.accept(this, c) match {
					case IntType => IntType
					case FloatType => FloatType 
					case _ => throw TypeMismatchInExpression(ast)
				}
			case "!" => 
				ast.body.accept(this, c) match {
					case BoolType => BoolType
					case _ => throw TypeMismatchInExpression(ast) 
				}
		}

	override def visitCallExpr(ast: CallExpr, c: Any): Type = {
		val temp = find[Symbol](ast.method.name , c.asInstanceOf[List[Symbol]] , _.name)
		if (temp.isEmpty) throw Undeclared(Function, ast.method.name)
		else if (!temp.get.typ.isInstanceOf[FunctionType]) throw Undeclared(Function, ast.method.name)
			else if (temp.get.typ.asInstanceOf[FunctionType].input.length != ast.params.length) 
				throw TypeMismatchInExpression(ast)
			else {
				val zipListType = temp.get.typ.asInstanceOf[FunctionType].input.
				zip(ast.params.map(_.accept(this,c)).asInstanceOf[List[Type]])
				if ( zipListType.exists( b => checkParaOrReturnType(b._1, b._2) == false) ) 
						throw TypeMismatchInExpression(ast)
				else temp.get.typ.asInstanceOf[FunctionType].output
			}
	}

	override def visitId(ast: Id, c: Any): Type = {
		val temp = find[Symbol](ast.name, c.asInstanceOf[List[Symbol]], _.name)
		if (temp.isEmpty) throw Undeclared(Identifier, ast.name)
		else if (temp.get.typ.isInstanceOf[FunctionType]) 
			throw Undeclared(Identifier, ast.name)
			else temp.get.typ
	}


	override def visitArrayCell(ast: ArrayCell, c: Any) : Type = {
		val arr =  ast.arr.accept(this,c)
		val index = ast.idx.accept(this,c)
		(arr, index) match {
			case ( p : ArrayPointerType , IntType) => 
				arr.asInstanceOf[ArrayPointerType].eleType
			case ( a : ArrayType , IntType) => 
				arr.asInstanceOf[ArrayType].eleType
			case (_,_) => throw TypeMismatchInExpression(ast)
		}
	}

    override def visitProgram(ast: Program, c: Any) {
    	val listSymb = ast.decl.foldLeft(c.asInstanceOf[List[Symbol]] ) ((a,b) => 
    					lookupToInsert(b , a, if (b.isInstanceOf[VarDecl]) Variable else Function) )
		ast.decl.map(_.accept(this, listSymb  ))
    }

    override def visitFuncDecl(ast : FuncDecl, c: Any){
    	val listPara = ast.param.foldLeft(List[Symbol]()) ( (a,b) => lookupToInsert(b, a, Parameter))
    	ast.body.asInstanceOf[Block].decl.map( lookupToInsert(_,listPara,Variable))	
    	this.currentFunction = ast
    	val newC = listPara ::: c.asInstanceOf[List[Symbol]]
    	ast.body.accept(this, newC)
	}

	override def visitReturn(ast: Return, c: Any): Any = {
		val funcType = currentFunction.returnType
		if (ast.expr.isEmpty){ 
			if (funcType != VoidType) throw TypeMismatchInStatement(ast)
		}
		else if (checkParaOrReturnType(funcType, ast.expr.get.accept(this,c).asInstanceOf[Type]) == false)
			 throw TypeMismatchInStatement(ast)
	} 

	override def visitBlock (ast : Block , c : Any) : Any = {
		val listSymb = ast.decl.foldLeft(List[Symbol]()) ( (a,b) => lookupToInsert(b,a, Variable))
		val newC = listSymb ::: c.asInstanceOf[List[Symbol]]
		ast.stmt.map( _.accept(this, newC))
	}

	override def visitIf(ast : If, c: Any) : Any = {
		if ( ast.expr.accept(this,c) != BoolType) throw TypeMismatchInStatement(ast)
		ast.thenStmt.accept(this,c)
		if (ast.elseStmt.isEmpty == false) ast.elseStmt.get.accept(this,c) 
	}

	override def visitFor(ast: For, c: Any) : Any = {
		if (ast.expr1.accept(this,c) == IntType &&
		ast.expr2.accept(this,c) ==  BoolType &&
		ast.expr3.accept(this,c) == IntType) 
			ast.loop.accept(this, c)
		else throw TypeMismatchInStatement(ast)
	}

	override def visitDowhile(ast:Dowhile, c:Any) : Any = {
		if (ast.exp.accept(this, c) == BoolType){
			ast.sl.map(_.accept(this,c))
		}
		else throw TypeMismatchInStatement(ast)
	}
}
