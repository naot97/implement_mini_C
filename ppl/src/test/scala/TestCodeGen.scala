/*
*	CodeGenerator
*/
import java.io._
import java.net.{URL, URLClassLoader}

import mc.astgen._
import mc.codegen.CodeGenerator
import mc.parser._
import mc.utils._
import org.antlr.v4.runtime.{ANTLRFileStream, CommonTokenStream}
import jasmin.Main

import scala.io.Source

trait TestCodeGen extends TestUtils {
    val libdir = "lib"

    //val sepa = File.separator
    //val outdir = "src"+sepa+"test"+sepa+"scala"+sepa
    val resultFile = "result.txt"

    def test(infile:ANTLRFileStream,i:Int):Unit = {
        val lexer = new MCLexer(infile);
        val tokens = new CommonTokenStream(lexer);
        val parser = new MCParser(tokens);
        val progtree = parser.program()

        val astbuild = new ASTGeneration()
        val ast = astbuild.visit(progtree).asInstanceOf[Program]
        test(ast, i)
    }
    def test(ast:AST,i:Int) : Unit = {
        try {
            val outdir = soldir + sepa + i
            val flder = new File(outdir)
            val succ = flder.mkdir
            // Target Code Generation at folder outdir/i
            CodeGenerator.gen(ast, flder)
            // Assembler translates .j to .class
            Main.main(Array("-d",outdir,outdir+sepa+"MCClass.j"))

            // library io
            val f2 = new File(libdir)
            val url2 = f2.toURI.toURL
            // code generated
            val url = flder.toURI.toURL

            val urls = Array[URL](url,url2)
            // Create a new class loader with the directory
            val cl = new URLClassLoader(urls)
            // load io
            val cls1 = cl.loadClass("io")
            // load main class
            val cls = cl.loadClass("MCClass")
            // run => result is printed in console
            val runMain = cls.newInstance.asInstanceOf[{def main(args:Array[String]):Void} ]
            val ps_con = System.out

            System.setOut(new PrintStream(new FileOutputStream(new File(outdir+sepa+resultFile)),true))

            runMain.main(Array(""))

            System.setOut(ps_con)

            
        } catch {
            case e:Exception  => e.printStackTrace
        }
    }
    def checkCode(input:String,expect:String,num:Int): Boolean = {
        val source = makeSource(input,num)
        test(source,num)
        compare(expect,num)
    }
    def checkCode(input:AST,expect:String,num:Int): Boolean = {
        test(input,num)
        compare(expect,num)
    }
    def compare(expect:String,num:Int): Boolean = {
        val outdir = soldir + sepa + num
        val result = Source.fromFile(s"$outdir$sepa$resultFile").getLines.mkString("")
        if (expect == result) true else false
    }
}





