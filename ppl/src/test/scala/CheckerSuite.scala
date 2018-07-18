import org.scalatest.FunSuite
import mc.checker._
import mc.utils._

/**
  * Created by nhphung on 4/29/17.
  */
class CheckerSuite extends FunSuite with TestChecker {
  test("1. Type Mismatch In Statement: If") {
    val input = "void main () {int x; if (2 + 3) x =1; } "
    val expected = """Type Mismatch In Statement: If(BinaryOp(+,IntLiteral(2),IntLiteral(3)),BinaryOp(=,Id(x),IntLiteral(1)),None)"""
    assert(checkCkr(input,expected,401))
  }
  test("2. Type Mismatch In Statement: If") {
    val input = "void main () {boolean x; int a; if (x) a = 1; } "
    val expected = ""
    assert(checkCkr(input,expected,402))
  }
  test("3. Type Mismatch In Expression: getInt") {
    val input = "void main () {getInt(3);}"
    val expected = "Type Mismatch In Expression: "+CallExpr(Id("getInt"),List(IntLiteral(3))).toString
    assert(checkCkr(input,expected,403))
  }
  test("4. Type Mismatch In Expression: putIntLn") {

    val input = "void main () {putIntLn();}"
    val expected = "Type Mismatch In Expression: "+CallExpr(Id("putIntLn"),List()).toString
    assert(checkCkr(input,expected,404))
  }
  test("5. Check with AST") {

    val input = Program(List(
      FuncDecl(Id("main"),List(),VoidType,
        Block(List(),
          List(CallExpr(Id("putIntLn"),List()))))))
    val expected = "Type Mismatch In Expression: "+CallExpr(Id("putIntLn"),List()).toString
    assert(checkAst(input,expected,405))
  }
  test("6. Check with AST putIntLn with float") {
    val input = Program(List(
      FuncDecl(Id("main"),List(),VoidType,
        Block(List(),
          List(CallExpr(Id("putIntLn"),List(FloatLiteral(1.2f))))))))
    val expected = "Type Mismatch In Expression: "+CallExpr(Id("putIntLn"),List(FloatLiteral(1.2f))).toString
    assert(checkAst(input,expected,406))
  }
  test("7. Type Mismatch In Statement: CallExpr") {

    val input = " int function(int a){} void main () {function(1,2);}"
    val expected = "Type Mismatch In Expression: CallExpr(Id(function),List(IntLiteral(1),IntLiteral(2)))"
    assert(checkCkr(input,expected,407))
  }
  test("8. Type Mismatch In Expression : Sub Expression") {

    val input = """ void main(int c){
       int a,b; b = ( true + a) + 1 ;
    }

    """
    val expected = """Type Mismatch In Expression: BinaryOp(+,BooleanLiteral(true),Id(a))"""

    assert(checkCkr(input,expected,408))
  }
  test("9. loi chua xac dinh") {

    val input = """ void main(int c){
       5 = 4;
    10.5 = 3;
    }

    """
    val expected = """"""

    assert(checkCkr(input,expected,409))
  }
  test("10. loi trong Unary") {

    val input = """ void print(){}
      int main(){
      boolean a;
      if(-a) print(); //Expr trong If: UnaryOp(-,Id(a))
  }

    """
    val expected = """Type Mismatch In Expression: UnaryOp(-,Id(a))"""

    assert(checkCkr(input,expected,410))
  }
  test("11. loi trong condition cua if") {

    val input = """   void sum (int a, int b) { if(a+b&&a>b) a+b;}
  

    """
    val expected = """Type Mismatch In Expression: BinaryOp(&&,BinaryOp(+,Id(a),Id(b)),BinaryOp(>,Id(a),Id(b)))"""

    assert(checkCkr(input,expected,411))
  }

