# An interpreter of the micro C

A interpreter for a programing language called Micro C, similar to C but simpler. The description of the language is :  https://github.com/naot97/implement_mini_C/blob/master/specific_MC.pdf

## Getting Started
### Installing
* Install Java JDK (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html). Make sure can run javac- in cmd or terminal.
* Install sbt 1.0.0 (http://www.scala-sbt.org)
### Running
Type ```sbt ```
 in cmd or terminal
### Compiling
Type ```compile``` in cmd or terminal
### Testing
Type ```test``` in cmd or terminal

### Steps of building the interpreter:

#### Step 1: Generating the parser tree.

- The project uses the ANother Tool for Language Recognition(ANTLR) to generate the parser tree from a regular expression language called CFG. ANTLR is a powerful parser generator that you can use to read, process, execute, or translate structured text or binary files. It’s widely used in academia and industry to build all sorts of languages, tools, and frameworks. Twitter search uses ANTLR for query parsing, with over 2 billion queries a day. The languages for Hive and Pig, the data warehouse and analysis systems for Hadoop, both use ANTLR. You can read more about ANTLR in !(https://www.antlr.org)[here].

- This step can help to check the syntax of the language. When we create CFG regular expressions, we define the syntax of the elements of the language, like: statements(if, while, for, break, return, continue,  expression, block), declarations(variable and function declarations), Datatypes and Values(interger, float, string, boolean), Token Set(Identifiers, Keywords, Operators) and Literals. This work can be found in !https://github.com/naot97/implement_mini_C/blob/master/ppl/src/main/mc/parser/MC.g4.

#### Step 2: Building AST tree

- Because the parser tree is too difficult to process and check the meaning (about Type Coercions in expression, check Precedence and Associativity, Index Expression, Index Expression), so we convert the structure of program from the parser tree to the AST tree. In here we build the AST tree from paser tree by packages in https://github.com/naot97/implement_mini_C/tree/master/ppl/bin/mc/parser. AST tree is represented by classes of Scala language. The code define these classes can be found in 
https://github.com/naot97/implement_mini_C/blob/master/ppl/src/main/mc/astgen/ASTGeneration.scala

#### Step 3: Checking meaning

- We check the Type Coercions in expression, check Precedence and Associativity, Index Expression, Index Expression. To understand more these problem, you can read the sections 6,7 in https://github.com/naot97/implement_mini_C/blob/master/specific_MC.pdf. The code can be found in https://github.com/naot97/implement_mini_C/tree/master/ppl/src/main/mc/checker

#### Step 4: Gen Assembly code

- From the Scala classes, we gen to the syntax of a assembly language called "Java byte code". And then the JVM can interprete the assembly code and run the program. he code can be found in https://github.com/naot97/implement_mini_C/tree/master/ppl/src/main/mc/codegen



 
  
