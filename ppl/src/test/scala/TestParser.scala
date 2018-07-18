

/**
 * @author nhphung
 */
import java.io.{File, PrintWriter}
import java.util.concurrent.TimeoutException

import mc.parser.{MCLexer, MCParser, ProcessError}
import org.antlr.v4.runtime.{ANTLRFileStream, CommonTokenStream}

import scala.io.Source




//import ProgramContext;

trait TestParser extends ProcessError with TestUtils {
  type Lexer = MCLexer
  type Parser = MCParser 



    /**
     * Parses an expression into an integer result.
     * @param expression the expression to part
     * @return and integer result
     */

    def test(fileName:ANTLRFileStream,outFile:PrintWriter) = {
        /*
         * Create a lexer that reads from our expression string
         */
        val lexer = new Lexer(fileName); // ANTLRInputStream
        val _listener = createErrorListener();

        val tokens = new CommonTokenStream(lexer);

        val parser = new Parser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(_listener);
        try {
          parser.program();
          outFile.println("sucessful");
        } catch {
          case e:Exception => outFile.println(e.getMessage());
        }
    }

  def checkRec(input:String,expected:String,num:Int) = {
    val source = makeSource(input,num)
    val dest = new PrintWriter(new File(s"$soldir$sepa$num.txt"))
    try {
      timeoutAfter(1000) {
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
    //println(result)
    if (result == expected) true else false
  }

}