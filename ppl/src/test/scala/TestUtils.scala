import java.io.{PrintWriter,File}
import java.util.concurrent.{Executors,TimeUnit,TimeoutException}
import org.antlr.v4.runtime.ANTLRFileStream
import scala.io.Source

import mc.parser._
import org.scalatest.FunSuite

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


trait TestUtils extends Timed {
	val sepa = File.separator
	val testdir = "src" + sepa + "test" + sepa + "scala" + sepa + "testcases"
	val soldir = "src" + sepa + "test" + sepa + "scala" + sepa + "solutions"

	def makeSource(input: String, num: Int) = {
		val tmp = new PrintWriter(new File(s"$testdir$sepa$num.txt"))
		tmp.print(input)
		tmp.close
		new ANTLRFileStream(s"$testdir$sepa$num.txt")
	}

}


/*
class MyReporter extends Reporter {
	var count = 0
	val res = new PrintWriter(new File("kq.txt"))
	def apply(event:Event) = event match {
		case e:TestSucceeded => count = count + 1
		case e:RunCompleted => 	res.println(count);res.close
	}
}*/