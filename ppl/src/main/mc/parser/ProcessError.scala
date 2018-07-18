

/**
 * @author nhphung
 */
package mc.parser
import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

trait ProcessError {
    
    def createErrorListener() = new BaseErrorListener() {
          override  def syntaxError(arg0:Recognizer[_, _] , obj: Any, line:Int, position:Int,message:String, ex:RecognitionException ) {
              throw new IllegalArgumentException(s"Error on line $line col ${position + 1}: ${obj.asInstanceOf[Token].getText}");
            }           
        }
}

object NeverHappen extends RuntimeException
class ErrorToken(val s:String) extends RuntimeException {
  override def getMessage: String = "Error Token "+ s
}
class UncloseString(val s:String) extends RuntimeException {
  override def getMessage: String = "Unclosed String: "+ s
}
class IllegalEscape(val s:String) extends RuntimeException {
  override def getMessage: String = "Illegal Escape In String: "+ s
}

