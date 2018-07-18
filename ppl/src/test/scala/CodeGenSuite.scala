import org.scalatest.FunSuite

/**
  * Created by nhphung on 4/30/17.
  */
class CodeGenSuite extends FunSuite with TestCodeGen {
  test("1. two simple method") {
    val input = "void main () {foo(125); } void foo(int a){ putIntLn(a);}"
    val expected = "125"
    assert(checkCode(input,expected,502))
  }
  test("2. simple program and binaryop") {
    val input = """ int a[3]; void main() {  a[0] = 5 * 5 + 10; a[1] = a[a[2]];  putIntLn(a[1]); }    """
    val expected = "35"
    assert(checkCode(input,expected,501))
  }
  
  test("3. print int by putFloatLn") {
    val input = """
       int i ;
    void main () {
      float a ;
      a = 1;
      putFloatLn(a);
    } """
    val expected = "1.0"
    assert(checkCode(input,expected,503))
  }
  test("4. test tren group") {
    val input = """
    void main () {
      putStringLn("abc");
    } """
    val expected = "abc"
    assert(checkCode(input,expected,504))
  }
  test("5. global variable int") {
    val input = """
    int a;
    void main () {
      putInt(a);
    } """
    val expected = "0"
    assert(checkCode(input,expected,505))
  }
  test("6. global variable float") {
    val input = """
    float a;
    void main () {
      //a = getFloat();
      putFloat(a);
    } """
    val expected = "0.0"
    assert(checkCode(input,expected,506))
  }
  test("7. global variable string") {
    val input = """
    string a;
    void main () {
      //a = getFloat();
      putString(a);
    } """
    val expected = "null"
    assert(checkCode(input,expected,507))
  }
  test("8. global variable arr") {
    val input = """
    string a[5];
    boolean b[4];
    void main () {
      putString(a[4]);
      putBoolLn(b[3]);
    } """
    val expected = "nullfalse"
    assert(checkCode(input,expected,508))
  }
  test("9. int function foo") {
    val input = """
    string a[5];
    boolean b[4];
    void main () {
      putString(a[4]);
      putBoolLn(b[3]);
    } """
    val expected = "nullfalse"
    assert(checkCode(input,expected,509))
  }
  test("10. int function foo") {
    val input = """
    int a ;
    void main () {
      putInt(foo());
    } 
    int foo(){
      return 5;
      putInt(a);
    }
    """
    val expected = "5"
    assert(checkCode(input,expected,510))
  }
  test("11. if statement1") {
    val input = """
    int a ;
    void main () {
      a = 1;
      if (a >= 0)
        if (a > 5) putString("lon hon 5");
        else putString("be hon 5 lon hon -1");
      else 
        if (a < -5) putString("be hon -5");
        else putString("lon hon -5 be hon 0");  
    } 
    """
    val expected = "be hon 5 lon hon -1"
    assert(checkCode(input,expected,511))
  }
  test("12. if statement2") {
    val input = """
    int a ;
    void main () {
      a = --(-1 + 2 * -4 + 6);
      if (a >= 0)
        if (a > 5) putString("lon hon 5");
        else putString("be hon 5 lon hon -1");
      else 
        if (a < -5) putString("be hon -5");
        else putString("lon hon -5 be hon 0");  
    } 
    """
    val expected = "lon hon -5 be hon 0"
    assert(checkCode(input,expected,512))
  }
  test("13. for statement1") {
    val input = """
    int a ;
    void main () {
      a = --(-1 + 2 * -4 + 6);
      for (a = a; a > 10; a = a + 1)
        a = 0;
      putIntLn(a)  ;
    } 
    """
    val expected = "-3"
    assert(checkCode(input,expected,513))
  }
  test("14. for statement2") {
    val input = """
    int a ;
    void main () {
      int i;
      a = 1;
      for (i = 1; i < 10; i = i + 1)
        a = a * 2;
      putIntLn(a)  ;
    } 
    """
    val expected = "512"
    assert(checkCode(input,expected,514))
  }
  test("15. for statement2") {
    val input = """
    int a ;
    void main () {
      int i;
      a = 1;
      for (i = 1; i < 10; i = i + 1)
        a = a * 2;
      putIntLn(a)  ;
    } 
    """
    val expected = "512"
    assert(checkCode(input,expected,515))
  }
  test("16. do while statement 1") {
    val input = """
    int a ;
    void main () {
      int i;
      a = 1;
      for (i = 1; i < 10; i = i + 1)
        {
          a = a - 1;
          a = a + 1;
          a = --------a * 2;
        }
      putIntLn(a)  ;
    } 
    """
    val expected = "512"
    assert(checkCode(input,expected,516))
  }
  test("17. test tren lop") {
    val input = """
    int a ;
    void main () {
      putInt(foo(1,2.5));
    } 
    int foo(int a, float b){
      if (a == b)
        return 1;
      else return 0;
    }
    string[] foo2(int a, int b){
    }
    """
    val expected = "0"
    assert(checkCode(input,expected,517))
  }
  test("18. test break for") {
    val input = """
    int a ;
    void main () {
      for (a = 10; a > 0 ; a = a - 1)
      {
        if (a == 4) break;
      }
      putIntLn(a);

    } 
    
    """
    val expected = "4"
    assert(checkCode(input,expected,518))
  }
  test("19. test continue for") {
    val input = """
    void main () {
      int a ;
      int i;
      i = 1;
      for (a = 10; a > 0 ; a = a - 1)
      {
       if (a == 4) continue;
        i = i * 2;
      }
      putIntLn(i);

    } 
    
    """
    val expected = "512"
    assert(checkCode(input,expected,519))
  }
  test("20. test continue for") {
    val input = """
    void main () {
      putFloatLn(foo());
    } 
    float foo(){
      return 3;
    }
    
    """
    val expected = "3.0"
    assert(checkCode(input,expected,520))
  }
  test("21. test putFloat int value") {
    val input = """
    float a;
    void main () {
      float a; a =1;
      {
        putFloatLn(foo(2,a) );
      }
    } 
    int foo(float a, float b){
      {
        {
          {

          }
        }
      return -2;
      }
    }
    
    """
    val expected = "-2.0"
    assert(checkCode(input,expected,521))
  }
  test("22. test short-cicruit") {
    val input = """
    void main () {
      boolean a;
      a = (1 < 2) || (2 > 3) || (3 == 4) ;
      putBoolLn(a);
    } 
    """
    val expected = "true"
    assert(checkCode(input,expected,522))
  }

