/**
 *	@author Nguyen Hua Phung
 *	@version 1.0
 *	23/10/2015
 * 	This file provides a simple version of code generator
 *
 */

package mc.codegen
import mc.utils._
import org.antlr.v4.runtime.ANTLRFileStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree._
import scala.collection.JavaConverters._

//trait Val

case class Symbol(name : String,typ : Type, value : Val)
case class FunctionType(input:List[Type],output:Type) extends Type



import java.io.{PrintWriter, File}
//case class Symbol(name : String,typ : Type, value : Val)
//case class FunctionType(input:List[Type],output:Type) extends Type

object CodeGenerator extends Utils {
  val libName = "io"
  def init() = 
    List(
          Symbol("getInt", FunctionType(List(), IntType), CName(libName)),
          Symbol("putInt", FunctionType(List(IntType), VoidType) , CName(libName)),
          Symbol("putIntLn", FunctionType(List(IntType), VoidType) , CName(libName)),
          Symbol("getFloat", FunctionType( List(),FloatType) , CName(libName)),
          Symbol("putFloat", FunctionType(List(FloatType), VoidType) , CName(libName)),
          Symbol("putFloatLn", FunctionType(List(FloatType), VoidType) , CName(libName)),
          Symbol("putBool", FunctionType(List(BoolType), VoidType) , CName(libName)),
          Symbol("putBoolLn", FunctionType(List(BoolType), VoidType) , CName(libName)),
          Symbol("putString", FunctionType(List(StringType), VoidType) , CName(libName)),
          Symbol("putStringLn", FunctionType(List(StringType), VoidType) , CName(libName)),
          Symbol("putLn", FunctionType(List(), VoidType) , CName(libName))
        )

	def gen(ast:AST,dir:File) = {
    	val gl = init()     
		val gc = new CodeGenVisitor(ast,gl,dir) ;   
		gc.visit(ast, null);   
	}
}

case class ClassType(cname:String) extends Type

case class SubBody(frame:Frame,sym:List[Symbol]) 
case class Access(val frame:Frame,val sym:List[Symbol],val isLeft:Boolean)
 //case class Access(frame : Frame, sym : List[Symbol], isLeft : Boolean)   

trait Val
  case class Index(value:Int) extends Val
  case class CName(value:String) extends Val


class CodeGenVisitor(astTree:AST,env:List[Symbol],dir:File) extends BaseVisitor with Utils {
	
  val className = "MCClass"
  val path = dir.getPath()
  val emit = new Emitter(path+"/"+className+".j")
  var bufferInInit : String = ""
  var frameOfClinit = new Frame("<clinit>",VoidType)
   override def visitProgram(ast:Program,c:Any): Any = {
      emit.printout(emit.emitPROLOG(className, "java.lang.Object"))  
      val subtxt = ast.decl.foldLeft(SubBody(null,env))((e,x) => visit(x,e).asInstanceOf[SubBody]) 
      ast.decl.filter(_.isInstanceOf[FuncDecl]).asInstanceOf[List[FuncDecl]].
      map(x => genMETHOD(x,subtxt.sym,new Frame(x.name.name, x.returnType)))
      if (bufferInInit != "") genMETHOD(FuncDecl(Id("<clinit>"),List(),VoidType,Block(List(),List())),env,frameOfClinit)
      genMETHOD(FuncDecl(Id("<init>"),List(),null,Block(List(),List())),env,new Frame("<init>",VoidType))
      emit.emitEPILOG()
  }

  
  def genMETHOD(ast:FuncDecl,o:Any,frame:Frame) : Any= {
    val isInit = ast.returnType == null
    val isMain = ast.name.name == "main" && ast.param.length == 0 && ast.returnType == VoidType
    val isClinit = ast.name.name == "<clinit>"
    val returnType = if (isInit) VoidType else ast.returnType
    val methodName = if (isInit) "<init>" else ast.name.name
    val intype = if (isMain) List(ArrayPointerType(StringType)) else ast.param.map(_.varType)
    val funcType =  FunctionType(intype, returnType)

    frame.push()
    emit.printout(emit.emitMETHOD(methodName, funcType, !isInit, frame))

    frame.enterScope(false);
    
    val glenv = o.asInstanceOf[List[Symbol]]
    val body = ast.body.asInstanceOf[Block]
    emit.printout(emit.emitLABEL(frame.getStartLabel(),frame))
   	// sinh code cho para va vardecla
    if (isInit) emit.printout(emit.emitVAR(frame.getNewIndex,"this",ClassType(className),frame.getStartLabel,frame.getEndLabel,frame))
    if (isMain) emit.printout(emit.emitVAR(frame.getNewIndex,"args",ArrayPointerType(StringType),frame.getStartLabel,frame.getEndLabel,frame))
    val newGlenv = ast.param.foldLeft(glenv)( (a,b) => {  		
    	emit.printout (emit.emitVAR(frame.getNewIndex, b.variable.name, b.varType ,frame.getStartLabel,frame.getEndLabel,frame))
  		Symbol(b.variable.name, b.varType,Index(frame.getCurrIndex() - 1)) :: a
	})

    //sinh code cho statement
    if (isInit) {
      emit.printout(emit.emitREADVAR("this",ClassType(className),0,frame))
      emit.printout(emit.emitINVOKESPECIAL(frame))
    }
    if (isClinit)
    	emit.printout(bufferInInit)

	val subtxt =  SubBody( frame,newGlenv)
	val subtxt2 = body.decl.foldLeft(subtxt)((a,b) => visit(b,a).asInstanceOf[SubBody])
  	body.stmt.map(visit(_,subtxt2))

    emit.printout(emit.emitLABEL(frame.getEndLabel(),frame))
    if (returnType != VoidType) 
 	{
 		val temp = returnType match {
 			case IntType => emit.emitPUSHICONST(0 , frame)
 			case FloatType => emit.emitPUSHFCONST("0.0", frame)
 			case BoolType => emit.emitPUSHICONST(0 , frame)
 			case _ => emit.emitPUSHNULL(frame)	
 		}
 		emit.printout(temp)
 	}
 	emit.printout(emit.emitRETURN(returnType,frame));

    emit.printout(emit.emitENDMETHOD(frame));
    frame.exitScope(); 
  }

