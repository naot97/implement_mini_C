
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import java.io.{File, PrintWriter}
import java.util.concurrent.TimeoutException

import org.antlr.v4.runtime.ANTLRFileStream
import mc.utils._

import scala.collection.JavaConverters._
import org.antlr.v4.runtime.tree._
import mc.astgen._
import mc.parser._

import scala.io.Source


/**
 * @author nhphung
 */
trait TestAst extends TestUtils {

  def test(infile:ANTLRFileStream,outfile:PrintWriter) = {
      val lexer = new MCLexer(infile);
      val tokens = new CommonTokenStream(lexer);

      val parser = new MCParser(tokens);
      val progtree =  parser.program()

      val astbuild = new ASTGeneration()
      val ast = astbuild.visit(progtree)

      outfile.print(ast)

  }
  def checkAst(input:String,expected:AST,num:Int) = {
    val source = makeSource(input,num)
    val dest = new PrintWriter(new File(s"$soldir$sepa$num.txt"))

    try {
        timeoutAfter(500) {
          try {
            test(source, dest)
          } catch {
            case e: Exception => dest.println(e.getMessage)
          }
        }
    }
    catch {
      case te: TimeoutException => dest.println("Test runs timeout")
    }
    finally {
      dest.close
    }
    val result = Source.fromFile(s"$soldir$sepa$num.txt").getLines.mkString("")
    //val astgen = new AstRebuild
    //val ast = astgen.generate(result)
    //println(expected.toString)
    if (result == expected.toString) true else false
  }

}