  test("23. test do while") {
    val input = """
    boolean a;
    void main () {
      int b;
      do
        b = 2;
      while(a);
      putIntLn(b);
      putBoolLn(a);
    } 
    """
    val expected = "2false"
    assert(checkCode(input,expected,523))
  }
  test("24. test do while 2") {
    val input = """
    boolean a;
    void main () {
      int b;
      b = 0;
      do
        do
          b = b + 1;
        while (b < 6);
        b = b - 2;
      while(a);
      putIntLn(b);
      putBoolLn(a);
    } 
    """
    val expected = "4false"
    assert(checkCode(input,expected,524))
  }
  test("25. test do while 3") {
    val input = """
    boolean a;
    void main () {
      int b;
      b = 0;
      do
        do
          b = b + 1;
          {
            int c;
            c = 6;
            b = b + c;
          }
        while (b < 6);
        b = b - 2;
      while(a);
      putIntLn(b);
      putBoolLn(a);
    } 
    """
    val expected = "5false"
    assert(checkCode(input,expected,525))
  }
  test("26. test do while 4") {
    val input = """
    boolean a;
    void main () {
      int b;
      b = 0;
      do
        do
          b = b + 1;
          {
            int b;
            b = 6;
            {}
            {}
          }
          {}
        while (b < 6);
        b = b - 2;
      while(a);
      putIntLn(b);
      putBoolLn(a);
    } 
    """
    val expected = "4false"
    assert(checkCode(input,expected,526))
  }
  test("27. test array") {
    val input = """
    void main () {
      putIntLn(toan()[4]);
    } 

    int[] toan(){
      return foo();
    }

    int[] foo(){
      int a[5];
      a[4] = 5;
      return a;
    }
    """
    val expected = "5"
    assert(checkCode(input,expected,527))
  }
  test("28. test array") {
    val input = """
    void main () {
      putIntLn(toan()[4]);
    } 

    int[] toan(){
      return foo();
    }

    int[] foo(){
      int a[5];
      a[4] = 5;
      return a;
    }
    """
    val expected = "5"
    assert(checkCode(input,expected,528))
  }
  test("29. test array 2 ") {
    val input = """
    void main () {
      putIntLn(toan()[4]);
    } 

    int[] toan(){
      return foo();
    }

    int[] foo(){
      int a[5];
      a[4] = 5;
      return a;
    }
    """
    val expected = "5"
    assert(checkCode(input,expected,529))
  }
  test("30. test array 3 ") {
    val input = """
    int x;
    void main () {
      int a[5],b[4];
      foo(2)[3+x] = a[b[2]] +3;
    } 
    int[] foo(int d){
      int a[5];
      a[d] = 5;
      return a;
    }
    """
    val expected = ""
    assert(checkCode(input,expected,530))
  }
  test("31. test array 4 ") {
    val input = """
    int x;
    void main () {
      int a[5],b[4];
      foo(2)[3+x] = a[b[2]] +3;
    } 
    int[] foo(int d){
      int a[5];
      a[d] = 5;
      return a;
    }
    """
    val expected = ""
    assert(checkCode(input,expected,531))
  }
  test("32. test array 5 ") {
    val input = """
    int x;
    void main () {
      
    } 
    int[] foo(int d){
      int a[5];
      a[d] = 5;
      return a;
    }
    """
    val expected = ""
    assert(checkCode(input,expected,532))
  }
  test("33. test break ") {
    val input = """
    int x;
    void main () {
      int i;
      for (i = 0; i < 5; i =  i + 1)
        do
          putInt(1);
          if (i == 1) break;
          i = i + 1;
        while (i < 3);
    }
    """
    val expected = "1111"
    assert(checkCode(input,expected,533))
  }
  test("34. test break ") {
    val input = """
    int x;
    void main () {
      int i;
      for (i = 0; i < 5; i =  i + 1)
        do
          putInt(1);
          if (i == 1) break;
          i = i + 1;
        while (i < 3);
    }
    """
    val expected = "1111"
    assert(checkCode(input,expected,534))
  }
  test("35. test break 2 ") {
    val input = """
    int x;
    void main () {
      int i;
      for (i = 0; i < 5; i =  i + 1)
        do
          putInt(1);
          i = i + 1;
        while (i < 3);
    }
    """
    val expected = "1111"
    assert(checkCode(input,expected,535))
  }

