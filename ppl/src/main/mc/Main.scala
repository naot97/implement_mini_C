

/**
 * @author nhphung
 */
package mc

import java.io.{File,IOException}
import java.net.URL
import java.net.MalformedURLException
import java.net.URLClassLoader
import java.util.concurrent.{Executors, TimeUnit}

import jasmin.Main

import org.antlr.v4.runtime.ANTLRFileStream
import org.antlr.v4.runtime.CommonTokenStream

import org.antlr.v4.gui.TestRig

import mc.parser._
import mc.astgen._
import mc.checker._
import mc.codegen._
import mc.utils._


trait Timed {
  def timeoutAfter(timeout: Long)(codeToTest: => Unit): Unit = {
    val executor = Executors.newSingleThreadExecutor
    val future = executor.submit(new Runnable {
      def run = codeToTest
    })

    try {
      future.get(timeout, TimeUnit.MILLISECONDS)
    }
    finally {
      executor.shutdown()
    }
  }
}

object Compiler extends  Timed {
  
  val sepa = File.separator // dung cho linux

  val testdir = "src"+sepa+"test"+sepa+"scala"+sepa

  def main(args: Array[String]): Unit = {
    if (args.length > 0) {
      val option = args(0).drop(1)
      try {
        option match {

          case "draw" => {
            //scala mc.Compiler -draw [<start rule> [<input file>]]
            val defaultrule = "program"
            val language = "mc.parser.MC"
            val opt= "-gui"
            val rigargs = args.length match { 
              case 3 => Array(language,args(1),opt,args(2))
              case 2 => Array(language,args(1),opt)
              case 1 => Array(language,defaultrule,opt)
            }
      
            val testrig =  new TestRig(rigargs) 
                       
            testrig.process()
          }
          case _ => throw new RuntimeException("Unknow option "+ option)
        }
        


      } catch {
        case e: RuntimeException => println(e.getMessage())
        case e: IOException => println(e.getMessage());
      }
    } else
      println("Usage: scala mc.Compiler <option> [<start rule> [<inputfile>]]")
  }
  

}