  test("12. ko biet ghi gi") {

    val input = """   int main(int a){
    int a; //1
    {
    int a; //2
    }
      }
  
    """
    val expected = """Redeclared Variable: a"""

    assert(checkCkr(input,expected,412))
  }
  test("13. test return type 1") {

    val input = """  
    boolean a; 
    float main(int a){
      return a;
    }
    void b(int a){
      return ;
    }
  
    """
    val expected = """"""

    assert(checkCkr(input,expected,413))
  }
  test("14. test return type 2") {

    val input = """   
  
    """
    val expected = """"""

    assert(checkCkr(input,expected,414))
  }
  test("15. test cua bon no") {

    val input = """   void foo() {}
    void main() {
    int foo;
    foo();
}
      
    """
    val expected = """Undeclared Function: foo"""

    assert(checkCkr(input,expected,415))
  }
   test("16. test ve do while") {

    val input = """   void foo() {return;}
    void main() {
      int x,y,z;
      do 
        x = x + 1;
        x = x - 1;
      while (y) ;
    }
      
    """
    val expected = """Type Mismatch In Statement: Dowhile(List(BinaryOp(=,Id(x),BinaryOp(+,Id(x),IntLiteral(1))),BinaryOp(=,Id(x),BinaryOp(-,Id(x),IntLiteral(1)))),Id(y))"""

    assert(checkCkr(input,expected,416))
  }
  test("17. test ve do while") {
    val input = """   void foo() {return;}
    float main() {
      int x,y,z;
      {
        int a;string b;
        {
          float a;
          return a;
        }
      }
    }
      
    """
    val expected = """"""

    assert(checkCkr(input,expected,417))
  }
  test("18. test ve do while") {
    val input = """   void foo() {return;}
    boolean[] main() {
      int x,y,z;
      {
        int a;string b;
        {
          boolean a[10];
          return a;
        }
      }
    }
      
    """
    val expected = """"""

    assert(checkCkr(input,expected,418))
  }
  test("19. test ve do while") {
    val input = """   void foo() {return;}
    boolean[] main(boolean c[]) {
      int x,y,z;
      {
        int a;string b;
        {
          boolean a[10];
          return c;
        }
      }
    }
    """
    val expected = """"""
    assert(checkCkr(input,expected,419))
  }

  test("20. test Redecla Variable") {
    val input = """   
    int a,b,c;
    string d,e;
    int main(){}
    float b;
    """
    val expected = """Redeclared Variable: b"""
    assert(checkCkr(input,expected,420))
  }  
  test("21. test Redecla Variable 2") {
    val input = """   
    int a,b,c;
    string d,e;
    int main(){
       float b;
    }
    """
    val expected = """"""
    assert(checkCkr(input,expected,421))
  }  
  test("22. test Redecla Variable 3") {
    val input = """   
    int a,b,c;
    string d,e;
    int main(float b){
      string c;
    }
    """
    val expected = """"""
    assert(checkCkr(input,expected,422))
  } 

  test("23. test Redecla Variable 4") {
    val input = """   
    int a,b,c;
    string d,e;
    int main(float b){
      string b;
    }
    """
    val expected = """Redeclared Variable: b"""
    assert(checkCkr(input,expected,423))
  }

   test("24. test Redecla Variable 5") {
    val input = """   
    int a,b,c;
    string d,e;
    int main(float b){
      boolean main;
      {
        string b,a;
      }
    }
    """
    val expected = """"""
    assert(checkCkr(input,expected,424))
  }

  test("25. test Redecla Paraterment") {
    val input = """   
    int a,b,c;
    string d,e;
    int main(float b, string b){
      boolean main;
      {
        string b,a;
      }
    }
    """
    val expected = """Redeclared Parameter: b"""
    assert(checkCkr(input,expected,425))
  }

  test("26. test Redecla Function") {
    val input = """   
    int a,b,c;
    string d,e;
    int main(float b, string b){
      boolean main;
      {
        string b,a;
      }
    }

    boolean g,h,j,k;
    void main(){}
    """
    val expected = """Redeclared Function: main"""
    assert(checkCkr(input,expected,426))
  }

  test("27. test Undeclared Identifier") {
    val input = """   
    int a,b,c;
    string d,e;
    int main(float b){
      boolean main;
      {
        string b,a;
      }
      u = 2;
    }
    """
    val expected = """Undeclared Identifier: u"""
    assert(checkCkr(input,expected,427))
  }

