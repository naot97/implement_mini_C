package mc.astgen
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import java.io.{PrintWriter,File}
import org.antlr.v4.runtime.ANTLRFileStream
import mc.utils._
import scala.collection.JavaConverters._
import org.antlr.v4.runtime.tree._
import mc.parser._
import mc.parser.MCParser._

class ASTGeneration extends MCBaseVisitor[Any] {

  override def visitProgram(ctx : ProgramContext) = 
	 Program(ctx.decla.asScala.toList.map(decla => decla.accept(this).asInstanceOf[List[Decl]]).flatten) 

  override def visitDecla(ctx : DeclaContext) =
    if (ctx.vardecla != null) ctx.vardecla.accept(this)
    else List(ctx.funcdecla.accept(this))

  override def visitSubvar(ctx : SubvarContext) = 
  	VarDecl(
      Id( ctx.ID.getText),
      if (ctx.getChildCount == 1)
  		 ctx.getParent.asInstanceOf[VardeclaContext].pritype.accept(this).asInstanceOf[Type] 
  	 else 
  		 ArrayType(IntLiteral(ctx.INT.getText.toInt),
  		ctx.getParent.asInstanceOf[VardeclaContext].pritype.accept(this).asInstanceOf[Type])
    )

  override def visitVardecla(ctx : VardeclaContext)  = 
    ctx.subvar.asScala.toList.map(subvar => subvar.accept(this))

  override def visitParadecla(ctx : ParadeclaContext) = 
  	if (ctx.getChildCount == 2)
  		VarDecl(
        Id(ctx.ID.getText),
  		  ctx.pritype.accept(this).asInstanceOf[Type]
      )
  	else 
  		VarDecl(
        Id(ctx.ID.getText),
  		  ArrayPointerType(ctx.pritype.accept(this).asInstanceOf[Type])
      )
  
  override def visitParalistdecla(ctx : ParalistdeclaContext) = 
    ctx.paradecla.asScala.toList.map(paradecla => paradecla.accept(this))

  override def visitFuncdecla(ctx : FuncdeclaContext) = {
  	val name         = Id(ctx.ID.getText)
  	val params       = ctx.paralistdecla.accept(this).asInstanceOf[List[VarDecl]]
  	val returnType   = ctx.functype.accept(this).asInstanceOf[Type]
  	val body : Stmt  = ctx.blocksta.accept(this).asInstanceOf[Stmt]
  	FuncDecl(name, params, returnType, body)
  }

  override def visitBlocksta(ctx : BlockstaContext) = 
  	Block(
      ctx.vardecla.asScala.toList.map(vardecla => vardecla.accept(this).asInstanceOf[List[VarDecl]]).flatten,  // list vardecla
      ctx.sta.asScala.toList.map(sta => sta.accept(this).asInstanceOf[Stmt])                      // list statement
    )
  

  override def visitSta(ctx : StaContext) = 
  	ctx.getChild(0).accept(this)

  override def visitIfsta(ctx : IfstaContext) = 
 	  If(
      ctx.exp.accept(this).asInstanceOf[Expr],
      ctx.sta(0).accept(this).asInstanceOf[Stmt],
      if (ctx.sta(1) != null) Some(ctx.sta(1).accept(this).asInstanceOf[Stmt]) 
      else None
    )

  //dowhilesta : DO (sta)+ WHILE  exp SEMI;
  override def visitDowhilesta(ctx : DowhilestaContext) = 
  	Dowhile(
      ctx.sta.asScala.toList.map(sta => sta.accept(this).asInstanceOf[Stmt]),
      ctx.exp.accept(this).asInstanceOf[Expr]
    )
  
  //forsta : FOR '(' exp SEMI exp SEMI exp ')' sta;
  override def visitForsta(ctx : ForstaContext) = 
  	For(
      ctx.exp(0).accept(this).asInstanceOf[Expr],
      ctx.exp(1).accept(this).asInstanceOf[Expr],
      ctx.exp(2).accept(this).asInstanceOf[Expr],
      ctx.sta.accept(this).asInstanceOf[Stmt]
    )
  
  override def visitBreaksta(ctx : BreakstaContext) = Break

  override def visitContista(ctx : ContistaContext) = Continue

  override def visitReturnsta(ctx : ReturnstaContext) = 
  	Return (
      if (ctx.exp != null) Some(ctx.exp.accept(this).asInstanceOf[Expr]) 
      else None
    )
  
