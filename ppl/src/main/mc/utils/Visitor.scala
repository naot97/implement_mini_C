package mc.utils

/**
 * @author nhphung
 */
trait Visitor {
  def visit(ast: AST, c: Any): Any = ast.accept(this,c)
  def visitProgram(ast: Program, c: Any): Any
  def visitVarDecl(ast: VarDecl, c: Any): Any
  def visitFuncDecl(ast: FuncDecl, c: Any): Any
  def visitIntType(ast: IntType.type, c: Any): Any 
  def visitFloatType(ast: FloatType.type, c: Any): Any
  def visitBoolType(ast: BoolType.type, c: Any): Any
  def visitStringType(ast: StringType.type, c: Any): Any
  def visitVoidType(ast: VoidType.type, c: Any): Any
  def visitArrayType(ast: ArrayType, c: Any): Any
  def visitArrayPointerType(ast:ArrayPointerType, c: Any): Any
  def visitBinaryOp(ast: BinaryOp, c: Any): Any
  def visitUnaryOp(ast: UnaryOp, c: Any): Any
  def visitCallExpr(ast: CallExpr, c: Any): Any
  def visitId(ast: Id, c: Any): Any
  def visitArrayCell(ast: ArrayCell, c: Any): Any
  def visitBlock(ast: Block, c: Any): Any
  def visitIf(ast: If, c: Any): Any
  def visitFor(ast: For, c: Any): Any
  def visitBreak(ast: Break.type, c: Any): Any
  def visitContinue(ast: Continue.type, c: Any): Any
  def visitReturn(ast: Return, c: Any): Any
  def visitDowhile(ast:Dowhile, c:Any):Any
  def visitIntLiteral(ast: IntLiteral, c: Any): Any
  def visitFloatLiteral(ast: FloatLiteral, c: Any): Any
  def visitStringLiteral(ast: StringLiteral, c: Any): Any
  def visitBooleanLiteral(ast: BooleanLiteral, c: Any): Any
 
}
