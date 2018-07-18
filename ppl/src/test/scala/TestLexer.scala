

/**
 * @author nhphung
 */
import java.io.{File, PrintWriter}


import org.antlr.v4.runtime.{ANTLRFileStream, Token}
import java.io.{PrintWriter,File}
import java.util.concurrent.{Executors,TimeUnit,TimeoutException}
import org.antlr.v4.runtime.ANTLRFileStream
import scala.io.Source

import mc.parser._
import org.scalatest.FunSuite

trait TestLexer extends ProcessError with TestUtils {
  type Lexer = MCLexer

  def test(infile:ANTLRFileStream,outfile:PrintWriter) = {
      val lexer = new Lexer(infile);
      
      try {
        printLexeme(lexer,outfile)
      }
      catch {
        case et:ErrorToken => outfile.println("ErrorToken "+et.s)
        case ie:IllegalEscape => outfile.println("Illegal escape in string: "+ie.s)
        case us:UncloseString => outfile.println("Unclosed string: "+ us.s)
        
      }

  }
  def checkLex(input:String,expected:String,num:Int) = {
    val source = makeSource(input,num)
    val dest = new PrintWriter(new File(s"$soldir$sepa$num.txt"))
    try {
      timeoutAfter(600) {
        test(source,dest)
      }
    }
    catch {
      case te: TimeoutException => dest.println("Test runs timeout")
    }
    finally {
      dest.close
    }
    val result = Source.fromFile(s"$soldir$sepa$num.txt").getLines.mkString(",")
    if (result == expected) true else false
  }

  def printAtt(lexer:Lexer,dev:PrintWriter,prn:Token=>String):Unit = {
    
      val tok = lexer.nextToken()
      if (tok.getType() != Token.EOF) {
        dev.println(prn(tok))
        printAtt(lexer,dev,prn)
      } else dev.print(prn(tok))
    
    
  }
  
  
  def printLexeme(lexer:Lexer,dev:PrintWriter) = printAtt(lexer,dev,_.getText())
  
  def printToken(lexer:Lexer,dev:PrintWriter) = printAtt(lexer,dev,x => lexer.getVocabulary.getSymbolicName(x.getType()))
  
  def printAll(lexer:Lexer,dev:PrintWriter) = printAtt(lexer,dev,x => x.getText() +"\t"+ lexer.getVocabulary.getSymbolicName(x.getType()))
  

}