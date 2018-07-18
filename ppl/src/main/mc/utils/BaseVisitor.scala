package mc.utils

/**
 * @author nhphung
 */
class BaseVisitor extends Visitor {
  def visitProgram(ast: Program, c: Any): Any = null
  def visitVarDecl(ast: VarDecl, c: Any): Any = null
  def visitFuncDecl(ast: FuncDecl, c: Any): Any = null
  def visitIntType(ast: IntType.type, c: Any): Any = null 
  def visitFloatType(ast: FloatType.type, c: Any): Any = null
  def visitBoolType(ast: BoolType.type, c: Any): Any = null
  def visitStringType(ast: StringType.type, c: Any): Any = null
  def visitVoidType(ast: VoidType.type, c: Any): Any = null
  def visitArrayType(ast: ArrayType, c: Any): Any = null
  def visitArrayPointerType(ast:ArrayPointerType, c: Any): Any = null
  def visitBinaryOp(ast: BinaryOp, c: Any): Any = null
  def visitUnaryOp(ast: UnaryOp, c: Any): Any = null
  def visitCallExpr(ast: CallExpr, c: Any): Any = null
  def visitId(ast: Id, c: Any): Any = null
  def visitArrayCell(ast: ArrayCell, c: Any): Any = null
  def visitBlock(ast: Block, c: Any): Any = null
  def visitIf(ast: If, c: Any): Any = null
  def visitFor(ast: For, c: Any): Any = null
  def visitBreak(ast: Break.type, c: Any): Any = null
  def visitContinue(ast: Continue.type, c: Any): Any = null
  def visitReturn(ast: Return, c: Any): Any = null
  def visitDowhile(ast: Dowhile, c:Any): Any = null
  def visitIntLiteral(ast: IntLiteral, c: Any): Any = null
  def visitFloatLiteral(ast: FloatLiteral, c: Any): Any = null
  def visitStringLiteral(ast: StringLiteral, c: Any): Any = null
  def visitBooleanLiteral(ast: BooleanLiteral, c: Any): Any = null
}