   test("36. test break 3 ") {
    val input = """
    int x;
    void main () {
      int i;
      for (i = 0; i < 5; i =  i + 1)
      {
        if (i >= 3 ) continue;
        do
          putInt(1);
          if (i == 1) break;
          i = i + 1;
        while (i < 3);
      }
        
    }
    """
    val expected = "111"
    assert(checkCode(input,expected,536))
  }
   test("37. test for co block ") {
    val input = """
    int x;
    void main () {
      int i;
      for (i = 0; i < 5; i =  i + 1)
      {
          if (i >= 3 ) continue;
          putInt(1);
          i = i + 1;
      }
        
    }
    """
    val expected = "11"
    assert(checkCode(input,expected,537))
  }
  test("38. test binaryop reop ") {
    val input = """
    
    void main () {
      boolean a;
      a = (2 > 4 + 3);
      putBool(a);
    }
    """
    val expected = "false"
    assert(checkCode(input,expected,538))
  }
  test("39. test binaryop phep gan trong reop ") {
    val input = """
    
    void main () {
      int a;
      int b;
      a = b = 2;
      putBool(a);
    }
    """
    val expected = "true"
    assert(checkCode(input,expected,539))
  }
  test("""40.test exprion long nhau """) {
    val input = 
      """ void main(){
        int a,b,c;
        boolean d;
        a = b = c = 5;
        d = ((a = 1) == 2) || ((b = 2)==2) || ((c = 3) == 4);
        putIntLn(a);
        putIntLn(b);
        putIntLn(c);
        putBoolLn(d);
        a;
        5;
        true;
        "10";
        a + 5;
        5 - 6;
        }
        float b;
         """
    val expected = """125true"""
    assert(checkCode(input,expected,540))
  }

  test("""41.test compare variable int and float""") {
    val input = 
      """ void main(){
        int a,b,c;
        boolean d;
        a = b = c = 5;
        d = ((a = 1) == 2) && ((b = 2)==2) && ((c = 3) == 4);
        putIntLn(a);
        putIntLn(b);
        putIntLn(c);
        putBoolLn(d);
        }
         """
    val expected = """155false"""
    assert(checkCode(input,expected,541))
  }