  test("28. test Undeclared Identifier 2") {
    val input = """   
    int a,b,c;
    string d,e;
    int main(float b){
      boolean main;
      {
        string b,a;
        u = 2;
      }
    }
    """
    val expected = """Undeclared Identifier: u"""
    assert(checkCkr(input,expected,428))
  }

  test("29. test Undeclared Function") {
    val input = """   
    int a,b,c;
    string d,e;
    int main(float b){
      boolean main;
      {
        string b,a;
        gg();
      }
    }
    """
    val expected = """Undeclared Function: gg"""
    assert(checkCkr(input,expected,429))
  }

  test("30. Type Mismatch In Statemen : If ") {
    val input = """   
    int a,b,c;
    string d,e;
    int main(float b){
      boolean main;
      {
        string b,a;
        if (c == 1) b = "nguyenduyviettoan";
      }
    }
    """
    val expected = """"""
    assert(checkCkr(input,expected,430))
  }

  test("31. Type Mismatch In Statemen : If ") {
    val input = """   
    int a,b,c;
    string d,e;
    int main(float b){
      boolean main;
      {
        string b,a;
        if (c + 1) b = "nguyenduyviettoan";
      }
    }
    """
    val expected = """Type Mismatch In Statement: If(BinaryOp(+,Id(c),IntLiteral(1)),BinaryOp(=,Id(b),StringLiteral(nguyenduyviettoan)),None)"""
    assert(checkCkr(input,expected,431))
  }

  test("32. Type Mismatch In Statemen : If ") {
    val input = """   
    int a,b,c;
    string d,e;
    int main(float b){
      boolean main;
      {
        string b,a;
        if (c = 6) b = "nguyenduyviettoan";
      }
    }
    """
    val expected = """Type Mismatch In Statement: If(BinaryOp(=,Id(c),IntLiteral(6)),BinaryOp(=,Id(b),StringLiteral(nguyenduyviettoan)),None)"""
    assert(checkCkr(input,expected,432))
  } 

  test("33. Type Mismatch In Statemen : for ") {
    val input = """   
    int a,b,c;
    string d,e;
    int main(float b){
      boolean main;
      {
        for (main=4;c > main ;c-3) b = b - 3;
      }
    }
    """
    val expected = """Type Mismatch In Expression: BinaryOp(=,Id(main),IntLiteral(4))"""
    assert(checkCkr(input,expected,433))
  } 

  test("34. Type Mismatch In Statemen : for ") {
    val input = """   
    int a,b,c;
    string d,e;
    int main(float b){
      boolean main;
      {
        for (a=4;c > a ;c-3) b = b - 3;
      }
    }
    """
    val expected = """"""
    assert(checkCkr(input,expected,434))
  } 

  test("35. Type Mismatch In Statemen : for ") {
    val input = """   
    int a,b,c;
    string d,e;
    int main(float b){
      boolean main;
      {
        for (a = 4; c > 5 ; true) b = b - 3;
      }
    }
    """
    val expected = 
      """Type Mismatch In Statement: For(BinaryOp(=,Id(a),IntLiteral(4)),BinaryOp(>,Id(c),IntLiteral(5)),BooleanLiteral(true),BinaryOp(=,Id(b),BinaryOp(-,Id(b),IntLiteral(3))))"""
    assert(checkCkr(input,expected,435))
  } 

  test("36. Type Mismatch In Statemen : do while ") {
    val input = """   
    int a,b,c;
    string d,e;
    int main(float b){
      do 
        b = 2.4;
        b = 2 / 1;
        c == a;
      while (true);
    }
    """
    val expected = """"""
    assert(checkCkr(input,expected,436))
  } 

  test("37. Type Mismatch In Statemen : do while va return ") {
    val input = """   
    string d,e;
    int main(float b){
      do 
        b = 2.4;
        b = 2 / 1;
        c == a;
      while (false);
      return 2;
    }
    int a,b,c;
    """
    val expected = """"""
    assert(checkCkr(input,expected,437))
  } 

  test("38. Type Mismatch In Statemen : for va return ") {
    val input = """   
    string d,e;
    int main(float b){
      for (a=4;c > a ;c-3) b = b - 3;
      return ;
    }
    int a,b,c;
    """
    val expected = """Type Mismatch In Statement: Return(None)"""
    assert(checkCkr(input,expected,438))
  } 