  override def visitFuncDecl(ast:FuncDecl,o:Any): Any = {
    val subctxt = o.asInstanceOf[SubBody]
    SubBody(null,Symbol(ast.name.name,FunctionType(ast.param.map(_.varType),ast.returnType),CName(className))::subctxt.sym)
  }
  
  override def visitVarDecl(ast : VarDecl, o : Any): Any = {
  	val subtxt = o.asInstanceOf[SubBody]
  	val glenv = subtxt.sym
  	val frame = subtxt.frame
  	val isArr = ast.varType.isInstanceOf[ArrayType]
  	if (frame == null) {
  		emit.printout(emit.emitSTATIC(ast.variable.name, ast.varType))
  		val sym = Symbol(ast.variable.name, ast.varType, CName(className))
  		if (isArr) 
  		{
  			bufferInInit = bufferInInit + emit.emitINITARRAY(sym,ast.varType.asInstanceOf[ArrayType],frameOfClinit) 
  		}
  		SubBody(frame,sym :: glenv)
  	}
  	else {
  		emit.printout ( emit.emitVAR(frame.getNewIndex, ast.variable.name, ast.varType ,frame.getStartLabel,frame.getEndLabel,frame))
  		val sym = Symbol(ast.variable.name, ast.varType, Index(frame.getCurrIndex() - 1))
  		if (isArr) 
  			emit.printout(emit.emitINITARRAY(sym,ast.varType.asInstanceOf[ArrayType],frame))
  		SubBody(frame,sym :: glenv)
  	}
  }

  def getResource(o : Any) : Any= {
  	if (o.isInstanceOf[SubBody]) {
  		val temp1 = o.asInstanceOf[SubBody]
  		(true,temp1.frame, temp1.sym, false)
  	}
  	else {
  		val temp2 = o.asInstanceOf[Access]
  		(false,temp2.frame,temp2.sym,false)
  	}
  }