  test("""42.test put many variable""") {
    val input = 
      """ void main(){
        int a,b,c;
        boolean d;
        a = b = c = 5;
        d = ((a = 1) == 2) && ((b = 2)==2) && ((c = 3) == 4);
        putIntLn(a);
        putIntLn(b);
        putIntLn(c);
        putBoolLn(d);
        }
         """
    val expected = """155false"""
    assert(checkCode(input,expected,542))
  }
  test("""43.test return in function foo""") {
    val input = 
      """ void main(){
            putInt( foo(2,2) );
            putInt( foo(2, 1.5  ));
        }

        int foo(int a, float b){
            if (a > b) 
              return 1;
            else return 0;
        }
         """
        
    val expected = """01"""
    assert(checkCode(input,expected,543))
  }
  test("""44.write value interger""") {
    val input = 
      """ void main(){
            putInt( 214748364 );
        }

         """
        
    val expected = """214748364"""
    assert(checkCode(input,expected,544))
  }
  test("""45.write value float """) {
    val input = 
      """ void main(){
            putFloat(0.33E-3);
        }

        
         """
        
    val expected = """3.3E-4"""
    assert(checkCode(input,expected,545))
  }

  test("""46.write variable string""") {
    val input = 
      """ void main(){
            string str;
            str = "hcmut-1513539";
            putStringLn(str);
        }

        
         """
        
    val expected = """hcmut-1513539"""
    assert(checkCode(input,expected,546))
  }

  test("""47. test tren group : call exp""") {
    val input = """
      string foo(string str){
      return goo();
      }
      string goo(){
      return "abc";
      }
      void main () {
      putString(foo("bcd"));
      }
      """
        
    val expected = """abc"""
    assert(checkCode(input,expected,547))
  }
  test("""48. test tren group : putFloatLn""") {
    val input = """
      
      void main () {
      putFloatLn(2.6 - 3.0);
      }
      """
        
    val expected = """-0.4000001"""
    assert(checkCode(input,expected,548))
  }
  test("""49. test tren group : nguyen van thi""") {
    val input = """
      
      void main () {
      foo();
      }
      int foo(){
        int a;
        a=1;
        do{
        a=a+1;
        if(a>10) break;
        return 999;
        }
        while(a<100);
        }
      """
        
    val expected = """"""
    assert(checkCode(input,expected,549))
  }
  test("""50. test tren group : function not return ?""") {
    val input = """
      
      void main () {
      main1();
      }
      int main1(){
      int a;
      a = 100;
      do{
      if(a==0) return a;
      a = a-1;
      }while(a>0);
      }
      """
        
    val expected = """"""
    assert(checkCode(input,expected,550))
  }
  
  test("51. test array 5 ") {
    val input = """
    int x;
    void main () {
      
    } 
    int[] foo(int d){
      int a[5];
      a[d] = 5;
      return a;
    }
    """
    val expected = ""
    assert(checkCode(input,expected,551))
  }
  test("52. test break ") {
    val input = """
    int x;
    void main () {
      int i;
      for (i = 0; i < 5; i =  i + 1)
        do
          putInt(1);
          if (i == 1) break;
          i = i + 1;
        while (i < 3);
    }
    """
    val expected = "1111"
    assert(checkCode(input,expected,552))
  }
  test("53. test break ") {
    val input = """
    int x;
    void main () {
      int i;
      for (i = 0; i < 5; i =  i + 1)
        do
          putInt(1);
          if (i == 1) break;
          i = i + 1;
        while (i < 3);
    }
    """
    val expected = "1111"
    assert(checkCode(input,expected,553))
  }
  test("54. test break 2 ") {
    val input = """
    int x;
    void main () {
      int i;
      for (i = 0; i < 5; i =  i + 1)
        do
          putInt(1);
          i = i + 1;
        while (i < 3);
    }
    """
    val expected = "1111"
    assert(checkCode(input,expected,554))
  }