  test("39. test cua thang huynh") {
  val input = """int main1; 
                 int main(float a,int b){
                  for (a=1;a<100;a+1) b=2;
                  } """
  val expected = "Type Mismatch In Statement: For(BinaryOp(=,Id(a),IntLiteral(1)),BinaryOp(<,Id(a),IntLiteral(100)),BinaryOp(+,Id(a),IntLiteral(1)),BinaryOp(=,Id(b),IntLiteral(2)))"
  assert(checkCkr(input,expected,439))
  }

  test("40. Type mismatch in if statement") {
    val input =
      """                         int z;
                                  void main (){
                                    if (z) z = 2;
                                  }"""

    val expected = "Type Mismatch In Statement: If(Id(z),BinaryOp(=,Id(z),IntLiteral(2)),None)"
    assert(checkCkr(input, expected, 440))
  }
  test("41. Type doesn't mismatch in if statement") {
    val input =
      """
                                  int z;
                                  void main (){
                                    boolean z;
                                    if (z) z = false;
                                  }"""

    val expected = ""
    assert(checkCkr(input, expected, 441))
  }

  test("42. Type doesn't mismatch in if statement 2") {
    val input =
      """
                                  boolean z;
                                  void main (){
                                    if (z) z = true;
                                  }"""

    val expected = ""
    assert(checkCkr(input, expected, 442))
  }

  test("43. Type mismatch in if statement 2") {
    val input =
      """
                                  boolean z(){}
                                  void main (){
                                    int z;
                                    if (z) z = 2;
                                  }"""

    val expected = "Type Mismatch In Statement: If(Id(z),BinaryOp(=,Id(z),IntLiteral(2)),None)"
    assert(checkCkr(input, expected, 443))
  }
  test("44. Type mismatch in for statement 1") {
    val input =
      """
                                  boolean z(){}
                                  void main (){
                                    int x, y, z;
                                    for (x = 1; y; z = 2) x = 0;
                                  }"""

    val expected = "Type Mismatch In Statement: For(BinaryOp(=,Id(x),IntLiteral(1)),Id(y),BinaryOp(=,Id(z),IntLiteral(2)),BinaryOp(=,Id(x),IntLiteral(0)))"
    assert(checkCkr(input, expected, 444))
  }

  test("45. Type doesn't mismatch in for statement 1") {
    val input =
      """
                                  void main (){
                                    int x, y, z;
                                    for (x = 1; z > 0; z = 2) x = 0;
                                  }"""

    val expected = ""
    assert(checkCkr(input, expected, 445))
  }

  test("46. Type doesn't mismatch in for statement 2") {
    val input =
      """
                                  boolean z(){}
                                  void main (){
                                    int x, y, z;
                                    for (x = 1; x % 3 == 0; z = 2) x = 0;
                                  }"""

    val expected = ""
    assert(checkCkr(input, expected, 446))
  }
  test("47. Type mismatch in Dowhile statement 1") {
    val input =
      """
                                  boolean z(){}
                                  void main (){
                                    int k;
                                    do z(); while k;
                                  }"""

    val expected = "Type Mismatch In Statement: Dowhile(List(CallExpr(Id(z),List())),Id(k))"
    assert(checkCkr(input, expected, 447))
  }
  test("48. Type mismatch in Dowhile statement 2") {
    val input =
      """
                                  boolean z(){}
                                  boolean k;
                                  void main (){
                                    int k;
                                    do z(); while k;
                                  }"""

    val expected = "Type Mismatch In Statement: Dowhile(List(CallExpr(Id(z),List())),Id(k))"
    assert(checkCkr(input, expected, 448))
  }