  override def visitExpsta(ctx : ExpstaContext) = ctx.exp.accept(this).asInstanceOf[Expr]
  // BinaryOp ; left OP right
  def getBinaryOp(ctx : ParserRuleContext) = {
  	val left = ctx.getChild(0).accept(this).asInstanceOf[Expr]
  	val right = ctx.getChild(2).accept(this).asInstanceOf[Expr]
  	val op = ctx.getChild(1).getText
  	BinaryOp(op, left, right)
  }

  override def visitExp(ctx : ExpContext) = 
  	if (ctx.getChildCount == 1) ctx.exp1.accept(this)
  	else getBinaryOp(ctx)
  
  override def visitExp1(ctx : Exp1Context) = 
  	if (ctx.getChildCount == 1) ctx.exp2.accept(this)
  	else getBinaryOp(ctx)
  
  override def visitExp2(ctx : Exp2Context) = 
  	if (ctx.getChildCount == 1) ctx.getChild(0).accept(this)
	  else getBinaryOp(ctx)	  		
  
  override def visitExp3(ctx : Exp3Context) = 
  	if (ctx.getChildCount == 1) ctx.getChild(0).accept(this)
  	else getBinaryOp(ctx)
  
  override def visitExp4(ctx : Exp4Context) = 
  	if (ctx.getChildCount == 1) ctx.getChild(0).accept(this)
  	else getBinaryOp(ctx)	
  
  override def visitExp5(ctx : Exp5Context) = 
  	if (ctx.getChildCount == 1) ctx.getChild(0).accept(this)
  	else getBinaryOp(ctx)	
  
  override def visitExp6(ctx : Exp6Context) = 
  	if (ctx.getChildCount == 1) ctx.getChild(0).accept(this)
  	else getBinaryOp(ctx)
  
  override def visitExp7(ctx : Exp7Context) = 
  	if (ctx.getChildCount == 1)
  		ctx.getChild(0).accept(this)
  	else {
  		val op = ctx.getChild(0).getText
  		val expr = ctx.getChild(1).accept(this).asInstanceOf[Expr]
  		UnaryOp(op, expr)
  	}
  
  override def visitExp8(ctx : Exp8Context) = 
  	if (ctx.getChildCount == 1)
  		ctx.getChild(0).accept(this)
  	else {
  		val arr = ctx.exp9.accept(this).asInstanceOf[Expr]
  		val index = ctx.exp.accept(this).asInstanceOf[Expr]
  		ArrayCell(arr, index)
  	}
  
  override def visitExp9(ctx : Exp9Context) = 
  	if (ctx.getChildCount == 1) ctx.getChild(0).accept(this)
  	else ctx.exp.accept(this)
  
  override def visitOperand(ctx : OperandContext) = {
  	if (ctx.ID != null) Id(ctx.ID.getText)
  	else ctx.getChild(0).accept(this)
  }

  override def visitFuncall(ctx : FuncallContext) = 
  	CallExpr(
      Id(ctx.ID.getText), 
      ctx.listexp.accept(this).asInstanceOf[List[Expr]]
    )
  
  override def visitListexp(ctx : ListexpContext) = 
    ctx.exp().asScala.toList.map(x => x.accept(this))  

  override def visitLiteral(ctx : LiteralContext) = 
  	if (ctx.INT != null) IntLiteral(ctx.INT.getText.toInt)
  	else if (ctx.FLOAT != null) FloatLiteral(ctx.FLOAT.getText.toFloat)
  		   else if (ctx.STRING != null) StringLiteral(ctx.STRING.getText)
  		        else BooleanLiteral(ctx.BOOLEAN.getText.toBoolean) 

  override def visitFunctype(ctx : FunctypeContext) = {
  	if (ctx.pritype != null) ctx.pritype.accept(this)
  	else if(ctx.arrpointertype != null) ctx.arrpointertype.accept(this)
  		   else VoidType
  } 

  override def visitArrpointertype(ctx : ArrpointertypeContext) = 
  	ArrayPointerType(ctx.pritype.accept(this).asInstanceOf[Type])

  override def visitPritype(ctx : PritypeContext) = {
  	if (ctx.INTTYPE != null) IntType
  	else if (ctx.FLOATTYPE != null) FloatType
  		   else if (ctx.BOOLTYPE != null) BoolType
  		 	      else StringType
  }
}