  override def visitBinaryOp(ast: BinaryOp, o: Any) : Any = {
  	val reso = getResource(o).asInstanceOf[(Boolean,Frame,List[Symbol],Boolean)]
  	val isStmt = reso._1
  	val frame = reso._2
  	val glenv = reso._3
  	val access = Access(frame,glenv,false)
  	
  	val left = if (ast.op == "=") 
  		ast.left.accept(this,Access(frame,glenv,true)).asInstanceOf[(String,Type)]
  	else 
  		ast.left.accept(this,access).asInstanceOf[(String,Type)]

  	val leftBuffer = left._1
  	var leftType = left._2

  	val right = ast.right.accept(this,access).asInstanceOf[(String,Type)]
  	val rightBuffer = right._1
  	var rightType = right._2
  	var buffer = (leftType,rightType) match {
  		case (IntType,FloatType)  => {
  			leftType =  FloatType
  			leftBuffer + emit.emitI2F(frame)+rightBuffer
  		}             ///sai chua sua
  		case (FloatType,IntType)  => {	
  			rightType = FloatType
  			leftBuffer +rightBuffer + emit.emitI2F(frame)
  		}
  		case (BoolType, BoolType) => {
  			val rightLaBel = frame.getNewLabel
  			val outLaBel = frame.getNewLabel
  			if (ast.op == "&&")
  				leftBuffer + 
  				emit.emitIFTRUE(rightLaBel, frame) + 
  				emit.emitPUSHICONST("false",frame)+ 
  				emit.emitGOTO(outLaBel,frame)+
  				emit.emitLABEL(rightLaBel, frame) +
  				rightBuffer +
  				emit.emitLABEL(outLaBel,frame)
  			else if (ast.op == "||")
  				leftBuffer + 
  				emit.emitIFFALSE(rightLaBel, frame) + 
  				emit.emitPUSHICONST("true",frame) +
  				emit.emitGOTO(outLaBel,frame)+
  				emit.emitLABEL(rightLaBel, frame) +
  				rightBuffer +
  				emit.emitLABEL(outLaBel,frame)
  			else 
  				leftBuffer+rightBuffer
  		}
  		case (_,_) => {
  			leftBuffer+rightBuffer
  		}
  	}

  	val result = 
  		(ast.op match {
  			case "+" | "-" =>  (emit.emitADDOP(ast.op,leftType,frame),leftType)
  			case "*" | "/" =>  (emit.emitMULOP(ast.op,leftType,frame),leftType)
  			case "%" 	   =>  (emit.emitMOD(frame),IntType)
  			case "&&" 	   =>  ("",BoolType)
  			case "||"	   =>  ("",BoolType)
  			case "="	   =>  
  				(if (ast.left.isInstanceOf[ArrayCell]) emit.emitASTORE(leftType,frame)
  				else {
  					val name = ast.left.asInstanceOf[Id].name
  					val sym = lookup(name,glenv ,(x:Symbol)=>x.name).get
  					emit.emitSTORE(sym, frame)
  				} ,leftType)	
  			case _         => (emit.emitREOP(ast.op,leftType,frame),BoolType)
  		}).asInstanceOf[(String,Type)]
  	buffer = buffer + result._1
  	val resultTyp : Type = result._2
  	if (isStmt) emit.printout(buffer)
  		else if (ast.op == "=") buffer = buffer + ast.left.accept(this,access).asInstanceOf[(String,Type)]._1
  	(buffer,resultTyp)
  }

  override def visitUnaryOp(ast: UnaryOp, o: Any): Any = {
  	  	if (o.isInstanceOf[Access]){

  	val temp = ast.body.accept(this,o).asInstanceOf[(String,Type)]
  	val resultTyp = temp._2
  	val frame = o.asInstanceOf[Access].frame
  	var buffer = temp._1
  	buffer = buffer + {if (ast.op == "-") emit.emitNEGOP(resultTyp, frame)
  	else emit.emitNOT(resultTyp,frame)}
  	(buffer, resultTyp)	
  }
  }


  override def visitBlock(ast : Block, o : Any): Any = {
  	val subtxt = o.asInstanceOf[SubBody]
  	val frame = subtxt.frame
  	frame.enterScope(false)
  	emit.printout(emit.emitLABEL(frame.getStartLabel(),frame))
  	val subtxt2 = ast.decl.foldLeft(subtxt)((a,b) => visit(b,a).asInstanceOf[SubBody])
  	ast.stmt.map(visit(_,subtxt2))
    emit.printout(emit.emitLABEL(frame.getEndLabel(),frame))
  	frame.exitScope()
  }