  test("49. Type doesn't mismatch in Dowhile statement 1") {
    val input =
      """
                                  boolean z(){}
                                  boolean k;
                                  void main (){
                                    do z(); while k;
                                  }"""

    val expected = ""
    assert(checkCkr(input, expected, 449))
  }
  test("50. Type doesn't mismatch in Dowhile statement 2") {
    val input =
      """
                                  boolean z(){}
                                  boolean k;
                                  void main (){
                                    do z(); while k != false;
                                  }"""

    val expected = ""
    assert(checkCkr(input, expected, 450))
  }
  test("51. Type doesn't mismatch in Dowhile statement 3") {
    val input =
      """
                                  boolean z(){}
                                  boolean k;
                                  void main (){
                                    do z(); while (k != true) || (k != false);
                                  }"""

    val expected = ""
    assert(checkCkr(input, expected, 451))
  }
  test("52. Type doesn't mismatch in Dowhile statement 4") {
    val input =
      """
                                  boolean z(){}
                                  string k;
                                  void main (){
                                    do z(); while z();
                                  }"""

    val expected = ""
    assert(checkCkr(input, expected, 452))
  }
  test("53. Type doesn't mismatch in Dowhile statement 5") {
    val input =
      """
                                  boolean z[2];
                                  string k;
                                  void main (){
                                    do z[1]; while z[0];
                                  }"""

    val expected = ""
    assert(checkCkr(input, expected, 453))
  }
  test("54. Type mismatch in Dowhile statement 3") {
    val input =
      """
                                  int z[2];
                                  string k;
                                  void main (){
                                    do z[1]; while z[0];
                                  }"""

    val expected = "Type Mismatch In Statement: Dowhile(List(ArrayCell(Id(z),IntLiteral(1))),ArrayCell(Id(z),IntLiteral(0)))"
    assert(checkCkr(input, expected, 454))
  }

  test("55. Type mismatch in Return statement 1") {
    val input =
      """
                                  int z[2];
                                  string k;
                                  void main (){
                                    return 0;
                                  }"""

    val expected = "Type Mismatch In Statement: Return(Some(IntLiteral(0)))"
    assert(checkCkr(input, expected, 455))
  }
  test("56. Type mismatch in Return statement 2") {
    val input =
      """
                                  int z[2];
                                  string k;
                                  void main (){
                                    return z;
                                  }"""

    val expected = "Type Mismatch In Statement: Return(Some(Id(z)))"
    assert(checkCkr(input, expected, 456))
  }
  test("57. Type mismatch in Return statement 3") {
    val input =
      """
                                  int z[2];
                                  string k;
                                  int main (){
                                    return "123";
                                  }"""

    val expected = "Type Mismatch In Statement: Return(Some(StringLiteral(123)))"
    assert(checkCkr(input, expected, 457))
  }
  test("58. Type mismatch in Return statement 4") {
    val input =
      """
                                  int z[2];
                                  string kk;
                                  int func (int k[]){
                                    return k;
                                  }"""

    val expected = "Type Mismatch In Statement: Return(Some(Id(k)))"
    assert(checkCkr(input, expected, 458))
  }
  test("59. Type mismatch in Return statement 5") {
    val input =
      """
                                  int z[2];
                                  boolean m;
                                  int func (int k[]){
                                    return m;
                                  }"""

    val expected = "Type Mismatch In Statement: Return(Some(Id(m)))"
    assert(checkCkr(input, expected, 459))
  }

  test("60. Type doesn't mismatch in Return statement 1") {
    val input =
      """
                                  int z[2];
                                  boolean m;
                                  boolean func (int k[]){
                                    return false;
                                  }"""

    val expected = ""
    assert(checkCkr(input, expected, 460))
  }

  test("61. Type doesn't mismatch in Return statement 2") {
    val input =
      """
                                  int z[2];
                                  boolean m;
                                  string func (int k[]){
                                    return "123";
                                  }"""

    val expected = ""
    assert(checkCkr(input, expected, 461))
  }
  test("62. Type doesn't mismatch in Return statement 3") {
    val input =
      """
                                  int z[2];
                                  boolean m;
                                  string func (){
                                    return func();
                                  }"""

    val expected = ""
    assert(checkCkr(input, expected, 462))
  }
  test("63. Type doesn't mismatch in Return statement 4") {
    val input =
      """
                                  int z[2];
                                  boolean m;
                                  void func (){
                                    return;
                                  }"""

    val expected = ""
    assert(checkCkr(input, expected, 463))
  }
  test("64. Type doesn't mismatch in Return statement 5") {
    val input =
      """
                                  int z[2];
                                  boolean m;
                                  int[] func (){
                                    return z;
                                  }"""

    val expected = ""
    assert(checkCkr(input, expected, 464))
  }
  test("65. Type doesn't mismatch in Return statement 6") {
    val input =
      """
                                  int z[2];
                                  boolean m;
                                  int[] func (int k[]){
                                    return k;
                                  }"""

    val expected = ""
    assert(checkCkr(input, expected, 465))
  }

