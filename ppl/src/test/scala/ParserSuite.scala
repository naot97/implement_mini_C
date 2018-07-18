import org.scalatest.FunSuite

/**
  * Created by nhphung on 4/28/17.
  */
class ParserSuite  extends FunSuite with TestParser {

  test("a simple program") {
    val input = "int main () {}"
    val expect = "sucessful"
    assert(checkRec(input,expect,101))
  }
  test("more complex program") {
    val input ="""int main () {
	putIntLn(4);
}"""
    val expect ="sucessful"
    assert(checkRec(input,expect,102))
  }
  test("wrong program"){
    val input = "} int main {"
    val expect = "Error on line 1 col 1: }"
    assert(checkRec(input,expect,103))
  }

}