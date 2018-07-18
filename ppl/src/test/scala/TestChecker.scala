/**
 * @author nhphung
 */


import java.io.{File, PrintWriter}
import java.util.concurrent.TimeoutException

import mc.astgen._
import mc.checker._
import mc.parser._
import mc.utils._
import org.antlr.v4.runtime.{ANTLRFileStream, CommonTokenStream}

import scala.io.Source


trait TestChecker extends TestUtils {
  def astGen(infile:ANTLRFileStream) = {
    val lexer = new MCLexer(infile);
    val tokens = new CommonTokenStream(lexer);
    val parser = new MCParser(tokens);
    val progtree = parser.program()
    
    val astbuild = new ASTGeneration()
    astbuild.visit(progtree).asInstanceOf[Program]
  }
  def test(ast:AST,outfile:PrintWriter) = {
    val checker = new StaticChecker(ast)
    try {
        checker.check()
    } catch {
      case Undeclared(k, n)  => outfile.println("Undeclared "+k+": "+n)
      case Redeclared(k, n)  => outfile.println("Redeclared "+k+": "+n)
      case TypeMismatchInExpression(exp) => outfile.println("Type Mismatch In Expression: "+exp)
      case TypeMismatchInStatement(stmt) => outfile.println("Type Mismatch In Statement: "+stmt)
      case FunctionNotReturn(m) => outfile.println("Function Not Return: "+m)
      case BreakNotInLoop => outfile.println("Break Not In Loop")
      case ContinueNotInLoop => outfile.println("Continue Not In Loop")
      case UnreachableStatement(stmt) => outfile.println("Unreachable Statement: "+stmt)
    }
  }
  
  // the common checking
  def check(input:AST,expected:String,num:Int) = {
    val dest = new PrintWriter(new File(s"$soldir$sepa$num.txt"))

    try {
      timeoutAfter(500) {
          test(input, dest)
      }
    }
    catch {
      case te: TimeoutException => dest.println("Test runs timeout")
    }
    finally {
      dest.close
    }
    val result = Source.fromFile(s"$soldir$sepa$num.txt").getLines.mkString("")   
    if (result == expected) true else false
  }
  // the first entry: input by AST
  def checkAst(input:AST,expected:String,num:Int) = {
    val source = makeSource(input.toString,num)
    check(input,expected,num)
  }
  // the second entry: input by String
  def checkCkr(input:String,expected:String,num:Int) = {
    val source = makeSource(input,num)
    val ast = astGen(source)
    check(ast,expected,num)
  }
}