  test("66. Type mismatch in Return statement 6") {
    val input =
      """
                                  boolean z[2];
                                  boolean m;
                                  int[] func (int k[]){
                                    return z;
                                  }"""

    val expected = "Type Mismatch In Statement: Return(Some(Id(z)))"
    assert(checkCkr(input, expected, 466))
  }

  test("67. Type mismatch in Return statement 8") {
    val input =
      """
                                  boolean z[2];
                                  boolean m;
                                  void func (int k[]){
                                    return z;
                                  }"""

    val expected = "Type Mismatch In Statement: Return(Some(Id(z)))"
    assert(checkCkr(input, expected, 467))
  }

  test("68. Type doesn't mismatch in Return statement 7") {
    val input =
      """
                                  string z[2];
                                  boolean m;
                                  string func (int k[]){
                                    return z[0];
                                  }"""

    val expected = ""
    assert(checkCkr(input, expected, 468))
  }

  test("69. Type mismatch in Return statement with many function") {
    val input =
      """
                                  string z[2];
                                  boolean m;
                                  string[] func (int k[]){
                                    return z[0];
                                    }
                                  string kaka(){
                                    return "a";
                                  }
                                  """

    val expected = "Type Mismatch In Statement: Return(Some(ArrayCell(Id(z),IntLiteral(0))))"
    assert(checkCkr(input, expected, 469))
  }

  test("70. Type mismatch in Return function statement") {
    val input =
      """
                                  string z[2];
                                  boolean m;
                                  string[] func (){
                                    return z;
                                  }
                                  string[] kaka(){
                                    return func()[1];
                                  }
                                  """

    val expected = "Type Mismatch In Statement: Return(Some(ArrayCell(CallExpr(Id(func),List()),IntLiteral(1))))"
    assert(checkCkr(input, expected, 470))
  }

  test("71. Type doesn't mismatch in Return function statement") {
    val input =
      """
                                  string z[2];
                                  boolean m;
                                  string[] func (){
                                    return z;
                                  }
                                  string[] kaka(){
                                    return func();
                                  }
                                  """

    val expected = ""
    assert(checkCkr(input, expected, 471))
  }

  test("72. Type mismatch in array subscripting expression 1") {
    val input =
      """
                                  string z[2];
                                  int main(){
                                    string k;
                                    z[k];
                                  }
                                  """

    val expected = "Type Mismatch In Expression: ArrayCell(Id(z),Id(k))"
    assert(checkCkr(input, expected, 472))
  }

  test("73. Type mismatch in array subscripting expression 2") {
    val input =
      """
                                  string z[2];
                                  int main(){
                                    string k;
                                    k[2];
                                  }
                                  """

    val expected = "Type Mismatch In Expression: ArrayCell(Id(k),IntLiteral(2))"
    assert(checkCkr(input, expected, 473))
  }