   test("55. test break 3 ") {
    val input = """
    int x;
    void main () {
      int i;
      for (i = 0; i < 5; i =  i + 1)
      {
        if (i >= 3 ) continue;
        do
          putInt(1);
          if (i == 1) break;
          i = i + 1;
        while (i < 3);
      }
        
    }
    """
    val expected = "111"
    assert(checkCode(input,expected,555))
  }
   test("56. test for co block ") {
    val input = """
    int x;
    void main () {
      int i;
      for (i = 0; i < 5; i =  i + 1)
      {
          if (i >= 3 ) continue;
          putInt(1);
          i = i + 1;
      }
        
    }
    """
    val expected = "11"
    assert(checkCode(input,expected,556))
  }
  test("57. test binaryop reop ") {
    val input = """
    
    void main () {
      boolean a;
      a = (2 > 4 + 3);
      putBool(a);
    }
    """
    val expected = "false"
    assert(checkCode(input,expected,557))
  }
  test("58. test binaryop phep gan trong reop ") {
    val input = """
    
    void main () {
      int a;
      int b;
      a = b = 2;
      putBool(a);
    }
    """
    val expected = "true"
    assert(checkCode(input,expected,558))
  }

  test("""59.short-circuit evaluation """) {
    val input = 
      """ void main(){
        int a,b,c;
        boolean d;
        a = b = c = 5;
        d = ((a = 1) == 2) || ((b = 2)==2) || ((c = 3) == 4);
        putIntLn(a);
        putIntLn(b);
        putIntLn(c);
        putBoolLn(d);
        }
        float b;
         """
    val expected = """125true"""
    assert(checkCode(input,expected,559))
  } 
  test("60. int function foo") {
    val input = """
    int a ;
    void main () {
      putInt(foo());
    } 
    int foo(){
      return 5;
      putInt(a);
    }
    """
    val expected = "5"
    assert(checkCode(input,expected,560))
  }
  test("61. if statement1") {
    val input = """
    int a ;
    void main () {
      a = 1;
      if (a >= 0)
        if (a > 5) putString("lon hon 5");
        else putString("be hon 5 lon hon -1");
      else 
        if (a < -5) putString("be hon -5");
        else putString("lon hon -5 be hon 0");  
    } 
    """
    val expected = "be hon 5 lon hon -1"
    assert(checkCode(input,expected,561))
  }
  test("62. if statement2") {
    val input = """
    int a ;
    void main () {
      a = --(-1 + 2 * -4 + 6);
      if (a >= 0)
        if (a > 5) putString("lon hon 5");
        else putString("be hon 5 lon hon -1");
      else 
        if (a < -5) putString("be hon -5");
        else putString("lon hon -5 be hon 0");  
    } 
    """
    val expected = "lon hon -5 be hon 0"
    assert(checkCode(input,expected,562))
  }
  test("63. for statement1") {
    val input = """
    int a ;
    void main () {
      a = --(-1 + 2 * -4 + 6);
      for (a = a; a > 10; a = a + 1)
        a = 0;
      putIntLn(a)  ;
    } 
    """
    val expected = "-3"
    assert(checkCode(input,expected,563))
  }
  test("64. for statement2") {
    val input = """
    int a ;
    void main () {
      int i;
      a = 1;
      for (i = 1; i < 10; i = i + 1)
        a = a * 2;
      putIntLn(a)  ;
    } 
    """
    val expected = "512"
    assert(checkCode(input,expected,564))
  }
  test("65. for statement2") {
    val input = """
    int a ;
    void main () {
      int i;
      a = 1;
      for (i = 1; i < 10; i = i + 1)
        a = a * 2;
      putIntLn(a)  ;
    } 
    """
    val expected = "512"
    assert(checkCode(input,expected,565))
  }
  test("66. do while statement 1") {
    val input = """
    int a ;
    void main () {
      int i;
      a = 1;
      for (i = 1; i < 10; i = i + 1)
        {
          a = a - 1;
          a = a + 1;
          a = --------a * 2;
        }
      putIntLn(a)  ;
    } 
    """
    val expected = "512"
    assert(checkCode(input,expected,566))
  }
  test("67. test tren lop") {
    val input = """
    int a ;
    void main () {
      putInt(foo(1,2));
    } 
    int foo(int a, int b){
      if (a == b)
        return 1;
      else return 0;
    }
    string[] foo2(int a, int b){
    }
    """
    val expected = "0"
    assert(checkCode(input,expected,567))
  }
  test("68. test break for") {
    val input = """
    int a ;
    void main () {
      for (a = 10; a > 0 ; a = a - 1)
      {
        if (a == 4) break;
      }
      putIntLn(a);

    } 
    
    """
    val expected = "4"
    assert(checkCode(input,expected,568))
  }
  test("69. test continue for") {
    val input = """
    void main () {
      int a ;
      int i;
      i = 1;
      for (a = 10; a > 0 ; a = a - 1)
      {
       if (a == 4) continue;
        i = i * 2;
      }
      putIntLn(i);

    } 
    
    """
    val expected = "512"
    assert(checkCode(input,expected,569))
  }
  test("70. test continue for") {
    val input = """
    void main () {
      putFloatLn(foo());
    } 
    float foo(){
      return 3;
    }
    
    """
    val expected = "3.0"
    assert(checkCode(input,expected,570))
  }
  test("71. test putFloat int value") {
    val input = """
    float a;
    void main () {
      float a; a =1;
      {
        putFloatLn(foo(2,a) );
      }
    } 
    int foo(float a, float b){
      {
        {
          {

          }
        }
      return -2;
      }
    }
    
    """
    val expected = "-2.0"
    assert(checkCode(input,expected,571))
  }
  test("72. test short-cicruit") {
    val input = """
    void main () {
      boolean a;
      a = (1 < 2) || (2 > 3) || (3 == 4) ;
      putBoolLn(a);
    } 
    """
    val expected = "true"
    assert(checkCode(input,expected,572))
  }

