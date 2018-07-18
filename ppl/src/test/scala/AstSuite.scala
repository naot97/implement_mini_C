import org.scalatest.FunSuite
import mc.utils._

/**
  * Created by nhphung on 4/29/17.
  */
class AstSuite extends FunSuite with TestAst {
  test("201. funcdecla voi kieu void") {
    val input = "void main () {}"
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(List(),List())
          )
        )
      )
    assert(checkAst(input,expected,201))
  }

  test("202. funcdecla voi kieu int") {
    val input = "int main () {}"
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            IntType,
            Block(List(),List())
          )
        )
      )
    assert(checkAst(input,expected,202))
  }

  test("203. funccall don gian") {
    val input = "void main () {putIntLn(5);}"
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                CallExpr(
                  Id("putIntLn"),
                  List(IntLiteral(5))
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,203))
  }

  test("204. 3 function decla") {
    val input = "void main(){} int func1(){} boolean func2(){}"
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(List(),List())
          ),
           FuncDecl(
            Id("func1"),
            List(),
            IntType,
            Block(List(),List())
          ),
           FuncDecl(
            Id("func2"),
            List(),
            BoolType,
            Block(List(),List())
          )
         )
      )
    assert(checkAst(input,expected,204))
  }

  test("205. mot vardecla") {
    val input = "string a;"
    val expected = 
      Program(
        List(
          VarDecl(Id("a"), StringType  )
        )
      )
    assert(checkAst(input,expected,205))
  }
  test("206. mot vardecla nhung nhieu bien") {
    val input = "int a,b,c;"
    val expected = 
      Program(
        List(
          VarDecl(Id("a"), IntType),
          VarDecl(Id("b"), IntType  ),
          VarDecl(Id("c"), IntType  )    
        )
      )
    assert(checkAst(input,expected,206))
  }
  test("207. mot vardecla nhung nhieu bien va co arraytype") {
    val input = "int i,j,k[5];"
    val expected = 
      Program(
        List(
          VarDecl(Id("i"), IntType),
          VarDecl(Id("j"), IntType  ),
          VarDecl(Id("k"), ArrayType(IntLiteral(5),IntType))
        )
      )
    assert(checkAst(input,expected,207))
  }
  test("208. nhieu vardecla") {
    val input =
      """boolean a[45], p, o;
        string s, b;
        int a; float f[76]; boolean m[100];string yy;
      """
    val expected = 
      Program(
        List(
          VarDecl(Id("a"), ArrayType(IntLiteral(45),BoolType)  ),
          VarDecl(Id("p"), BoolType  ),
          VarDecl(Id("o"), BoolType  ),
          VarDecl(Id("s"), StringType),
          VarDecl(Id("b"), StringType),
          VarDecl(Id("a"), IntType),
          VarDecl(Id("f"), ArrayType(IntLiteral(76),FloatType)  ),
          VarDecl(Id("m"), ArrayType(IntLiteral(100),BoolType)  ),
          VarDecl(Id("yy"), StringType)    
        )
      )
    assert(checkAst(input,expected,208))
  }
  test("209. test funcdecla voi kieu arraypoiter va co params") {
      val input =
          """int[] func(int a, float b, boolean c) { }"""
      val expected = 
        Program(
          List(
            FuncDecl(
              Id("func"),
              List( 
                VarDecl(Id("a"),IntType),
                VarDecl(Id("b"),FloatType),
                VarDecl(Id("c"),BoolType)),
              ArrayPointerType(IntType),
              Block(List(),List())  
            )  
          )
        )
    assert(checkAst(input,expected,209))
  }
  test("210. test funcdecla voi kieu arraypoiter va co params kieu arraypointer") {
      val input =
          """boolean[] foo(boolean a[], boolean b, int c){
        }
      """
      val expected = 
        Program(
          List(
            FuncDecl(
              Id("foo"),
              List( 
                VarDecl(Id("a"),ArrayPointerType(BoolType)),
                VarDecl(Id("b"),BoolType),
                VarDecl(Id("c"),IntType) 
              ),
              ArrayPointerType(BoolType),
              Block(List(),List())  
            )  
          )
        )
    assert(checkAst(input,expected,210))
  }
  test("211. test funcdecla binh thuong") {
      val input =
              """void main(float k, string b){

          }
        """

      val expected = 
        Program(
          List(
            FuncDecl(
              Id("main"),
              List(
                VarDecl(Id("k"),FloatType),
                VarDecl(Id("b"),StringType)
              ),
              VoidType,
              Block(List(),List())
            )
          )
        )
    assert(checkAst(input,expected,211))
  }
  test("212. test ifsta don gian") {
      val input =
        """void test(float a, float b){
          if(a > b) a = b + 1;
         }"""

      val expected = 
        Program(
          List(
            FuncDecl(
              Id("test"),
              List(
                VarDecl(Id("a"),FloatType),
                VarDecl(Id("b"),FloatType)
              ),
              VoidType,
              Block(
                List(),
                List(
                  If(
                    BinaryOp(">", Id("a"), Id("b")),
                    BinaryOp("=", Id("a"), BinaryOp("+", Id("b"), IntLiteral(1)) ),
                    None
                  )
                )
              )
            )
          )
        )
    assert(checkAst(input,expected,212))
  }
  test("213. test ifsta phuc tap") {
    val input =
      """void test(int a, string b){
          if(a == b) {
            a = a + 2;
            b = b - 2;
          }
           else{
            a = a / b;
            b = b + 4;
          }
          return;
         }"""

    val expected = 
      Program(
        List(
          FuncDecl(
            Id("test"),
            List(
              VarDecl(Id("a"),IntType),
              VarDecl(Id("b"),StringType)
            ),
            VoidType,
            Block(
              List(),
              List(
                If(
                  BinaryOp("==", Id("a"), Id("b")),
                  Block(
                    List(),
                    List(
                      BinaryOp("=", Id("a"), BinaryOp("+", Id("a"), IntLiteral(2))),
                      BinaryOp("=", Id("b"), BinaryOp("-", Id("b"), IntLiteral(2))),                      
                    )
                  ),
                  Some(
                    Block(
                      List(),
                      List(
                        BinaryOp("=", Id("a"), BinaryOp("/", Id("a"), Id("b"))),
                        BinaryOp("=", Id("b"), BinaryOp("+", Id("b"), IntLiteral(4))),   
                      )
                    )
                  )
                ),
                Return(None)
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,213))
  }
  test("214. test ifsta phuc tap co ca vardecla") {
    val input =
      """int a,b,c;
        string s;
        int foo(int i, float a[]){
          a = b = c = 2;
          if((3 + 2) = 5)
            b = (5 + 6)%a;
          else
            b = 0;
         return 0;
        }"""

    val expected = 
      Program(
        List(
          VarDecl(Id("a"), IntType),
          VarDecl(Id("b"), IntType),
          VarDecl(Id("c"), IntType),
          VarDecl(Id("s"), StringType),
          FuncDecl(
            Id("foo"),
            List(
              VarDecl(Id("i"),IntType),
              VarDecl(Id("a"),ArrayPointerType(FloatType))
            ),
            IntType,
            Block(
              List(),
              List(
                BinaryOp(
                  "=", 
                  Id("a"), 
                  BinaryOp(
                    "=", 
                    Id("b"), 
                    BinaryOp("=", Id("c"), IntLiteral(2))
                  )
                ), 
                If(
                  BinaryOp("=", BinaryOp("+", IntLiteral(3), IntLiteral(2)), IntLiteral(5)),
                  BinaryOp(
                    "=",
                    Id("b"), 
                    BinaryOp("%", BinaryOp("+", IntLiteral(5), IntLiteral(6)), Id("a"))
                  ),
                  Some(
                    BinaryOp("=", Id("b"), IntLiteral(0))
                  )
                ),
                Return(Some(IntLiteral(0)))
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,214))
  }
  test("215. test ifsta rut gon") {
    val input =
      """float[] foo(float a[], boolean c){
          if(a) c = c + 4;
          return a;
          }"""

    val expected = 
      Program(
        List(
          FuncDecl(
            Id("foo"),
            List(
              VarDecl(Id("a"),ArrayPointerType(FloatType)),
              VarDecl(Id("c"), BoolType)
            ),
            ArrayPointerType(FloatType),
            Block(
              List(),
              List(
                If(
                  Id("a"),
                  BinaryOp(
                    "=",
                    Id("c"),
                    BinaryOp("+", Id("c"), IntLiteral(4))
                  ),
                  None
                ),
                Return(Some(Id("a")))
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,215))
  }
  test("216. test ifsta rut gon 2") {
    val input =
      """int foo(){
          if(a) b = false;
          else c = true;
        }"""

    val expected = 
      Program(
        List(
          FuncDecl(
            Id("foo"),
            List(),
            IntType,
            Block(
              List(),
              List(
                If(
                  Id("a"),
                  BinaryOp(
                    "=",
                    Id("b"),
                    BooleanLiteral(false)
                  ),
                  Some(
                    BinaryOp(
                      "=",
                      Id("c"),
                      BooleanLiteral(true)
                    )
                  )
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,216))
  }
  test("217. test do while don gian") {
    val input =
      """int foo(){
          do
            a;
            2;
            2%3;
          while b;
        }"""

    val expected = 
      Program(
        List(
          FuncDecl(
            Id("foo"),
            List(),
            IntType,
            Block(
              List(),
              List(
                Dowhile(
                  List(
                    Id("a"),
                    IntLiteral(2),
                    BinaryOp("%", IntLiteral(2), IntLiteral(3))
                  ),
                  Id("b")
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,21))
  }
  test("218. test do while phuc tap") {
    val input =
      """float foo(float a, float b){
          do
            if (a) b;
            if (a % 2 == 5) 2 + 1;
              else (a * 2 - 4 * 3 *( 4 - 2));
          while b!=2;
        }"""

    val expected = 
      Program(
        List(
          FuncDecl(
            Id("foo"),
            List(
              VarDecl(Id("a"),FloatType),
              VarDecl(Id("b"),FloatType)
            ),
            FloatType,
            Block(
              List(),
              List(
                Dowhile(
                  List(
                    If(Id("a"), Id("b"), None),
                    If(
                      BinaryOp("==", BinaryOp("%", Id("a"), IntLiteral(2)), IntLiteral(5) ),
                      BinaryOp("+", IntLiteral(2), IntLiteral(1)),
                      Some(
                        BinaryOp(
                          "-",
                          BinaryOp("*", Id("a"), IntLiteral(2)),
                          BinaryOp(
                            "*",
                            BinaryOp("*", IntLiteral(4), IntLiteral(3)),
                            BinaryOp("-", IntLiteral(4), IntLiteral(2))
                          ) 
                        )
                      )
                    )
                  ),
                  BinaryOp("!=",Id("b"), IntLiteral(2))
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,218))
  }
  test("219. test do while long do while") {
    val input =
      """string a, b, c;
        void main(){
          float d;
          int e;
          boolean f;
          int a[5];
          do
            do
              a + 2;
              f = true;
            while c;
          while 2;
        }"""

    val expected = 
      Program(
        List(
          VarDecl(Id("a"),StringType),
          VarDecl(Id("b"),StringType),
          VarDecl(Id("c"),StringType),
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(
                VarDecl(Id("d"),FloatType),
                VarDecl(Id("e"),IntType),
                VarDecl(Id("f"),BoolType),
                VarDecl(Id("a"),ArrayType(IntLiteral(5), IntType))
              ),
              List(
                Dowhile(
                  List(
                    Dowhile(
                      List(
                        BinaryOp("+", Id("a"), IntLiteral(2)),
                        BinaryOp("=", Id("f"), BooleanLiteral(true))
                      ),
                    Id("c")  
                    )
                  ),
                  IntLiteral(2)
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,219))
  }
  test("220. test do while long if") {
    val input =
      """boolean test(){
          int a;
          boolean b,c,d;
          do
            if (a == 5) 5;
            else{
              if (5) 6;
              do (a+2);
              while a==4;
              c=5;
            }
          while a==6;
        }"""

    val expected = 
      Program(
        List(
          FuncDecl(
            Id("test"),
            List(),
            BoolType,
            Block(
              List(
                VarDecl(Id("a"),IntType),
                VarDecl(Id("b"),BoolType),
                VarDecl(Id("c"),BoolType),
                VarDecl(Id("d"),BoolType)
              ),
              List(
                Dowhile(
                  List(
                    If(
                      BinaryOp("==", Id("a"), IntLiteral(5)),
                      IntLiteral(5),
                      Some(
                        Block(
                          List(),
                          List(
                            If(IntLiteral(5), IntLiteral(6), None),
                            Dowhile(
                            List(
                              BinaryOp("+", Id("a"), IntLiteral(2))
                            ),
                            BinaryOp("==", Id("a"), IntLiteral(4))
                            ),
                            BinaryOp("=", Id("c"), IntLiteral(5))
                          )
                        )
                      )
                    )
                  ),
                  BinaryOp("==", Id("a"), IntLiteral(6))
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,220))
  }
  test("221. test do while tiep tuc") {
    val input =
      """int main(string args){
          do{
            a + 2;
          }while(a<10);
        }"""

    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(
              VarDecl(Id("args"), StringType)
            ),
            IntType,
            Block(
              List(),
              List(
                Dowhile(
                  List(
                    Block(
                      List(),
                      List(
                        BinaryOp("+", Id("a"), IntLiteral(2))
                      )
                    )
                  ),
                  BinaryOp("<",Id("a"), IntLiteral(10))
                )
              )
            )  
          )
        )
      )
    assert(checkAst(input,expected,221))
  }

  test("222. test vong for don gian") {
    val input =
      """int a,b,c;
        void foo(){
          for(a < 1; a < 4; a + 2)
            a = a + 1;
        }
        """
    val expected = 
      Program(
        List(
          VarDecl(Id("a"),IntType),
          VarDecl(Id("b"),IntType),
          VarDecl(Id("c"),IntType),
          FuncDecl(
            Id("foo"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                For(
                  BinaryOp("<",Id("a"),IntLiteral(1)),
                  BinaryOp("<",Id("a"),IntLiteral(4)),
                  BinaryOp("+",Id("a"),IntLiteral(2)),
                  BinaryOp("=",Id("a"),BinaryOp("+",Id("a"),IntLiteral(1)))
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,222))
  }
  test("223. test for Statement 2") {
    val input =
      """int i;
        boolean c;
        string s;
        void foo(int a, float b[]){
          for(i = 0; i < 100;i + 1)
            b[2] = b[i] + 2;
          b[50];
        }
      """
    val expect = 
      Program(
        List(
          VarDecl(Id("i"),IntType),
          VarDecl(Id("c"),BoolType),
          VarDecl(Id("s"),StringType),
          FuncDecl(
            Id("foo"),
            List(
              VarDecl(Id("a"), IntType),
              VarDecl(Id("b"), ArrayPointerType(FloatType))
            ),
            VoidType,
            Block(
              List(),
              List(
                For(
                  BinaryOp("=", Id("i"), IntLiteral(0)),
                  BinaryOp("<", Id("i"), IntLiteral(100)),
                  BinaryOp("+", Id("i"), IntLiteral(1)),
                  BinaryOp(
                    "=",
                    ArrayCell(Id("b"), IntLiteral(2)),
                    BinaryOp(
                      "+",
                      ArrayCell(Id("b"), Id("i")),
                      IntLiteral(2)
                    )
                  )
                ),
                ArrayCell(Id("b"), IntLiteral(50))
              )
            )
          )
        )
      )
    assert(checkAst(input,expect,223))
  }
  test("224. array cell vong nhau") {
    val input =
      """int k;
        void func(){
        for(k == 5; k <= 10; k + 1){
           int a ,b, c ;
           float f [ 5 ] ;
           // start statement part
           a=b=2;
           if (a=b) f [0]=1.0;
           //end statement part
        }
        t = a[b[2]] +3;
      }
      """
    val expected = 
      Program(
        List(
          VarDecl(Id("k"),IntType),
          FuncDecl(
            Id("func"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                For(
                  BinaryOp("==",Id("k"),IntLiteral(5)),
                  BinaryOp("<=",Id("k"),IntLiteral(10)),
                  BinaryOp("+",Id("k"),IntLiteral(1)),
                  Block(
                    List(
                      VarDecl(Id("a"),IntType),
                      VarDecl(Id("b"),IntType),
                      VarDecl(Id("c"),IntType),
                      VarDecl(Id("f"),ArrayType(IntLiteral(5),FloatType))
                    ),
                    List(
                      BinaryOp("=",Id("a"),BinaryOp("=",Id("b"),IntLiteral(2))),
                      If(
                        BinaryOp("=",Id("a"),Id("b")),
                        BinaryOp("=",ArrayCell(Id("f"),IntLiteral(0)),FloatLiteral((1.0).toFloat)),
                        None
                      )
                    )
                  )
                ),
                  BinaryOp(
                    "=",
                    Id("t"),
                    BinaryOp("+",ArrayCell(Id("a"),ArrayCell(Id("b"),IntLiteral(2))),IntLiteral(3))
                  )
                )
            )
          )
        )
      )
      
    assert(checkAst(input,expected,224))
  }
  test("225. 2 func va vardecla") {
    val input =
      """int i ;
                  int f () {
                    for(a*2; b = 2; b * 2)
                     a = 100;
                    return 200;
                  }
                  void main() {
                    int main ;
                    main = f();
                  }"""
    val expected = 
      Program(
        List(
          VarDecl(Id("i"), IntType),
          FuncDecl(
            Id("f"),
            List(),
            IntType,
            Block(
              List(),
              List(
                For(
                  BinaryOp("*", Id("a"), IntLiteral(2)),
                  BinaryOp("=", Id("b"), IntLiteral(2)),
                  BinaryOp("*", Id("b"), IntLiteral(2)),
                  BinaryOp("=", Id("a"), IntLiteral(100))
                ),
                Return(Some(IntLiteral(200)))
              )
            )
          ),
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(
                VarDecl(Id("main"), IntType)
              ),
              List(
                BinaryOp("=", Id("main"), CallExpr(Id("f"), List()))
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,225))
  }
  test("226. vong for tiep tuc") {
    val input =
      """int a;
        void main(){
        for(a=5;a<10;a&&2)
          a * 2;
        }
      """
    val expected = 
      Program(
        List(
          VarDecl(Id("a"),IntType),
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                For(
                  BinaryOp("=",Id("a"),IntLiteral(5)),
                  BinaryOp("<",Id("a"),IntLiteral(10)),
                  BinaryOp("&&",Id("a"),IntLiteral(2)),
                  BinaryOp("*",Id("a"),IntLiteral(2))
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,226))
  }
  test("227. test break") {
    val input =
      """int main(){
         for(i=0; i < 5; i && 2)
          break;
          a = -2;
       }"""
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            IntType,
            Block(
              List(),
              List(
                For(
                  BinaryOp("=", Id("i"), IntLiteral(0)),
                  BinaryOp("<", Id("i"), IntLiteral(5)),
                  BinaryOp("&&", Id("i"), IntLiteral(2)),
                  Break
                ),
                BinaryOp(
                  "=",
                  Id("a"),
                  UnaryOp("-", IntLiteral(2))
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,227))
  }
  test("228. test break va do while") {
    val input =
       """int main(){
         do
          break;
          a % 2;
          a!=2;
         while a;
       }
      """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            IntType,
            Block(
              List(),
              List(
                Dowhile(
                  List(
                    Break,
                    BinaryOp("%", Id("a"), IntLiteral(2)),
                    BinaryOp("!=", Id("a"), IntLiteral(2))
                  ),
                  Id("a")
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,228))
  }
  test("229. test truong hop sai cua break") {
    val input =
      """int main(){
         break;
         for(i!=1;i+5;i&&2)
          i + 1;
          {
            break;
            if(a==2) b;
            else b >= 5;
            a % 7;
            !a;
            !3;
          }
       }"""
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            IntType,
            Block(
              List(),
              List(
                Break,
                For(
                  BinaryOp("!=", Id("i"), IntLiteral(1)),
                  BinaryOp("+", Id("i"), IntLiteral(5)),
                  BinaryOp("&&", Id("i"), IntLiteral(2)),
                  BinaryOp("+",Id("i"), IntLiteral(1))
                ),
                Block(
                  List(),
                  List(
                    Break,
                    If( BinaryOp("==", Id("a"), IntLiteral(2)), Id("b"), Some(BinaryOp(">=", Id("b"), IntLiteral(5))) ),
                    BinaryOp("%", Id("a"), IntLiteral(7)),
                    UnaryOp("!", Id("a")),
                    UnaryOp("!", IntLiteral(3))
                  )
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,229))
  }
  test("230. array cell ") {
    val input =
      """int main(){
         a[2];
         a[a];
       }
      """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            IntType,
            Block(
              List(),
              List(
                ArrayCell(Id("a"), IntLiteral(2)),
                ArrayCell(Id("a"), Id("a"))
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,230))
  }

  test("231. test 3 cai break lien") {
    val input =
     """int main(){
         if (a) b = 1;
         for(i=1;i<=10;i+1)
         break;
         break;
         break;
         do a; b = 2; c + 1; b && 1; while b==2;
       }
      """
    val expected =
      Program (
        List(
          FuncDecl(
            Id("main"),
            List(),
            IntType,
            Block(
              List(),
              List(
                If(Id("a"), BinaryOp("=", Id("b"), IntLiteral(1)), None),
                For(
                  BinaryOp("=", Id("i"), IntLiteral(1)),
                  BinaryOp("<=", Id("i"), IntLiteral(10)),
                  BinaryOp("+", Id("i"), IntLiteral(1)),
                  Break
                ),
                Break,
                Break,
                Dowhile(
                  List(
                    Id("a"),
                    BinaryOp("=", Id("b"), IntLiteral(2)),
                    BinaryOp("+", Id("c"), IntLiteral(1)),
                    BinaryOp("&&", Id("b"), IntLiteral(1))
                  ),
                  BinaryOp("==", Id("b"), IntLiteral(2))
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,231))
  }

  test("232. test continue") {
    val input =
       """string main;
         int a;
         boolean g;
         float a[47];
         int main(){
         for(a=5;a&&4;a<5)
         continue;
       }
      """
    val expected = 
      Program(
        List(
          VarDecl(Id("main"), StringType),
          VarDecl(Id("a"), IntType),
          VarDecl(Id("g"), BoolType),
          VarDecl(Id("a"), ArrayType(IntLiteral(47), FloatType)),
          FuncDecl(
            Id("main"),
            List(),
            IntType,
            Block(
              List(),
              List(
                For(
                  BinaryOp("=", Id("a"), IntLiteral(5)),
                  BinaryOp("&&", Id("a"), IntLiteral(4) ),
                  BinaryOp("<", Id("a"), IntLiteral(5)),
                  Continue
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,232))
  }

  test("233.") {
    val input =
      """int findMax(int a,int b){
         for(i = 1; i < 10; i + 1){
          if(a > b) continue;
          else a = a + 1;
         }
       }"""
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("findMax"),
            List(
              VarDecl(Id("a"), IntType),
              VarDecl(Id("b"), IntType)
            ),
            IntType,
            Block(
              List(),
              List(
                For(
                  BinaryOp("=", Id("i"), IntLiteral(1)),
                  BinaryOp("<", Id("i"), IntLiteral(10)),
                  BinaryOp("+", Id("i"), IntLiteral(1)),
                  Block(
                    List(),
                    List(
                      If(
                        BinaryOp(">", Id("a"), Id("b")),
                        Continue,
                        Some(BinaryOp("=", Id("a"), BinaryOp("+", Id("a"), IntLiteral(1))))
                      )
                    )
                  )
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,233))
  }

  test("234. test continue trong while ") {
    val input =
      """string a[3];
         int main(){
         do continue;
         while a[1]=4;
         a == 5;
         return 5;
       }
      """
    val expected = 
      Program(
        List(
          VarDecl(Id("a"), ArrayType(IntLiteral(3), StringType)),
          FuncDecl(
            Id("main"),
            List(),
            IntType,
            Block(
              List(),
              List(
                Dowhile(
                  List(
                    Continue
                  ),
                  BinaryOp("=", ArrayCell(Id("a"), IntLiteral(1)), IntLiteral(4))
                ),
                BinaryOp("==", Id("a"), IntLiteral(5)),
                Return(Some(IntLiteral(5)))
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,234))
  }

  test("235. test trong group 1") {
    val input =
      """
          int [] hungdz(boolean s , float i){
            {
              {
                {
                  {
                    continute;
                  }
                  break;
                }
                continute;
              }
              continute;
            }
          }
      """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("hungdz"),
            List(
              VarDecl(Id("s"), BoolType),
              VarDecl(Id("i"), FloatType)
            ),
            ArrayPointerType(IntType),
            Block(
              List(),
              List(
                Block(
                  List(),
                  List(
                    Block(
                      List(),
                      List(
                        Block(
                          List(),
                          List(
                            Block(
                              List(),
                              List(
                                Id("continute")
                              )
                            ),
                            Break
                          )
                        ),
                        Id("continute")
                      )
                    ),
                    Id("continute")
                  )
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,235))
  }

  test("236. test nhieu phep toan kho") {
    val input =
      """ void main(){
                        id[2]/5[7];
                        (2.8*!id)+8+9;
                        id[3]-5;
                        2---8;
                        
                      }  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("/", ArrayCell(Id("id"), IntLiteral(2)), ArrayCell(IntLiteral(5),IntLiteral(7))),
                BinaryOp("+", BinaryOp("+", BinaryOp("*", FloatLiteral(2.8.toFloat), UnaryOp("!", Id("id"))), IntLiteral(8)), IntLiteral(9)),
                BinaryOp("-", ArrayCell(Id("id"), IntLiteral(3)), IntLiteral(5)),
                BinaryOp("-", IntLiteral(2), UnaryOp("-", UnaryOp("-", IntLiteral(8))))                
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,236))
  }

  test("237.") {
    val input =
      """void main(){
         return;
       }
      """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                Return(None)
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,237))
  }

  test("238.") {
    val input =
      """int main(){
         return 0;
       }
      """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            IntType,
            Block(
              List(),
              List(
                Return(Some(IntLiteral(0)))
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,238))
  }

  test("239. call func nhieu tham so") {
    val input =
      """
      string s;
      int Main(){
        func(a,b,c);
        a = func1(4,3,5);
        a + b = c + d;
        }
      """
    val expected = 
      Program(
        List(
          VarDecl(Id("s"), StringType),
          FuncDecl(
            Id("Main"),
            List(),
            IntType,
            Block(
              List(),
              List(
                CallExpr(
                  Id("func"), 
                  List(
                    Id("a"),
                    Id("b"),
                    Id("c"),
                  )
                ),
                BinaryOp(
                  "=",
                  Id("a"),
                  CallExpr(
                    Id("func1"),
                    List(
                      IntLiteral(4),
                      IntLiteral(3),
                      IntLiteral(5),
                    )
                  )
                ),
                BinaryOp(
                  "=",
                  BinaryOp("+", Id("a"), Id("b")),
                  BinaryOp("+", Id("c"), Id("d")),
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,239))
  }

  test("240. test inner array cell") {
    val input =
      """int main(){
        a[b[4]];
        }
      """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            IntType,
            Block(
              List(),
              List(
                ArrayCell(Id("a"),ArrayCell(Id("b"),IntLiteral(4)))
              )
            )
          )
        )
      )
      
    assert(checkAst(input,expected,240))
  }

  test("241. so nguyen to") {
     val input = """void main()
{
   int n, c;
   printf("nhap so ngueyn\n");
   scanf("%d", n);

   if ( n == 2 )
      printf("so nguyen to");
   else
   {
       for ( c = 2 ; c <= n - 1 ; c= c+1 )
       {
           if ( n % c == 0 )
              break;
       }
       if ( c != n )
          printf("so nguyen to.");
       else
          printf("ko phai so nguyen to.");
   }
   return 1;
} """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(
                VarDecl(Id("n"),IntType),
                VarDecl(Id("c"),IntType)
              ),
              List(
                CallExpr(Id("printf"),List(StringLiteral("nhap so ngueyn\\n"))),
                CallExpr(Id("scanf"),List(StringLiteral("%d"),Id("n"))),
                If(
                  BinaryOp("==" ,Id("n"),IntLiteral(2)),
                  CallExpr(Id("printf"),List(StringLiteral("so nguyen to"))),
                  Some(
                    Block(
                      List(),
                      List(
                        For(
                          BinaryOp("=" ,Id("c"),IntLiteral(2)),
                          BinaryOp("<=" ,Id("c"),BinaryOp("-" ,Id("n"),IntLiteral(1))),
                          BinaryOp("=" ,Id("c"),BinaryOp("+" ,Id("c"),IntLiteral(1))),
                          Block(List(),List(If(BinaryOp("==" ,BinaryOp("%" ,Id("n"),Id("c")),IntLiteral(0)),Break,None)))
                        ),
                        If(
                          BinaryOp("!=" ,Id("c"),Id("n")),
                          CallExpr(Id("printf"),List(StringLiteral("so nguyen to."))),
                          Some(CallExpr(Id("printf"),List(StringLiteral("ko phai so nguyen to."))))
                        )
                      )
                    )
                  )
                ),
                Return(Some(IntLiteral(1)))
              )
            )
          )
        )
      )

    assert(checkAst(input,expected,241))
  }

  test("242. comment in program") {
    val input = """void main()
{
   // Single line comment in c source code

   printf("Writing comments is very useful.\n");

   /*
    * Multi line comment syntax
    * Comments help us to understand code later easily.
    * Will you write comments while developing programs ?
    */

   return 0;
}  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                CallExpr(Id("printf"),List(StringLiteral("Writing comments is very useful.\\n"))),
                Return(Some(IntLiteral(0))))
            )
          )
        )
      )

    assert(checkAst(input,expected,242))
  }


  test("243.") {
    val input =
      """int main(){
         a == 5;
         break;
         a = ((6 - a)/100 % b)*(c || (4 - 100));
       }
      """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            IntType,
            Block(
              List(),
              List(
                BinaryOp("==", Id("a"), IntLiteral(5)),
                Break,
                BinaryOp(
                  "=",
                  Id("a"),
                  BinaryOp(
                    "*",
                    BinaryOp(
                      "%",
                      BinaryOp(
                        "/",
                        BinaryOp("-", IntLiteral(6), Id("a")),
                        IntLiteral(100)
                      ),
                      Id("b")
                    ),
                    BinaryOp(
                      "||",
                      Id("c"),
                      BinaryOp("-", IntLiteral(4), IntLiteral(100))
                    )
                  )
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,243))
  }
  test("244. funcall") {
    val input =
       """
                   void main() {
                    putIntLn( i );
                   }"""
    val expected =
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                CallExpr(
                  Id("putIntLn"),
                  List(
                    Id("i")
                  )
                )
              )
            )
          )
        )
      ) 
    assert(checkAst(input,expected,244))
  }
  test("245. funcall") {
    val input =
       """
                   void main() {
                    main = f ();
                    putIntLn( i );
                    {
                      int i ;
                      putIntLn(main );
                    }
                   }"""
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("=", Id("main"), CallExpr(Id("f"), List())),
                CallExpr(Id("putIntLn"), List(Id("i"))),
                Block(
                  List(
                    VarDecl(Id("i"), IntType)
                  ),
                  List(
                    CallExpr(Id("putIntLn"), List(Id("main"))),
                  )
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,245))
  }

  test("246. program khong co gi ca") {
    val input =
      """  """
    val expected = 
      Program(
        List(
        )
      )
    assert(checkAst(input,expected,246))
  }


  test("247. test phep chia") {
    val input =
      """ void main(){
                        id[2]/5[7];
                        (2.8*!id)+8+9;
                        id[3]-5;
                        2---8;
                        
                      }  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("/", ArrayCell(Id("id"), IntLiteral(2)), ArrayCell(IntLiteral(5),IntLiteral(7))),
                BinaryOp("+", BinaryOp("+", BinaryOp("*", FloatLiteral(2.8.toFloat), UnaryOp("!", Id("id"))), IntLiteral(8)), IntLiteral(9)),
                BinaryOp("-", ArrayCell(Id("id"), IntLiteral(3)), IntLiteral(5)),
                BinaryOp("-", IntLiteral(2), UnaryOp("-", UnaryOp("-", IntLiteral(8))))                
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,247))
  }

  test("248. test UnaryOp") {
    val input =
      """ void main(){
                        id[2]/5[7];
                        (2.8*!id)+8+9;
                        id[3]-5;
                        2---8;
                        
                      }  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("/", ArrayCell(Id("id"), IntLiteral(2)), ArrayCell(IntLiteral(5),IntLiteral(7))),
                BinaryOp("+", BinaryOp("+", BinaryOp("*", FloatLiteral(2.8.toFloat), UnaryOp("!", Id("id"))), IntLiteral(8)), IntLiteral(9)),
                BinaryOp("-", ArrayCell(Id("id"), IntLiteral(3)), IntLiteral(5)),
                BinaryOp("-", IntLiteral(2), UnaryOp("-", UnaryOp("-", IntLiteral(8))))                
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,248))
  }
  test("249. test phep cong tru ") {
    val input =
      """ void main(){
                        id[2]/5[7];
                        (2.8*!id)+8+9;
                        id[3]-5;
                        2---8;
                        
                      }  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("/", ArrayCell(Id("id"), IntLiteral(2)), ArrayCell(IntLiteral(5),IntLiteral(7))),
                BinaryOp("+", BinaryOp("+", BinaryOp("*", FloatLiteral(2.8.toFloat), UnaryOp("!", Id("id"))), IntLiteral(8)), IntLiteral(9)),
                BinaryOp("-", ArrayCell(Id("id"), IntLiteral(3)), IntLiteral(5)),
                BinaryOp("-", IntLiteral(2), UnaryOp("-", UnaryOp("-", IntLiteral(8))))                
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,249))
  }
  test("250. thu nhieu dau tru voi nhau") {
    val input =
      """ void main(){
                        id[2]/5[7];
                        (2.8*!id)+8+9;
                        id[3]-5;
                        2---8;
                        
                      }  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("/", ArrayCell(Id("id"), IntLiteral(2)), ArrayCell(IntLiteral(5),IntLiteral(7))),
                BinaryOp("+", BinaryOp("+", BinaryOp("*", FloatLiteral(2.8.toFloat), UnaryOp("!", Id("id"))), IntLiteral(8)), IntLiteral(9)),
                BinaryOp("-", ArrayCell(Id("id"), IntLiteral(3)), IntLiteral(5)),
                BinaryOp("-", IntLiteral(2), UnaryOp("-", UnaryOp("-", IntLiteral(8))))                
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,250))
  }
  test("251. kiem tra array cell") {
    val input =
      """ void main(){
                        id[2]/5[7];
                        (2.8*!id)+8+9;
                        id[3]-5;
                        2---8;
                        
                      }  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("/", ArrayCell(Id("id"), IntLiteral(2)), ArrayCell(IntLiteral(5),IntLiteral(7))),
                BinaryOp("+", BinaryOp("+", BinaryOp("*", FloatLiteral(2.8.toFloat), UnaryOp("!", Id("id"))), IntLiteral(8)), IntLiteral(9)),
                BinaryOp("-", ArrayCell(Id("id"), IntLiteral(3)), IntLiteral(5)),
                BinaryOp("-", IntLiteral(2), UnaryOp("-", UnaryOp("-", IntLiteral(8))))                
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,251))
  }
  test("252. test araay cell phep tru") {
    val input =
      """ void main(){
                        id[2]/5[7];
                        (2.8*!id)+8+9;
                        id[3]-5;
                        2---8;
                        
                      }  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("/", ArrayCell(Id("id"), IntLiteral(2)), ArrayCell(IntLiteral(5),IntLiteral(7))),
                BinaryOp("+", BinaryOp("+", BinaryOp("*", FloatLiteral(2.8.toFloat), UnaryOp("!", Id("id"))), IntLiteral(8)), IntLiteral(9)),
                BinaryOp("-", ArrayCell(Id("id"), IntLiteral(3)), IntLiteral(5)),
                BinaryOp("-", IntLiteral(2), UnaryOp("-", UnaryOp("-", IntLiteral(8))))                
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,252))
  }
  test("253. 2 function khong tham so") {
    val input =
      """int i ;
                  int f () {
                    for(a*2; b = 2; b * 2)
                     a = 100;
                    return 200;
                  }
                  void main() {
                    int main ;
                    main = f();
                  }"""
    val expected = 
      Program(
        List(
          VarDecl(Id("i"), IntType),
          FuncDecl(
            Id("f"),
            List(),
            IntType,
            Block(
              List(),
              List(
                For(
                  BinaryOp("*", Id("a"), IntLiteral(2)),
                  BinaryOp("=", Id("b"), IntLiteral(2)),
                  BinaryOp("*", Id("b"), IntLiteral(2)),
                  BinaryOp("=", Id("a"), IntLiteral(100))
                ),
                Return(Some(IntLiteral(200)))
              )
            )
          ),
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(
                VarDecl(Id("main"), IntType)
              ),
              List(
                BinaryOp("=", Id("main"), CallExpr(Id("f"), List()))
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,253))
  }
  test("254. test ko ten") {
    val input =
      """ void main(){
                        id[2]/5[7];
                        (2.8*!id)+8+9;
                        id[3]-5;
                        2---8;
                        
                      }  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("/", ArrayCell(Id("id"), IntLiteral(2)), ArrayCell(IntLiteral(5),IntLiteral(7))),
                BinaryOp("+", BinaryOp("+", BinaryOp("*", FloatLiteral(2.8.toFloat), UnaryOp("!", Id("id"))), IntLiteral(8)), IntLiteral(9)),
                BinaryOp("-", ArrayCell(Id("id"), IntLiteral(3)), IntLiteral(5)),
                BinaryOp("-", IntLiteral(2), UnaryOp("-", UnaryOp("-", IntLiteral(8))))                
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,254))
  }
  test("255. giong test o tren") {
    val input =
      """ void main(){
                        id[2]/5[7];
                        (2.8*!id)+8+9;
                        id[3]-5;
                        2---8;
                        
                      }  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("/", ArrayCell(Id("id"), IntLiteral(2)), ArrayCell(IntLiteral(5),IntLiteral(7))),
                BinaryOp("+", BinaryOp("+", BinaryOp("*", FloatLiteral(2.8.toFloat), UnaryOp("!", Id("id"))), IntLiteral(8)), IntLiteral(9)),
                BinaryOp("-", ArrayCell(Id("id"), IntLiteral(3)), IntLiteral(5)),
                BinaryOp("-", IntLiteral(2), UnaryOp("-", UnaryOp("-", IntLiteral(8))))                
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,255))
  }
  test("256. giong test o duoi") {
    val input =
      """ void main(){
                        id[2]/5[7];
                        (2.8*!id)+8+9;
                        id[3]-5;
                        2---8;
                        
                      }  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("/", ArrayCell(Id("id"), IntLiteral(2)), ArrayCell(IntLiteral(5),IntLiteral(7))),
                BinaryOp("+", BinaryOp("+", BinaryOp("*", FloatLiteral(2.8.toFloat), UnaryOp("!", Id("id"))), IntLiteral(8)), IntLiteral(9)),
                BinaryOp("-", ArrayCell(Id("id"), IntLiteral(3)), IntLiteral(5)),
                BinaryOp("-", IntLiteral(2), UnaryOp("-", UnaryOp("-", IntLiteral(8))))                
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,256))
  }
  test("257. voi type va intype test") {
    val input =
      """ void main(){
                        id[2]/5[7];
                        (2.8*!id)+8+9;
                        id[3]-5;
                        2---8;
                        
                      }  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("/", ArrayCell(Id("id"), IntLiteral(2)), ArrayCell(IntLiteral(5),IntLiteral(7))),
                BinaryOp("+", BinaryOp("+", BinaryOp("*", FloatLiteral(2.8.toFloat), UnaryOp("!", Id("id"))), IntLiteral(8)), IntLiteral(9)),
                BinaryOp("-", ArrayCell(Id("id"), IntLiteral(3)), IntLiteral(5)),
                BinaryOp("-", IntLiteral(2), UnaryOp("-", UnaryOp("-", IntLiteral(8))))                
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,257))
  }
  test("258. hai tram nam muoi tam") {
    val input =
      """ void main(){
                        id[2]/5[7];
                        (2.8*!id)+8+9;
                        id[3]-5;
                        2---8;
                        
                      }  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("/", ArrayCell(Id("id"), IntLiteral(2)), ArrayCell(IntLiteral(5),IntLiteral(7))),
                BinaryOp("+", BinaryOp("+", BinaryOp("*", FloatLiteral(2.8.toFloat), UnaryOp("!", Id("id"))), IntLiteral(8)), IntLiteral(9)),
                BinaryOp("-", ArrayCell(Id("id"), IntLiteral(3)), IntLiteral(5)),
                BinaryOp("-", IntLiteral(2), UnaryOp("-", UnaryOp("-", IntLiteral(8))))                
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,258))
  }
  test("259. test id co tham so la int") {
    val input =
      """ void main(){
                        id[2]/5[7];
                        (2.8*!id)+8+9;
                        id[3]-5;
                        2---8;
                        
                      }  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("/", ArrayCell(Id("id"), IntLiteral(2)), ArrayCell(IntLiteral(5),IntLiteral(7))),
                BinaryOp("+", BinaryOp("+", BinaryOp("*", FloatLiteral(2.8.toFloat), UnaryOp("!", Id("id"))), IntLiteral(8)), IntLiteral(9)),
                BinaryOp("-", ArrayCell(Id("id"), IntLiteral(3)), IntLiteral(5)),
                BinaryOp("-", IntLiteral(2), UnaryOp("-", UnaryOp("-", IntLiteral(8))))                
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,259))
  }
  test("260. ham co nhieu unary") {
    val input =
      """ void main(){
                        id[2]/5[7];
                        (2.8*!id)+8+9;
                        id[3]-5;
                        2---8;
                        
                      }  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("/", ArrayCell(Id("id"), IntLiteral(2)), ArrayCell(IntLiteral(5),IntLiteral(7))),
                BinaryOp("+", BinaryOp("+", BinaryOp("*", FloatLiteral(2.8.toFloat), UnaryOp("!", Id("id"))), IntLiteral(8)), IntLiteral(9)),
                BinaryOp("-", ArrayCell(Id("id"), IntLiteral(3)), IntLiteral(5)),
                BinaryOp("-", IntLiteral(2), UnaryOp("-", UnaryOp("-", IntLiteral(8))))                
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,260))
  }
  test("261. test dau ngoac tron") {
    val input =
      """ void main(){
                        id[2]/5[7];
                        (2.8*!id)+8+9;
                        id[3]-5;
                        2---8;
                        
                      }  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("/", ArrayCell(Id("id"), IntLiteral(2)), ArrayCell(IntLiteral(5),IntLiteral(7))),
                BinaryOp("+", BinaryOp("+", BinaryOp("*", FloatLiteral(2.8.toFloat), UnaryOp("!", Id("id"))), IntLiteral(8)), IntLiteral(9)),
                BinaryOp("-", ArrayCell(Id("id"), IntLiteral(3)), IntLiteral(5)),
                BinaryOp("-", IntLiteral(2), UnaryOp("-", UnaryOp("-", IntLiteral(8))))                
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,261))
  }
  test("262. test function don gian") {
    val input =
      """ void main(){
                        id[2]/5[7];
                        (2.8*!id)+8+9;
                        id[3]-5;
                        2---8;
                        
                      }  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("/", ArrayCell(Id("id"), IntLiteral(2)), ArrayCell(IntLiteral(5),IntLiteral(7))),
                BinaryOp("+", BinaryOp("+", BinaryOp("*", FloatLiteral(2.8.toFloat), UnaryOp("!", Id("id"))), IntLiteral(8)), IntLiteral(9)),
                BinaryOp("-", ArrayCell(Id("id"), IntLiteral(3)), IntLiteral(5)),
                BinaryOp("-", IntLiteral(2), UnaryOp("-", UnaryOp("-", IntLiteral(8))))                
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,262))
  }

  test("263. turn off computer") {
     val input = """int main()
{
   string ch;

   scanf("%c", ch);

   if (ch == "y" || ch == "Y")
      system("C:WINDOWSSystem32shutdown -s");

   return 0;
}"""
    val expected = 
Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(VarDecl(Id("ch"),StringType)),
  List(CallExpr(Id("scanf"),List(StringLiteral("%c"),Id("ch"))),If(BinaryOp("||" ,
    BinaryOp("==" ,Id("ch"),StringLiteral("y")),BinaryOp("==" ,Id("ch"),StringLiteral("Y"))),
  CallExpr(Id("system"),List(StringLiteral("C:WINDOWSSystem32shutdown -s"))),None),Return(Some(IntLiteral(0))))))))    
assert(checkAst(input,expected,263))
  }


  test("264. get ip andress") {
      val input = """ int main()
{
   system("C:\\WindowsSystem32ipconfig");

   return 0;
} """
    val expected = 
Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(),List(CallExpr(Id("system"),List(StringLiteral("C:\\\\WindowsSystem32ipconfig"))),
  Return(Some(IntLiteral(0))))))))    
assert(checkAst(input,expected,264))
  }

  test("265. rander") {
     val input = """ int main() {
  int c, n;

  printf("Ten random numbers in [1,100]");

  for (c = 1; c <= 10; c = c+1) {
    n = rand() % 100 + 1;
  }

  return 0;
} """
    val expected = 
Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(VarDecl(Id("c"),IntType),VarDecl(Id("n"),IntType)),List(CallExpr(Id("printf"),
  List(StringLiteral("Ten random numbers in [1,100]"))),For(BinaryOp("=" ,Id("c"),IntLiteral(1)),BinaryOp("<=" ,Id("c"),IntLiteral(10)),
  BinaryOp("=" ,Id("c"),BinaryOp("+" ,Id("c"),IntLiteral(1))),Block(List(),List(BinaryOp("=" ,Id("n"),
  BinaryOp("+" ,BinaryOp("%" ,CallExpr(Id("rand"),List()),IntLiteral(100)),IntLiteral(1)))))),Return(Some(IntLiteral(0))))))))   
assert(checkAst(input,expected,265))
  }

  test("266. delete file") {
    val input = """int main()
  {
   int status;
   string file_name[25];

   status = remove(file_name);

   if( status == 0 )
      printf("%s file deleted successfully.",file_name);
   else
   {
      printf("Unable to delete the file");
      perror("Error");
   }

   return 0;
  } """
    val expected = 
  Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(VarDecl(Id("status"),IntType),VarDecl(Id("file_name"),
  ArrayType(IntLiteral(25),StringType))),List(BinaryOp("=" ,Id("status"),CallExpr(Id("remove"),List(Id("file_name")))),
  If(BinaryOp("==" ,Id("status"),IntLiteral(0)),CallExpr(Id("printf"),
  List(StringLiteral("%s file deleted successfully."),Id("file_name"))),Some(Block(List(),
  List(CallExpr(Id("printf"),List(StringLiteral("Unable to delete the file"))),CallExpr(Id("perror"),List(StringLiteral("Error"))))))),Return(Some(IntLiteral(0))))))))  
assert(checkAst(input,expected,266))
  }
  test("267.") {
    val input =
      """ void main(){
                        id[2]/5[7];
                        (2.8*!id)+8+9;
                        id[3]-5;
                        2---8;
                        
                      }  """
    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                BinaryOp("/", ArrayCell(Id("id"), IntLiteral(2)), ArrayCell(IntLiteral(5),IntLiteral(7))),
                BinaryOp("+", BinaryOp("+", BinaryOp("*", FloatLiteral(2.8.toFloat), UnaryOp("!", Id("id"))), IntLiteral(8)), IntLiteral(9)),
                BinaryOp("-", ArrayCell(Id("id"), IntLiteral(3)), IntLiteral(5)),
                BinaryOp("-", IntLiteral(2), UnaryOp("-", UnaryOp("-", IntLiteral(8))))                
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,267))
  }

  test("268. turn off computer") {
     val input = """int main()
{
   string ch;

   scanf("%c", ch);

   if (ch == "y" || ch == "Y")
      system("C:WINDOWSSystem32shutdown -s");

   return 0;
}"""
    val expected = 
Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(VarDecl(Id("ch"),StringType)),
  List(CallExpr(Id("scanf"),List(StringLiteral("%c"),Id("ch"))),If(BinaryOp("||" ,
    BinaryOp("==" ,Id("ch"),StringLiteral("y")),BinaryOp("==" ,Id("ch"),StringLiteral("Y"))),
  CallExpr(Id("system"),List(StringLiteral("C:WINDOWSSystem32shutdown -s"))),None),Return(Some(IntLiteral(0))))))))    
assert(checkAst(input,expected,268))
  }


  test("269. get ip andress") {
      val input = """ int main()
{
   system("C:\\WindowsSystem32ipconfig");

   return 0;
} """
    val expected = 
Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(),List(CallExpr(Id("system"),List(StringLiteral("C:\\\\WindowsSystem32ipconfig"))),
  Return(Some(IntLiteral(0))))))))    
assert(checkAst(input,expected,269))
  }

  test("270. rander") {
     val input = """ int main() {
  int c, n;

  printf("Ten random numbers in [1,100]");

  for (c = 1; c <= 10; c = c+1) {
    n = rand() % 100 + 1;
  }

  return 0;
} """
    val expected = 
Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(VarDecl(Id("c"),IntType),VarDecl(Id("n"),IntType)),List(CallExpr(Id("printf"),
  List(StringLiteral("Ten random numbers in [1,100]"))),For(BinaryOp("=" ,Id("c"),IntLiteral(1)),BinaryOp("<=" ,Id("c"),IntLiteral(10)),
  BinaryOp("=" ,Id("c"),BinaryOp("+" ,Id("c"),IntLiteral(1))),Block(List(),List(BinaryOp("=" ,Id("n"),
  BinaryOp("+" ,BinaryOp("%" ,CallExpr(Id("rand"),List()),IntLiteral(100)),IntLiteral(1)))))),Return(Some(IntLiteral(0))))))))   
assert(checkAst(input,expected,270))
  }

  test("271. delete file") {
    val input = """int main()
  {
   int status;
   string file_name[25];

   status = remove(file_name);

   if( status == 0 )
      printf("%s file deleted successfully.",file_name);
   else
   {
      printf("Unable to delete the file");
      perror("Error");
   }

   return 0;
  } """
    val expected = 
  Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(VarDecl(Id("status"),IntType),VarDecl(Id("file_name"),
  ArrayType(IntLiteral(25),StringType))),List(BinaryOp("=" ,Id("status"),CallExpr(Id("remove"),List(Id("file_name")))),
  If(BinaryOp("==" ,Id("status"),IntLiteral(0)),CallExpr(Id("printf"),
  List(StringLiteral("%s file deleted successfully."),Id("file_name"))),Some(Block(List(),
  List(CallExpr(Id("printf"),List(StringLiteral("Unable to delete the file"))),CallExpr(Id("perror"),List(StringLiteral("Error"))))))),Return(Some(IntLiteral(0))))))))  
assert(checkAst(input,expected,271))
  }
  test("272.list files") {
    val input = """ int main()
  {
   int done;
   int a;

   printf("Press any key to view the files in the current directory");


   done = findfirst("*.*",a,0);

   {
      printf("%s",a,ff_name);
      done = findnext(a);
   }

   getch();
   return 0;
  } """
    val expect = 
      Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(VarDecl(Id("done"),IntType),VarDecl(Id("a"),IntType)),
        List(CallExpr(Id("printf"),List(StringLiteral("Press any key to view the files in the current directory"))),
          BinaryOp("=" ,Id("done"),CallExpr(Id("findfirst"),List(StringLiteral("*.*"),Id("a"),IntLiteral(0)))),
          Block(List(),List(CallExpr(Id("printf"),List(StringLiteral("%s"),
        Id("a"),Id("ff_name"))),BinaryOp("=" ,Id("done"),CallExpr(Id("findnext"),List(Id("a")))))),CallExpr(Id("getch"),List()),Return(Some(IntLiteral(0))))))))
    assert(checkAst(input,expect,272))
  }
  test("273. test ifsta phuc tap") {
    val input =
      """void test(int a, string b){
          if(a == b) {
            a = a + 2;
            b = b - 2;
          }
           else{
            a = a / b;
            b = b + 4;
          }
          return;
         }"""

    val expected = 
      Program(
        List(
          FuncDecl(
            Id("test"),
            List(
              VarDecl(Id("a"),IntType),
              VarDecl(Id("b"),StringType)
            ),
            VoidType,
            Block(
              List(),
              List(
                If(
                  BinaryOp("==", Id("a"), Id("b")),
                  Block(
                    List(),
                    List(
                      BinaryOp("=", Id("a"), BinaryOp("+", Id("a"), IntLiteral(2))),
                      BinaryOp("=", Id("b"), BinaryOp("-", Id("b"), IntLiteral(2))),                      
                    )
                  ),
                  Some(
                    Block(
                      List(),
                      List(
                        BinaryOp("=", Id("a"), BinaryOp("/", Id("a"), Id("b"))),
                        BinaryOp("=", Id("b"), BinaryOp("+", Id("b"), IntLiteral(4))),   
                      )
                    )
                  )
                ),
                Return(None)
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,273))
  }
  test("274. test ifsta phuc tap co ca vardecla") {
    val input =
      """int a,b,c;
        string s;
        int foo(int i, float a[]){
          a = b = c = 2;
          if((3 + 2) = 5)
            b = (5 + 6)%a;
          else
            b = 0;
         return 0;
        }"""

    val expected = 
      Program(
        List(
          VarDecl(Id("a"), IntType),
          VarDecl(Id("b"), IntType),
          VarDecl(Id("c"), IntType),
          VarDecl(Id("s"), StringType),
          FuncDecl(
            Id("foo"),
            List(
              VarDecl(Id("i"),IntType),
              VarDecl(Id("a"),ArrayPointerType(FloatType))
            ),
            IntType,
            Block(
              List(),
              List(
                BinaryOp(
                  "=", 
                  Id("a"), 
                  BinaryOp(
                    "=", 
                    Id("b"), 
                    BinaryOp("=", Id("c"), IntLiteral(2))
                  )
                ), 
                If(
                  BinaryOp("=", BinaryOp("+", IntLiteral(3), IntLiteral(2)), IntLiteral(5)),
                  BinaryOp(
                    "=",
                    Id("b"), 
                    BinaryOp("%", BinaryOp("+", IntLiteral(5), IntLiteral(6)), Id("a"))
                  ),
                  Some(
                    BinaryOp("=", Id("b"), IntLiteral(0))
                  )
                ),
                Return(Some(IntLiteral(0)))
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,274))
  }
  test("275. test ifsta rut gon") {
    val input =
      """float[] foo(float a[], boolean c){
          if(a) c = c + 4;
          return a;
          }"""

    val expected = 
      Program(
        List(
          FuncDecl(
            Id("foo"),
            List(
              VarDecl(Id("a"),ArrayPointerType(FloatType)),
              VarDecl(Id("c"), BoolType)
            ),
            ArrayPointerType(FloatType),
            Block(
              List(),
              List(
                If(
                  Id("a"),
                  BinaryOp(
                    "=",
                    Id("c"),
                    BinaryOp("+", Id("c"), IntLiteral(4))
                  ),
                  None
                ),
                Return(Some(Id("a")))
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,275))
  }
  test("276. test ifsta rut gon 2") {
    val input =
      """int foo(){
          if(a) b = false;
          else c = true;
        }"""

    val expected = 
      Program(
        List(
          FuncDecl(
            Id("foo"),
            List(),
            IntType,
            Block(
              List(),
              List(
                If(
                  Id("a"),
                  BinaryOp(
                    "=",
                    Id("b"),
                    BooleanLiteral(false)
                  ),
                  Some(
                    BinaryOp(
                      "=",
                      Id("c"),
                      BooleanLiteral(true)
                    )
                  )
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,276))
  }
  test("277. test do while don gian") {
    val input =
      """int foo(){
          do
            a;
            2;
            2%3;
          while b;
        }"""

    val expected = 
      Program(
        List(
          FuncDecl(
            Id("foo"),
            List(),
            IntType,
            Block(
              List(),
              List(
                Dowhile(
                  List(
                    Id("a"),
                    IntLiteral(2),
                    BinaryOp("%", IntLiteral(2), IntLiteral(3))
                  ),
                  Id("b")
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,277))
  }
  test("278. test do while phuc tap") {
    val input =
      """float foo(float a, float b){
          do
            if (a) b;
            if (a % 2 == 5) 2 + 1;
              else (a * 2 - 4 * 3 *( 4 - 2));
          while b!=2;
        }"""

    val expected = 
      Program(
        List(
          FuncDecl(
            Id("foo"),
            List(
              VarDecl(Id("a"),FloatType),
              VarDecl(Id("b"),FloatType)
            ),
            FloatType,
            Block(
              List(),
              List(
                Dowhile(
                  List(
                    If(Id("a"), Id("b"), None),
                    If(
                      BinaryOp("==", BinaryOp("%", Id("a"), IntLiteral(2)), IntLiteral(5) ),
                      BinaryOp("+", IntLiteral(2), IntLiteral(1)),
                      Some(
                        BinaryOp(
                          "-",
                          BinaryOp("*", Id("a"), IntLiteral(2)),
                          BinaryOp(
                            "*",
                            BinaryOp("*", IntLiteral(4), IntLiteral(3)),
                            BinaryOp("-", IntLiteral(4), IntLiteral(2))
                          ) 
                        )
                      )
                    )
                  ),
                  BinaryOp("!=",Id("b"), IntLiteral(2))
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,278))
  }
  test("279. test do while long do while") {
    val input =
      """string a, b, c;
        void main(){
          float d;
          int e;
          boolean f;
          int a[5];
          do
            do
              a + 2;
              f = true;
            while c;
          while 2;
        }"""

    val expected = 
      Program(
        List(
          VarDecl(Id("a"),StringType),
          VarDecl(Id("b"),StringType),
          VarDecl(Id("c"),StringType),
          FuncDecl(
            Id("main"),
            List(),
            VoidType,
            Block(
              List(
                VarDecl(Id("d"),FloatType),
                VarDecl(Id("e"),IntType),
                VarDecl(Id("f"),BoolType),
                VarDecl(Id("a"),ArrayType(IntLiteral(5), IntType))
              ),
              List(
                Dowhile(
                  List(
                    Dowhile(
                      List(
                        BinaryOp("+", Id("a"), IntLiteral(2)),
                        BinaryOp("=", Id("f"), BooleanLiteral(true))
                      ),
                    Id("c")  
                    )
                  ),
                  IntLiteral(2)
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,279))
  }
  test("280. test do while long if") {
    val input =
      """boolean test(){
          int a;
          boolean b,c,d;
          do
            if (a == 5) 5;
            else{
              if (5) 6;
              do (a+2);
              while a==4;
              c=5;
            }
          while a==6;
        }"""

    val expected = 
      Program(
        List(
          FuncDecl(
            Id("test"),
            List(),
            BoolType,
            Block(
              List(
                VarDecl(Id("a"),IntType),
                VarDecl(Id("b"),BoolType),
                VarDecl(Id("c"),BoolType),
                VarDecl(Id("d"),BoolType)
              ),
              List(
                Dowhile(
                  List(
                    If(
                      BinaryOp("==", Id("a"), IntLiteral(5)),
                      IntLiteral(5),
                      Some(
                        Block(
                          List(),
                          List(
                            If(IntLiteral(5), IntLiteral(6), None),
                            Dowhile(
                            List(
                              BinaryOp("+", Id("a"), IntLiteral(2))
                            ),
                            BinaryOp("==", Id("a"), IntLiteral(4))
                            ),
                            BinaryOp("=", Id("c"), IntLiteral(5))
                          )
                        )
                      )
                    )
                  ),
                  BinaryOp("==", Id("a"), IntLiteral(6))
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,280))
  }
  test("281. test do while tiep tuc") {
    val input =
      """int main(string args){
          do{
            a + 2;
          }while(a<10);
        }"""

    val expected = 
      Program(
        List(
          FuncDecl(
            Id("main"),
            List(
              VarDecl(Id("args"), StringType)
            ),
            IntType,
            Block(
              List(),
              List(
                Dowhile(
                  List(
                    Block(
                      List(),
                      List(
                        BinaryOp("+", Id("a"), IntLiteral(2))
                      )
                    )
                  ),
                  BinaryOp("<",Id("a"), IntLiteral(10))
                )
              )
            )  
          )
        )
      )
    assert(checkAst(input,expected,281))
  }

  test("282. test vong for don gian") {
    val input =
      """int a,b,c;
        void foo(){
          for(a < 1; a < 4; a + 2)
            a = a + 1;
        }
        """
    val expected = 
      Program(
        List(
          VarDecl(Id("a"),IntType),
          VarDecl(Id("b"),IntType),
          VarDecl(Id("c"),IntType),
          FuncDecl(
            Id("foo"),
            List(),
            VoidType,
            Block(
              List(),
              List(
                For(
                  BinaryOp("<",Id("a"),IntLiteral(1)),
                  BinaryOp("<",Id("a"),IntLiteral(4)),
                  BinaryOp("+",Id("a"),IntLiteral(2)),
                  BinaryOp("=",Id("a"),BinaryOp("+",Id("a"),IntLiteral(1)))
                )
              )
            )
          )
        )
      )
    assert(checkAst(input,expected,282))
  }
  test("283. test for Statement 2") {
    val input =
      """int i;
        boolean c;
        string s;
        void foo(int a, float b[]){
          for(i = 0; i < 100;i + 1)
            b[2] = b[i] + 2;
          b[50];
        }
      """
    val expect = 
      Program(
        List(
          VarDecl(Id("i"),IntType),
          VarDecl(Id("c"),BoolType),
          VarDecl(Id("s"),StringType),
          FuncDecl(
            Id("foo"),
            List(
              VarDecl(Id("a"), IntType),
              VarDecl(Id("b"), ArrayPointerType(FloatType))
            ),
            VoidType,
            Block(
              List(),
              List(
                For(
                  BinaryOp("=", Id("i"), IntLiteral(0)),
                  BinaryOp("<", Id("i"), IntLiteral(100)),
                  BinaryOp("+", Id("i"), IntLiteral(1)),
                  BinaryOp(
                    "=",
                    ArrayCell(Id("b"), IntLiteral(2)),
                    BinaryOp(
                      "+",
                      ArrayCell(Id("b"), Id("i")),
                      IntLiteral(2)
                    )
                  )
                ),
                ArrayCell(Id("b"), IntLiteral(50))
              )
            )
          )
        )
      )
    assert(checkAst(input,expect,283))
  }

  test("284. copy") {
    val input = """ int main()
{
   string ch, source_file[20], target_file[20];
   int source, target;

   if( source == NULL )
   {
      printf("Press any key to exit...\\\n");
      exit(EXIT_FAILURE);
   }

   printf("Enter name of target file");
   gets(target_file);

   target = fopen(target_file, "w");

   if( target == NULL )
   {
      fclose(source);
      printf("Press any key to exit...");
      exit(EXIT_FAILURE);
   }

   fclose(source);
   fclose(target);

   return 0;
} """
    val expect = 
      Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(VarDecl(Id("ch"),StringType),VarDecl(Id("source_file"),
        ArrayType(IntLiteral(20),StringType)),VarDecl(Id("target_file"),ArrayType(IntLiteral(20),StringType)),
    VarDecl(Id("source"),IntType),VarDecl(Id("target"),IntType)),List(If(BinaryOp("==" ,Id("source"),Id("NULL")),
      Block(List(),List(CallExpr(Id("printf"),List(StringLiteral("""Press any key to exit...\\\n"""))),CallExpr(Id("exit"),
        List(Id("EXIT_FAILURE"))))),None),CallExpr(Id("printf"),List(StringLiteral("Enter name of target file"))),
    CallExpr(Id("gets"),List(Id("target_file"))),BinaryOp("=" ,Id("target"),CallExpr(Id("fopen"),List(Id("target_file"),
      StringLiteral("w")))),If(BinaryOp("==" ,Id("target"),Id("NULL")),Block(List(),List(CallExpr(Id("fclose"),
        List(Id("source"))),CallExpr(Id("printf"),List(StringLiteral("Press any key to exit..."))),CallExpr(Id("exit"),
        List(Id("EXIT_FAILURE"))))),None),CallExpr(Id("fclose"),List(Id("source"))),CallExpr(Id("fclose"),List(Id("target"))),
      Return(Some(IntLiteral(0))))))))
    assert(checkAst(input,expect,284))
  }
    test("285. test trong group 16") {
    val input =
      "int main(){ g[f[2]] = 5;}"
    val expected = 
      Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(),List(BinaryOp("=",ArrayCell(Id("g"),ArrayCell(Id("f"),IntLiteral(2))),IntLiteral(5)))))))
    assert(checkAst(input,expected,285))
  }
  test("286. test trong group 15") {
    val input =
      "int main(){ g[f[2]] = 5;}"
    val expected = 
      Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(),List(BinaryOp("=",ArrayCell(Id("g"),ArrayCell(Id("f"),IntLiteral(2))),IntLiteral(5)))))))
    assert(checkAst(input,expected,286))
  }
  test("287. test trong group 14") {
    val input =
      "int main(){ g[f[2]] = 5;}"
    val expected = 
      Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(),List(BinaryOp("=",ArrayCell(Id("g"),ArrayCell(Id("f"),IntLiteral(2))),IntLiteral(5)))))))
    assert(checkAst(input,expected,287))
  }
  test("288. test trong group 13") {
    val input =
      "int main(){ g[f[2]] = 5;}"
    val expected = 
      Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(),List(BinaryOp("=",ArrayCell(Id("g"),ArrayCell(Id("f"),IntLiteral(2))),IntLiteral(5)))))))
    assert(checkAst(input,expected,288))
  }
  test("289. test trong group 12") {
    val input =
      "int main(){ g[f[2]] = 5;}"
    val expected = 
      Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(),List(BinaryOp("=",ArrayCell(Id("g"),ArrayCell(Id("f"),IntLiteral(2))),IntLiteral(5)))))))
    assert(checkAst(input,expected,289))
  }
  test("290. test trong group 11") {
    val input =
      "int main(){ g[f[2]] = 5;}"
    val expected = 
      Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(),List(BinaryOp("=",ArrayCell(Id("g"),ArrayCell(Id("f"),IntLiteral(2))),IntLiteral(5)))))))
    assert(checkAst(input,expected,290))
  }
  test("291. test trong group 10") {
    val input =
      "int main(){ g[f[2]] = 5;}"
    val expected = 
      Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(),List(BinaryOp("=",ArrayCell(Id("g"),ArrayCell(Id("f"),IntLiteral(2))),IntLiteral(5)))))))
    assert(checkAst(input,expected,291))
  }
  test("292. test trong group 9") {
    val input =
      "int main(){ g[f[2]] = 5;}"
    val expected = 
      Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(),List(BinaryOp("=",ArrayCell(Id("g"),ArrayCell(Id("f"),IntLiteral(2))),IntLiteral(5)))))))
    assert(checkAst(input,expected,292))
  }
  test("293. test trong group 8") {
    val input =
      "int main(){ g[f[2]] = 5;}"
    val expected = 
      Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(),List(BinaryOp("=",ArrayCell(Id("g"),ArrayCell(Id("f"),IntLiteral(2))),IntLiteral(5)))))))
    assert(checkAst(input,expected,293))
  }
  test("294. test trong group 7") {
    val input =
      "int main(){ g[f[2]] = 5;}"
    val expected = 
      Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(),List(BinaryOp("=",ArrayCell(Id("g"),ArrayCell(Id("f"),IntLiteral(2))),IntLiteral(5)))))))
    assert(checkAst(input,expected,294))
  }
  test("295. test trong group 6") {
    val input =
      "int main(){ g[f[2]] = 5;}"
    val expected = 
      Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(),List(BinaryOp("=",ArrayCell(Id("g"),ArrayCell(Id("f"),IntLiteral(2))),IntLiteral(5)))))))
    assert(checkAst(input,expected,295))
  }
  test("296. test trong group 5") {
    val input =
      "int main(){ g[f[2]] = 5;}"
    val expected = 
      Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(),List(BinaryOp("=",ArrayCell(Id("g"),ArrayCell(Id("f"),IntLiteral(2))),IntLiteral(5)))))))
    assert(checkAst(input,expected,296))
  }

  test("297. test trong group 4") {
    val input =
      "int main(){ g[f[2]] = 5;}"
    val expected = 
      Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(),List(BinaryOp("=",ArrayCell(Id("g"),ArrayCell(Id("f"),IntLiteral(2))),IntLiteral(5)))))))
    assert(checkAst(input,expected,297))
  }

  test("298. test trong group 3") {
    val input =
    """int[] main (int a, float b) {
    return;
    }"""
    val expected = 
      Program(List(FuncDecl(Id("main"),List(VarDecl(Id("a"),IntType),VarDecl(Id("b"),FloatType)),ArrayPointerType(IntType),Block(List(),List(Return(None))))))
    assert(checkAst(input,expected,298))
  }
  test("299. test trong group 2") {
      val input = """float[] abc(int a, float b, string c){
        x=x+1.04e412;
      }"""
    val expected = Program(List(FuncDecl(Id("abc"),List(VarDecl(Id("a"),IntType),VarDecl(Id("b"),FloatType),VarDecl(Id("c"),StringType)),ArrayPointerType(FloatType),Block(List(),List(BinaryOp("=",Id("x"),BinaryOp("+",Id("x"),FloatLiteral(Float.PositiveInfinity))))))))
    assert(checkAst(input,expected,299))
  }  

  test("300. test trong group 1") {
    val input =
      """int main(){
        "\n";
        }"""
    val expected = Program(List(FuncDecl(Id("main"),List(),IntType,Block(List(),List(StringLiteral("""\n"""))))))
    assert(checkAst(input,expected,300))
  }
}