  test("74. Type doesn't mismatch in array subscripting expression 1") {
    val input =
      """
                                  boolean z[2];
                                  int main(){
                                    int k;
                                    k = 0;
                                    if (z[k + 1]) k = 2;
                                  }
                                  """

    val expected = ""
    assert(checkCkr(input, expected, 474))
  }
  test("75. Type mismatch in unary op expression 1") {
    val input =
      """
                                  boolean z[2];
                                  int main(){
                                    int a, b;
                                    a = 1;
                                    b = 2;
                                    a = !a;
                                  }
                                  """

    val expected = "Type Mismatch In Expression: UnaryOp(!,Id(a))"
    assert(checkCkr(input, expected, 475))
  }
  test("76. Type mismatch in unary op expression 2") {
    val input =
      """
                                  boolean z[2];
                                  int main(){
                                    boolean a, b;
                                    -a;
                                  }
                                  """

    val expected = "Type Mismatch In Expression: UnaryOp(-,Id(a))"
    assert(checkCkr(input, expected, 476))
  }
  test("77. Type mismatch in unary op expression 3") {
    val input =
      """
                                  boolean z[2];
                                  int main(){
                                    int a, b;
                                    !(a+b);
                                  }
                                  """

    val expected = "Type Mismatch In Expression: UnaryOp(!,BinaryOp(+,Id(a),Id(b)))"
    assert(checkCkr(input, expected, 477))
  }
  test("78. Type mismatch in unary op expression 4") {
    val input =
      """
                                  boolean z[2];
                                  int main(){
                                    float a;
                                    !a;
                                  }
                                  """

    val expected = "Type Mismatch In Expression: UnaryOp(!,Id(a))"
    assert(checkCkr(input, expected, 478))
  }
  test("79. Type mismatch in unary op expression 5") {
    val input =
      """
                                  boolean z[2];
                                  int main(){
                                    string a;
                                    !a;
                                  }
                                  """

    val expected = "Type Mismatch In Expression: UnaryOp(!,Id(a))"
    assert(checkCkr(input, expected, 479))
  }
  test("80. Type doesn't mismatch in unary op expression 1") {
    val input =
      """
                                  boolean z[2];
                                  int main(){
                                    string a;
                                    !z[0];
                                  }
                                  """

    val expected = ""
    assert(checkCkr(input, expected, 480))
  }
  test("81. Type doesn't mismatch in unary op expression 2") {
    val input =
      """
                                  boolean z[2];
                                  int main(){
                                    float a;
                                    -a;
                                  }
                                  """

    val expected = ""
    assert(checkCkr(input, expected, 481))
  }
  test("82. Type doesn't mismatch in unary op expression 3") {
    val input =
      """
                                  boolean z[2];
                                  int main(){
                                    float a;
                                    int b;
                                    -b;
                                  }
                                  """

    val expected = ""
    assert(checkCkr(input, expected, 482))
  }
  test("83. Type mismatch in binary op /") {
    val input =
      """
                                  boolean z[2];
                                  int main(){
                                    float a;
                                    boolean b;
                                    a/b;
                                  }
                                  """

    val expected = "Type Mismatch In Expression: BinaryOp(/,Id(a),Id(b))"
    assert(checkCkr(input, expected, 483))
  }
  test("84. Type match in binary op /") {
    val input =
      """
                                  boolean z[2];
                                  int main(){
                                    float a;
                                    float b;
                                    a/b;
                                  }
                                  """

    val expected = ""
    assert(checkCkr(input, expected, 484))
  }
  test("85. Type mismatch in binary op *") {
    val input =
      """
                                  boolean z[2];
                                  int main(){
                                    string a;
                                    boolean b;
                                    a*b;
                                  }
                                  """

    val expected = "Type Mismatch In Expression: BinaryOp(*,Id(a),Id(b))"
    assert(checkCkr(input, expected, 485))
  }
  test("86. Type match in binary op *") {
    val input =
      """
                                  int z[2];
                                  int main(){
                                   float a;
                                    a*z[0];
                                  }
                                  """

    val expected = ""
    assert(checkCkr(input, expected, 486))
  }
  test("87. Type mismatch in binary op %") {
    val input =
      """
                                  int z[2];
                                  int main(){
                                   float a;
                                    a % 2;
                                  }
                                  """

    val expected = "Type Mismatch In Expression: BinaryOp(%,Id(a),IntLiteral(2))"
    assert(checkCkr(input, expected, 487))
  }
  test("88. Type match in binary op %") {
    val input =
      """
                                  int z[2];
                                  int main(){
                                   int a;
                                    a % 2;
                                  }
                                  """

    val expected = ""
    assert(checkCkr(input, expected, 488))
  }
  test("489. Type mismatch in binary op +") {
    val input =
      """
                                  int z[2];
                                  int main(){
                                   float a;
                                    a + "b";
                                  }
                                  """

    val expected = "Type Mismatch In Expression: BinaryOp(+,Id(a),StringLiteral(b))"
    assert(checkCkr(input, expected, 489))
  }
  test("490. Type match in binary op +") {
    val input =
      """
                                  int z[2];
                                  int main(){
                                   int a;
                                    a + z[1];
                                  }
                                  """

    val expected = ""
    assert(checkCkr(input, expected, 490))
  }
  test("491. Type match in binary op >") {
    val input =
      """
                                  int z[2];
                                  int main(){
                                   int a;
                                    a > true;
                                  }
                                  """

    val expected = "Type Mismatch In Expression: BinaryOp(>,Id(a),BooleanLiteral(true))"
    assert(checkCkr(input, expected, 491))
  }
  test("492. Type match in complicated ops") {
    val input =
      """
                                  int z[2];
                                  int main(){
                                   int a;
                                  boolean b;
                                  b = (a > 2) || (a < 3) && (a % z[0] == 6);
                                  }
                                  """

    val expected = ""
    assert(checkCkr(input, expected, 492))
  }