  override def visitCallExpr(ast:CallExpr,o:Any): Any = {
  	val reso = getResource(o).asInstanceOf[(Boolean,Frame,List[Symbol],Boolean)]
  	val isStmt = reso._1
    //val ctxt = if(isStmt) o.asInstanceOf[SubBody] else o.asInstanceOf[Access]
    val frame = reso._2
    val nenv = reso._3
    val sym = lookup(ast.method.name,nenv,(x:Symbol)=>x.name).get
    val cname = sym.value.asInstanceOf[CName].value
    val ctype = sym.typ.asInstanceOf[FunctionType]
    val listIn = ctype.input 
    val resultType = ctype.output
    var count = -1
    val access = Access(frame,nenv,false)

    var buffer = ast.params.foldLeft("")((b,a) => {
    	 val temp = a.accept(this,access).asInstanceOf[(String,Type)]
    	 val str = b + temp._1
    	 count = count + 1
    	 if (listIn.lift(count).get == FloatType && temp._2 == IntType )
    	 	str + emit.emitI2F(frame)
    	 else	
    	 	str
    	}
    	 )
    buffer = buffer + emit.emitINVOKESTATIC(cname+"/"+ast.method.name,ctype,frame)
    if (isStmt) emit.printout(buffer)
    (buffer,resultType)
  }

  override def visitId(ast : Id, o : Any): Any = {
  	  	if (o.isInstanceOf[Access]){
  	val ctxt = o.asInstanceOf[Access]
    val frame = ctxt.frame
    val nenv = ctxt.sym
    val isLeft = ctxt.isLeft
    val sym = lookup(ast.name,nenv ,(x:Symbol)=>x.name).get
    val value = sym.value
    val isLocal = value.isInstanceOf[Index]
    val isArr = sym.typ.isInstanceOf[ArrayType] | sym.typ.isInstanceOf[ArrayPointerType]
    val buffer = (isLeft,isLocal,isArr) match {
    	case (true,true,false) => ""
    	case (true,true,true) => emit.emitREADVAR(sym.name, sym.typ ,value.asInstanceOf[Index].value,frame)
    	case (false,true,_) => emit.emitREADVAR(sym.name, sym.typ ,value.asInstanceOf[Index].value,frame)
    	case (true,false,false) =>  ""
    	case (true,false,true) => emit.emitGETSTATIC(value.asInstanceOf[CName].value+"."+sym.name,sym.typ, frame)
    	case (false,false,_) => emit.emitGETSTATIC(value.asInstanceOf[CName].value+"."+sym.name,sym.typ, frame)
    }
    //emit.printout(buffer)
  	(buffer,sym.typ)
  }
  }

  override def visitArrayCell(ast : ArrayCell, o : Any): Any = {
  	if (o.isInstanceOf[Access]){
  	val subtxt = o.asInstanceOf[Access]
  	val isLeft = subtxt.isLeft
  	val frame = subtxt.frame
  	val glenv = subtxt.sym
  	val temp1 = ast.arr.accept(this,o).asInstanceOf[(String,Type)]
  	var buffer = temp1._1
  	val arrType = temp1._2
  	val resultTyp = if (arrType.isInstanceOf[ArrayType]) arrType.asInstanceOf[ArrayType].eleType
  	else arrType.asInstanceOf[ArrayPointerType].eleType  	
  	buffer = buffer + ast.idx.accept(this, Access(frame,glenv, false)).asInstanceOf[(String,Type)]._1
  	buffer = buffer + {if (isLeft == false) emit.emitALOAD(resultTyp,frame) else ""} 
  	(buffer,resultTyp)
  }
  }

  override def visitIf(ast: If, o: Any) : Any= {
  	val subtxt = o.asInstanceOf[SubBody]
  	val frame = subtxt.frame
  	val lenv = subtxt.sym
  	val access = Access(frame,lenv,false) 
  	val els = ast.elseStmt
  	emit.printout(ast.expr.accept(this,access).asInstanceOf[(String,Type)]._1)
  	if (els.isEmpty){
  		val label2 = frame.getNewLabel()
  		emit.printout(emit.emitIFFALSE(label2, frame))
  		ast.thenStmt.accept(this, o)
  		emit.printout(emit.emitLABEL(label2, frame))
  	}
  	else {
  		val label1 = frame.getNewLabel()
    	val label2 = frame.getNewLabel()
    	emit.printout(emit.emitIFFALSE(label1, frame))
    	ast.thenStmt.accept(this, o)
    	emit.printout(emit.emitGOTO(label2, frame))
    	emit.printout(emit.emitLABEL(label1, frame))
    	ast.elseStmt.get.accept(this,o)
    	emit.printout(emit.emitLABEL(label2, frame))
  	}
 }