  test("73. test do while") {
    val input = """
    boolean a;
    void main () {
      int b;
      do
        b = 2;
      while(a);
      putIntLn(b);
      putBoolLn(a);
    } 
    """
    val expected = "2false"
    assert(checkCode(input,expected,573))
  }
  test("74. test do while 2") {
    val input = """
    boolean a;
    void main () {
      int b;
      b = 0;
      do
        do
          b = b + 1;
        while (b < 6);
        b = b - 2;
      while(a);
      putIntLn(b);
      putBoolLn(a);
    } 
    """
    val expected = "4false"
    assert(checkCode(input,expected,574))
  }
  test("75. test do while 3") {
    val input = """
    boolean a;
    void main () {
      int b;
      b = 0;
      do
        do
          b = b + 1;
          {
            int c;
            c = 6;
            b = b + c;
          }
        while (b < 6);
        b = b - 2;
      while(a);
      putIntLn(b);
      putBoolLn(a);
    } 
    """
    val expected = "5false"
    assert(checkCode(input,expected,575))
  }
  test("76. test do while 4") {
    val input = """
    boolean a;
    void main () {
      int b;
      b = 0;
      do
        do
          b = b + 1;
          {
            int b;
            b = 6;
            {}
            {}
          }
          {}
        while (b < 6);
        b = b - 2;
      while(a);
      putIntLn(b);
      putBoolLn(a);
    } 
    """
    val expected = "4false"
    assert(checkCode(input,expected,576))
  }
  test("77. test array") {
    val input = """
    void main () {
      putIntLn(toan()[4]);
    } 

    int[] toan(){
      return foo();
    }

    int[] foo(){
      int a[5];
      a[4] = 5;
      return a;
    }
    """
    val expected = "5"
    assert(checkCode(input,expected,577))
  }
  test("78. test array") {
    val input = """
    void main () {
      putIntLn(toan()[4]);
    } 

    int[] toan(){
      return foo();
    }

    int[] foo(){
      int a[5];
      a[4] = 5;
      return a;
    }
    """
    val expected = "5"
    assert(checkCode(input,expected,578))
  }
  test("79. test array 2 ") {
    val input = """
    void main () {
      putIntLn(toan()[4]);
    } 

    int[] toan(){
      return foo();
    }

    int[] foo(){
      int a[5];
      a[4] = 5;
      return a;
    }
    """
    val expected = "5"
    assert(checkCode(input,expected,579))
  }
  test("80. test array 3 ") {
    val input = """
    int x;
    void main () {
      int a[5],b[4];
      foo(2)[3+x] = a[b[2]] +3;
    } 
    int[] foo(int d){
      int a[5];
      a[d] = 5;
      return a;
    }
    """
    val expected = ""
    assert(checkCode(input,expected,580))
  }
  test("81. test array 4 ") {
    val input = """
    int x;
    void main () {
      int a[5],b[4];
      foo(2)[3+x] = a[b[2]] +3;
    } 
    int[] foo(int d){
      int a[5];
      a[d] = 5;
      return a;
    }
    """
    val expected = ""
    assert(checkCode(input,expected,581))
  }
  test("82. test array 5 ") {
    val input = """
    int x;
    void main () {
      
    } 
    int[] foo(int d){
      int a[5];
      a[d] = 5;
      return a;
    }
    """
    val expected = ""
    assert(checkCode(input,expected,582))
  }
  test("83. test break ") {
    val input = """
    int x;
    void main () {
      int i;
      for (i = 0; i < 5; i =  i + 1)
        do
          putInt(1);
          if (i == 1) break;
          i = i + 1;
        while (i < 3);
    }
    """
    val expected = "1111"
    assert(checkCode(input,expected,583))
  }
  test("84. test break ") {
    val input = """
    int x;
    void main () {
      int i;
      for (i = 0; i < 5; i =  i + 1)
        do
          putInt(1);
          if (i == 1) break;
          i = i + 1;
        while (i < 3);
    }
    """
    val expected = "1111"
    assert(checkCode(input,expected,584))
  }
  test("85. test break 2 ") {
    val input = """
    int x;
    void main () {
      int i;
      for (i = 0; i < 5; i =  i + 1)
        do
          putInt(1);
          i = i + 1;
        while (i < 3);
    }
    """
    val expected = "1111"
    assert(checkCode(input,expected,585))
  }