  test("493. Type mismatch in complicated ops") {
    val input =
      """
                                  int z[2];
                                  int main(){
                                   int a;
                                  boolean b;
                                  b = (a > 2) || (a < 3) && (a % z[0] = 6);
                                  }
                                  """

    val expected = "Type Mismatch In Expression: BinaryOp(&&,BinaryOp(<,Id(a),IntLiteral(3)),BinaryOp(=,BinaryOp(%,Id(a),ArrayCell(Id(z),IntLiteral(0))),IntLiteral(6)))"
    assert(checkCkr(input, expected, 493))
  }
  test("494. Type match in complicated ops ass 1") {
    val input =
      """
                                  int z[2];
                                  int main(){
                                   int a;
                                  float b;
                                  b = a = 6;
                                  }
                                  """

    val expected = ""
    assert(checkCkr(input, expected, 494))
  }
  test("495. Type match in complicated ops ass 2") {
    val input =
      """
                                  int z[2];
                                  int main(){
                                   string a;
                                   string b;
                                  a = b = "abc";
                                  }
                                  """

    val expected = ""
    assert(checkCkr(input, expected, 495))
  }
  test("496. Type mismatch in function 1") {
    val input =
      """
                                  int z[2];
                                  int foo(string a){}
                                  int main(){
                                   string a;
                                   string b;
                                  a = b = "abc";
                                  foo(2);
                                  }
                                  """

    val expected = "Type Mismatch In Expression: CallExpr(Id(foo),List(IntLiteral(2)))"
    assert(checkCkr(input, expected, 496))
  }
  test("497. Type mismatch in function 2") {
    val input =
      """
                                  int z[2];
                                  int foo(int a, int b){}
                                  int main(){
                                   string a;
                                   string b;
                                  a = b = "abc";
                                  foo(2);
                                  }
                                  """

    val expected = "Type Mismatch In Expression: CallExpr(Id(foo),List(IntLiteral(2)))"
    assert(checkCkr(input, expected, 497))
  }
  test("498.Type mismatch in function 3") {
    val input =
      """
                                  int z[2];
                                  int foo(int a[]){}
                                  int main(){
                                   int a;
                                   string b;
                                  foo(a);
                                  }
                                  """

    val expected = "Type Mismatch In Expression: CallExpr(Id(foo),List(Id(a)))"
    assert(checkCkr(input, expected, 498))
  }
  test("499. Type mismatch in function 4") {
    val input =
      """
                                  int z[2];
                                  int foo(boolean a[]){}
                                  int a[4];
                                  int main(){
                                   string a[4];
                                   string b;
                                  foo(a);
                                  }
                                  """

    val expected = "Type Mismatch In Expression: CallExpr(Id(foo),List(Id(a)))"
    assert(checkCkr(input, expected, 499))
  }
  test("500 . Type match in super complicated function") {
    val input =
      """
                                  int z[2];
                                  int foo(int a, int b[], float c, float d[], string e, boolean f){}
                                  float a[4];
                                  int main(int p[]){
                                   int f;
                                   int g[5];
                                   f = foo(g[4] + 2, p, 4.3, a, "yeah", (false || true) && (2 >= -1));
                                  }
                                  """

    val expected = ""
    assert(checkCkr(input, expected, 500))
  }
}