 override def visitDowhile(ast: Dowhile, o : Any): Any = {
    val ctxt = o.asInstanceOf[SubBody]
    val frame = ctxt.frame
  	val lenv = ctxt.sym
  	val access = Access(frame,lenv,false) 
    frame.enterLoop()
    val break = frame.getBreakLabel()
    val continue = frame.getContinueLabel()
    emit.printout(emit.emitLABEL(continue, frame))
    ast.sl.map(_.accept(this,o))
    emit.printout(ast.exp.accept(this,access).asInstanceOf[(String,Type)]._1)
    emit.printout(emit.emitIFTRUE(continue, frame))
    emit.printout(emit.emitLABEL(break, frame))
    frame.exitLoop()
  }

  override def visitFor(ast: For, o : Any): Any = {
  	val ctxt = o.asInstanceOf[SubBody]
    val frame = ctxt.frame
    val lenv = ctxt.sym
  	val access = Access(frame,lenv,false) 
    frame.enterLoop
    val break = frame.getBreakLabel()
    val continue = frame.getContinueLabel()
    val loopLabel = frame.getNewLabel()
 	ast.expr1.accept(this,o).asInstanceOf[(String,Type)]._1
 	emit.printout( emit.emitLABEL(loopLabel,frame))
 	emit.printout(ast.expr2.accept(this,access).asInstanceOf[(String,Type)]._1)
 	emit.printout(emit.emitIFFALSE(frame.getBreakLabel(), frame))
 	ast.loop.accept(this,o)
 	emit.printout(emit.emitLABEL(continue, frame))
 	ast.expr3.accept(this,o).asInstanceOf[(String,Type)]._1
 	emit.printout(emit.emitGOTO(loopLabel, frame))
    emit.printout(emit.emitLABEL(break, frame))
    frame.exitLoop
  }

  override def visitBreak(ast: Break.type, o: Any) : Any= {
  	val ctxt = o.asInstanceOf[SubBody]
  	val frame = ctxt.frame
  	emit.printout(emit.emitGOTO(frame.getBreakLabel(), frame))
  }

  override def visitContinue(ast: Continue.type, o: Any) : Any= {
  	val ctxt = o.asInstanceOf[SubBody]
  	val frame = ctxt.frame
  	emit.printout(emit.emitGOTO(frame.getContinueLabel(), frame))
  }

  override def visitReturn(ast: Return, o: Any) : Any= {
  	val ctxt = o.asInstanceOf[SubBody]
  	val frame = ctxt.frame
  	val exp = ast.expr
  	val lenv = ctxt.sym
  	val resultTyp = frame.returnType
  	if (!exp.isEmpty) {
  		val temp = exp.get.accept(this, Access(frame,lenv,false)).asInstanceOf[(String,Type)]
  		emit.printout(temp._1)
  		if(resultTyp == FloatType && temp._2 == IntType )
  			emit.printout(emit.emitI2F(frame))
  	}
  	emit.printout(emit.emitRETURN(resultTyp,frame))
  }

  override def visitIntLiteral(ast:IntLiteral,o:Any) : Any= {
  	  	if (o.isInstanceOf[Access]){
    val ctxt = o.asInstanceOf[Access]
    val frame = ctxt.frame
    (emit.emitPUSHICONST(ast.value, frame),IntType)
	}
  }

  override def visitFloatLiteral(ast:FloatLiteral, o:Any) : Any= {
  	  	if (o.isInstanceOf[Access]){
    val ctxt = o.asInstanceOf[Access]
    val frame = ctxt.frame
    (emit.emitPUSHFCONST(ast.value.toString(), frame),FloatType)
	}
  }
  
   override def visitStringLiteral(ast:StringLiteral,o:Any) : Any= {
   	  	if (o.isInstanceOf[Access]){
    val ctxt = o.asInstanceOf[Access]
    val frame = ctxt.frame
    val str = ast.value.toString()
    (emit.emitPUSHCONST( "\"" +str+"\"", StringType, frame),StringType)
	}
    
  }

   override def visitBooleanLiteral(ast: BooleanLiteral, o: Any) : Any = {
   	  	if (o.isInstanceOf[Access]){
   	val ctxt = o.asInstanceOf[Access]
    val frame = ctxt.frame
    (emit.emitPUSHICONST(ast.value.toString, frame),BoolType)
   }}
}