   test("86. test break 3 ") {
    val input = """
    int x;
    void main () {
      int i;
      for (i = 0; i < 5; i =  i + 1)
      {
        if (i >= 3 ) continue;
        do
          putInt(1);
          if (i == 1) break;
          i = i + 1;
        while (i < 3);
      }
        
    }
    """
    val expected = "111"
    assert(checkCode(input,expected,586))
  }
   test("87. test for co block ") {
    val input = """
    int x;
    void main () {
      int i;
      for (i = 0; i < 5; i =  i + 1)
      {
          if (i >= 3 ) continue;
          putInt(1);
          i = i + 1;
      }
        
    }
    """
    val expected = "11"
    assert(checkCode(input,expected,587))
  }
  test("88. test binaryop reop ") {
    val input = """
    
    void main () {
      boolean a;
      a = (2 > 4 + 3);
      putBool(a);
    }
    """
    val expected = "false"
    assert(checkCode(input,expected,588))
  }
  test("89. test binaryop phep gan trong reop ") {
    val input = """
    
    void main () {
      int a;
      int b;
      a = b = 2;
      putBool(a);
    }
    """
    val expected = "true"
    assert(checkCode(input,expected,589))
  }

  test("""90.test tren lop kho """) {
    val input = 
      """ void main(){
        int a,b,c;
        boolean d;
        a = b = c = 5;
        d = ((a = 1) == 2) || ((b = 2)==2) || ((c = 3) == 4);
        putIntLn(a);
        putIntLn(b);
        putIntLn(c);
        putBoolLn(d);
        }
        float b;
         """
    val expected = """125true"""
    assert(checkCode(input,expected,590))
  } 
  

  test("""91.short-circuit evaluation""") {
    val input = 
      """ void main(){
        int a,b,c;
        boolean d;
        a = b = c = 5;
        d = ((a = 1) == 2) && ((b = 2)==2) && ((c = 3) == 4);
        putIntLn(a);
        putIntLn(b);
        putIntLn(c);
        putBoolLn(d);
        }
         """
    val expected = """155false"""
    assert(checkCode(input,expected,591))
  }
  test("""92.test return in function foo""") {
    val input = 
      """ void main(){
            putInt( foo(2,2) );
            putInt( foo(2, 1  ));
        }

        int foo(int a, int b){
            if (a > b) 
              return 1;
            else return 0;
        }
         """
        
    val expected = """01"""
    assert(checkCode(input,expected,592))
  }
  test("""93.write value interger""") {
    val input = 
      """ void main(){
            putInt( 214748364 );
        }

         """
        
    val expected = """214748364"""
    assert(checkCode(input,expected,593))
  }
  test("""94.write value float """) {
    val input = 
      """ void main(){
            putFloat(0.33E-3);
        }

        
         """
        
    val expected = """3.3E-4"""
    assert(checkCode(input,expected,594))
  }

  test("""95.write variable string""") {
    val input = 
      """ void main(){
            string str;
            str = "hcmut-1513539";
            putStringLn(str);
        }

        
         """
        
    val expected = """hcmut-1513539"""
    assert(checkCode(input,expected,595))
  }

  test("96. solve quadratic equation") {
val input =
"""float EPSILON;
|float squareRoot(float x){
|return testSQ(x,1);
|}
|float testSQ(float a, float b){
|if (closeEnough(a/b,b)) return b; else return testSQ(a,betterGuess(a,b));
|}
|boolean closeEnough(float a, float b){
|if (a-b>=0) return a-b<EPSILON; else return b-a<EPSILON;
|}
|float betterGuess(float a, float b){
|return (b+a/b)/2;
|}
|void main(){ // solve equation: a*x^2 + b*x + c = 0
|float a,b,c,delta,x_1,x_2;
|EPSILON = 0.001;
|a = 2; // enter a = 2;
|b = 5; // enter b = 5;
|c = 2; // enter c = 2;
|delta = b*b - 4*a*c; // delta = 9;
|if (delta < 0) {
|putString("The equation has no solution");
|return;
|} else {
|x_1 = (-b - squareRoot(delta))/(2*a); // x_1 = -2.0
|x_2 = (-b + squareRoot(delta))/(2*a); // x_2 = -0.5
|putString("x_1 = ");
|putFloat(x_1);
|putString("; x_2 = ");
|putFloat(x_2);
|}
|1;
|return;
|}""".stripMargin
val expected = "x_1 = -2.000023; x_2 = -0.4999771"
assert(checkCode(input, expected, 596))
}
  test("""97. test tren group : putFloatLn""") {
    val input = """
      
      void main () {
      putFloatLn(2.6 - 3.0);
      }
      """
        
    val expected = """-0.4000001"""
    assert(checkCode(input,expected,597))
  }
  test("""98. test tren group : nguyen van thi""") {
    val input = """
      
      void main () {
      foo();
      }
      int foo(){
        int a;
        a=1;
        do{
        a=a+1;
        if(a>10) break;
        return 999;
        }
        while(a<100);
        }
      """
        
    val expected = """"""
    assert(checkCode(input,expected,598))
  }
    test("""99.short-circuit evaluation ((a = 1) == 2) || ((b = 2)==2) || ((c = 3) == 4)""") {
    val input = 
      """ void main(){
        int a,b,c;
        boolean d;
        a = b = c = 5;
        d = ((a = 1) == 2) || ((b = 2)==2) || ((c = 3) == 4);
        putIntLn(a);
        putIntLn(b);
        putIntLn(c);
        putBoolLn(d);
        }
        float b;
         """
    val expected = """125true"""
    assert(checkCode(input,expected,599))
  }

  test("""FUNCTION CALL: test return ARRAYPOINTER type""") {
    val input = 
      """ void main(){
          int b[6];
          
          putIntLn(foo(b)[foo(b)[3]]);
          }
        int[] foo(int a[]){
          a[3] = 3;
          return a;
        }
         """
    val expected = """3"""
    assert(checkCode(input,expected,600))
  } 
  test("""BLOCK SCOPE: """) {
    val input = 
      """ void main(){
          int a,b[10];
          {
            int c[3],d;
            c[1] = 1;
            putIntLn(c[1]);
            2<= 4;
          }
          1;  
        }
         """
    val expected = """1"""
    assert(checkCode(input,expected,601))
  }
  test("""FUNCTION CALL: test DANGENOUS POINTER""") {
    val input = 
      """ void main(){
          int b[6];
          b = foo();
          putIntLn(b[3]);
        }
        int[] foo(){
          int a[5];
          a[3] = 3;
          return a;
        }
         """
    val expected = """3"""
    assert(checkCode(input,expected,602))
  }
  test("""ANDOP: short-circuit evaluation ((a = 1) == 2) && ((b = 2)==2) && ((c = 3) == 4)""") {
    val input = 
      """ void main(){
        int a,b,c;
        boolean d;
        a = b = c = 5;
        d = ((a = 1) == 2) && ((b = 2)==2) && ((c = 3) == 4);
        putIntLn(a);
        putIntLn(b);
        putIntLn(c);
        putBoolLn(d);
        }
         """
    val expected = """155false"""
    assert(checkCode(input,expected,603))
  }
}

