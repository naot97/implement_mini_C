/**
*	@author Dr.Nguyen Hua Phung
*	@version 1.0
*	28/6/2006
*	This class is used to generate code at a intermediate level
*
*/
package mc.codegen

import java.io.BufferedWriter
import java.io.FileWriter
import java.text.DecimalFormat
import java.util.Iterator

import mc.utils._


class Emitter(filename:String) {
  val buff = new StringBuffer()	
  val jvm = new JasminCode()

  def getJVMType(inType:Type):String = inType match {
    case IntType => "I"
    case StringType => "Ljava/lang/String;"
    case FloatType => "F"
    case VoidType => "V"
    case BoolType => "Z"
    case ArrayType(i,t) => "["+getJVMType(t)
    case ArrayPointerType(t) => "["+getJVMType(t)
    case FunctionType(param,re) => 
    	"("+param.foldLeft("")((a,b) => a+getJVMType(b))+")"+getJVMType(re)
    //case MType(il,o) => "("+il.foldLeft("")(_+getJVMType(_))+")"+getJVMType(o)
    case ClassType(t) => "L"+t+";"
  }
  def getFullType(inType:Type):String = inType match {
    case IntType => "int"
    case FloatType => "float"
    case BoolType => "boolean"
    case StringType => "java/lang/String"
    case VoidType => "void"
  }

	def emitPUSHICONST(i:Int,frame:Frame):String  =  
	 {
		frame.push();
		if (i >= -1 && i <= 5) jvm.emitICONST(i)
		else if (i >= -128 && i <= 127) jvm.emitBIPUSH(i)
		else if (i >= -32768 && i <= 32767) jvm.emitSIPUSH(i)
		else jvm.emitLDC("" + i) 	
	}



	def emitPUSHICONST(in:String,frame:Frame):String = 
    in match {
      case "true" => emitPUSHICONST(1,frame)
      case "false" => emitPUSHICONST(0,frame)
      case _ => emitPUSHICONST(in.toInt,frame)
    }


	def emitPUSHFCONST(in:String,frame:Frame):String = 
	 {
		val f = in.toFloat;	
		frame.push();
	    val myFormatter = new DecimalFormat("###0.0###");
		val rst = myFormatter.format(f);
		if (rst.equals("0.0") || rst.equals("1.0") ||rst.equals("2.0")) 
			jvm.emitFCONST(rst)
	   else
			jvm.emitLDC(in);
	}
	/**
	*	generate code to push a constant onto the operand stack.
	*	@param in the lexeme of the constant
	*	@param typ the type of the constant
	*/
	def emitPUSHCONST(in:String, typ:Type, frame:Frame) = 
		typ match {
	 case  (IntType|BoolType) => emitPUSHICONST(in,frame)
      case FloatType => emitPUSHFCONST(in,frame)
      case StringType => {
        frame.push();
        jvm.emitLDC( in);
      }
      case _ => throw IllegalOperandException(in)
    }

        ////////////////////////////////////////////////////////////////

        
    def emitALOAD(in:Type,frame:Frame) =  
	  {
    	//..., arrayref, index -> ..., value
    	frame.pop();
      in match {
        case IntType => jvm.emitIALOAD()
        case FloatType => jvm.emitFALOAD()
        case BoolType => jvm.emitBALOAD()
        case (ArrayType(_,_)|ArrayPointerType(_)|StringType) => jvm.emitAALOAD()
        case _ => throw IllegalOperandException(in.toString);
      }
		
	}
    
    def emitSTORE(sym : Symbol, frame : Frame) = {
    	//.. value ->..
    	val value = sym.value
    	val typ = sym.typ
    	val name = sym.name
    	val isLocal = value.isInstanceOf[Index]
    	if (isLocal) emitWRITEVAR(typ,value.asInstanceOf[Index].value, frame)
    	else	emitPUTSTATIC(value.asInstanceOf[CName].value+"."+name,typ, frame)
    }    

    def emitASTORE(in:Type,frame:Frame) = 
	{
    	//..., arrayref, index, value -> ...
    	frame.pop();
    	frame.pop();
    	frame.pop();
      in match {
        case IntType => jvm.emitIASTORE()
        case FloatType => jvm.emitFASTORE()
        case BoolType => jvm.emitBASTORE()
        case (ArrayType(_,_)|ArrayPointerType(_)|ClassType(_)|StringType) => jvm.emitAASTORE()
        case _ => throw  IllegalOperandException(in.toString)
      }
		
	}
   
	def emitVAR(in:Int,varName:String, inType:Type, fromLabel: Int, toLabel: Int,frame:Frame) = 
		jvm.emitVAR(in, varName, getJVMType(inType), fromLabel, toLabel);
	
	def emitREADVAR(name:String,inType:Type,index:Int,frame:Frame) = 
	{
		//... -> ..., value
		
		frame.push();
      	inType match {
        	case (IntType) => jvm.emitILOAD(index)
        	case FloatType => jvm.emitFLOAD(index)
       		case BoolType => jvm.emitILOAD(index)
        	case (ArrayType(_,_)|ArrayPointerType(_)|ClassType(_)|StringType) => jvm.emitALOAD(index)
        	case _ => throw IllegalOperandException(name)
      }
			

	}
  /* generate the second instruction for array cell access
   * 
   */


	/**
	*	generate code to pop a value on top of the operand stack and store it to a block-scoped variable.
	*	@param name the symbol entry of the variable.
	*/
	def emitWRITEVAR(inType:Type,index:Int,frame:Frame) = 
	{
		//..., value -> ...
		frame.pop();    
    
    inType match {
      case (IntType | BoolType) => jvm.emitISTORE(index)

      case (FloatType) => jvm.emitFSTORE(index)

      case (ArrayType(_,_) | ArrayPointerType(_)|StringType) => jvm.emitASTORE(index)
      
      case _ => throw IllegalOperandException("emitWRITEVAR")
    }
         
	}	
    /* generate the second instruction for array cell access
   * 
   */
	/** 	generate the field (static) directive for a class mutable or immutable attribute.
	*	@param lexeme the name of the attribute.
	*	@param in the type of the attribute.
	*	@param isFinal true in case of constant; false otherwise
	*/
	def emitSTATIC(lexeme:String,  in:Type) = 
      jvm.emitSTATICFIELD(lexeme,getJVMType(in),false)
		
  
    def emitGETSTATIC( lexeme:String,  in:Type,frame:Frame) = {
      frame.push()
      jvm.emitGETSTATIC(lexeme, getJVMType(in))
    }
        
    def emitPUTSTATIC( lexeme:String, in: Type,frame:Frame) = {
      frame.pop()
      jvm.emitPUTSTATIC(lexeme, getJVMType(in))
    }

	def emitINVOKESTATIC(lexeme:String,in:Type ,frame:Frame) =
	{	
    val typ = in.asInstanceOf[FunctionType]
    typ.input.map(x => frame.pop)
		if (typ.output != VoidType)
			frame.push();		
		jvm.emitINVOKESTATIC(lexeme,getJVMType(in));
	}
	/**  generate code to invoke a special method
  * @param lexeme the qualified name of the method(i.e., class-name/method-name)
  * @param in the type descriptor of the method.
  */
  def emitINVOKESPECIAL(lexeme:String,in:Type ,frame:Frame) =
  { 
    val typ = in.asInstanceOf[FunctionType]
    typ.input.map(x=>frame.pop)
    frame.pop
    if (typ.output != VoidType)
      frame.push();   
    jvm.emitINVOKESPECIAL(lexeme,getJVMType(in));
  } 
  
  /**  generate code to invoke a default special method
  * 
  */
  def emitINVOKESPECIAL(frame:Frame) = {
    frame.pop
    jvm.emitINVOKESPECIAL()
  }
 
  	def emitNEGOP( in:Type,frame:Frame) = 
	{
  		//..., value -> ..., result
       	if (in == IntType)
          	 jvm.emitINEG()
      	else
           	 jvm.emitFNEG()
  	}
        
  	def emitNOT(in:Type,frame:Frame ) =
	{
  		val label1 = frame.getNewLabel();
    	val label2 = frame.getNewLabel();
      val result = new StringBuffer();
      result.append(emitIFTRUE(label1,frame));
      result.append(emitPUSHCONST("true", in,frame));
      result.append(emitGOTO(label2,frame));
      result.append(emitLABEL(label1,frame));
      result.append(emitPUSHCONST("false", in,frame));
      result.append(emitLABEL(label2,frame));
      result.toString();
  	}
        
	/**
	*	generate iadd, isub, fadd or fsub.
	*	@param lexeme the lexeme of the operator.
	*	@param in the type of the operands.
	*/	
	  def emitADDOP(lexeme:String, in:Type,frame:Frame) = 
	{
		//..., value1, value2 -> ..., result
		frame.pop();
		if (lexeme.equals("+")) {
			if (in == IntType)
				 jvm.emitIADD();
			else 
				 jvm.emitFADD()
		} else 
			if (in == IntType)
				 jvm.emitISUB();
			else 
				 jvm.emitFSUB();
	}
	/**
	*	generate imul, idiv, fmul or fdiv.
	*	@param lexeme the lexeme of the operator.
	*	@param in the type of the operands.
	*/	
	
	def emitMULOP(lexeme:String, in:Type,frame:Frame) =
	{
		//TODO \:integer division; %:integer remainder
		//..., value1, value2 -> ..., result
		frame.pop();
		if (lexeme.equals("*")) {
			if (in == IntType)
				 jvm.emitIMUL();
			else 
				 jvm.emitFMUL();
		}
		else if (in == IntType)
          jvm.emitIDIV();
    else
			 jvm.emitFDIV();
	}

	
	def emitMOD(frame:Frame) =
	{
		frame.pop();
		jvm.emitIREM();
	}
	/**
	*	generate iand.
	*/	

	def emitANDOP(frame:Frame) =
	{
		frame.pop();
		jvm.emitIAND();
	}	
	/**
	*	generate ior.
	*/	
	def emitOROP(frame:Frame) = 
	{
		frame.pop();
		jvm.emitIOR();
	}
    
    def emitREOP( op:String,  in:Type,frame:Frame) =
	  {
  		//..., value1, value2 -> ..., result
		    val result = new StringBuffer();
       	val labelF = frame.getNewLabel();
       	val labelO = frame.getNewLabel();
       	frame.pop();
  		  frame.pop();
       	op match {
          case ">" => if (in == IntType)
                        result.append(jvm.emitIFICMPLE(labelF));
                      else {
                        result.append(jvm.emitFCMPL());
                        result.append(jvm.emitIFLE(labelF));
                      }
          case ">=" => if (in == IntType)
                          result.append(jvm.emitIFICMPLT(labelF));
                        else {
                          result.append(jvm.emitFCMPL());
                          result.append(jvm.emitIFLT(labelF));
                      }
          case "<" => if (in == IntType)
                        result.append(jvm.emitIFICMPGE(labelF));
                      else {
                          result.append(jvm.emitFCMPL());
                          result.append(jvm.emitIFGE(labelF));
                      }
          case "<=" => if (in == IntType)
                          result.append(jvm.emitIFICMPGT(labelF));
                      else {
                          result.append(jvm.emitFCMPL());
                          result.append(jvm.emitIFGT(labelF));
                    }
          case "!=" => if (in == IntType || in == BoolType) result.append(jvm.emitIFICMPEQ(labelF))
                       else if (in == FloatType) {
                          result.append(jvm.emitFCMPL());
                          result.append(jvm.emitIFEQ(labelF));
                         }
                       else result.append(jvm.INDENT + "if_acmpeq Label" + labelF + jvm.END)
          case "==" => if (in == IntType || in == BoolType) result.append(jvm.emitIFICMPNE(labelF))
                       else if (in == FloatType) {
                          result.append(jvm.emitFCMPL());
                          result.append(jvm.emitIFNE(labelF));
                         }
                       else result.append(jvm.INDENT + "if_acmpne Label" + labelF + jvm.END)
        }
       	result.append(emitPUSHCONST("true", BoolType,frame));
       	result.append(emitGOTO(labelO,frame));
      	result.append(emitLABEL(labelF,frame));
       	result.append(emitPUSHCONST("false", BoolType,frame));
       	result.append(emitLABEL(labelO,frame));
       	result.toString();
	}

  	/*def emitREOP( op:String,  in:Type,frame:Frame) =
	  {
  		//..., value1, value2 -> ..., result
		    val result = new StringBuffer();
       	val labelF = frame.getNewLabel();
       	val labelO = frame.getNewLabel();
        //println(in)
       	frame.pop();
  		frame.pop();
  		if (in == IntType){
       	op match {
          case ">" =>   result.append(jvm.emitIFICMPLE(labelF));

          case ">=" =>  result.append(jvm.emitIFICMPLT(labelF));

          case "<" =>  result.append(jvm.emitIFICMPGE(labelF));

          case "<=" =>  result.append(jvm.emitIFICMPGT(labelF));

          case "!=" => result.append(jvm.emitIFICMPEQ(labelF))

          case "==" =>  result.append(jvm.emitIFICMPNE(labelF))

        }}
        else {

        	val INDENT = "\t"
        	val END = "\n"
        	op match {
          case ">" =>   result.append(INDENT + "if_fcmple Label" + labelF + END);

          case ">=" =>  result.append(INDENT + "if_fcmplt Label" + labelF + END);

          case "<" =>  result.append(INDENT + "if_fcmpge Label" + labelF + END);

          case "<=" =>  result.append(INDENT + "if_fcmpgt Label" + labelF + END);

          case "!=" => result.append(INDENT + "if_fcmpeq Label" + labelF + END)

          case "==" =>  result.append(INDENT + "if_fcmpne Label" + labelF + END)

        }
    	}
       result.append(emitPUSHCONST("1", IntType,frame));
       	result.append(emitGOTO(labelO,frame));
      	result.append(emitLABEL(labelF,frame));
       	result.append(emitPUSHCONST("0", IntType,frame));
       	result.append(emitLABEL(labelO,frame));
       	result.toString();
	}*/

	/** 	generate the method directive for a function.
	*	@param lexeme the qualified name of the method(i.e., class-name/method-name).
	*	@param in the type descriptor of the method.
	*	@param isStatic <code>true</code> if the method is static; <code>false</code> otherwise.
	*/
	def emitMETHOD( lexeme:String, in: Type, isStatic:Boolean,frame:Frame) =  jvm.emitMETHOD(lexeme,getJVMType(in),isStatic)
	/** 	generate the end directive for a function.
	*/
	def emitENDMETHOD(frame:Frame)  = {
		var buffer = new StringBuffer();
		buffer.append(jvm.emitLIMITSTACK(frame.getMaxOpStackSize()));
		buffer.append(jvm.emitLIMITLOCAL(frame.getMaxIndex()));
		buffer.append(jvm.emitENDMETHOD());
		buffer.toString();
	}


  def getConst(ast:Literal)= ast match {
    case IntLiteral(i) => (i.toString,IntType)

  
  }


	
	def emitIFTRUE(label:Int,frame:Frame)  = 
	{
		frame.pop();
		jvm.emitIFGT(label);
	}
	/**
	*	generate code to jump to label if the value on top of operand stack is false.<p>
	*	ifle label
	*	@param label the label where the execution continues if the value on top of stack is false.
	*/
	def emitIFFALSE(label:Int,frame:Frame) = 
	{
		frame.pop();
		jvm.emitIFLE(label);
	}
        
  	def emitIFICMPGT(label:Int,frame:Frame) = 
	{
		frame.pop();
		jvm.emitIFICMPGT(label);
	}
        

  	def emitIFICMPLT(label:Int,frame:Frame) = 
	{
		frame.pop();
		jvm.emitIFICMPLT(label);
	}
        
	/** 	generate code to duplicate the value on the top of the operand stack.<p>
	*	Stack:<p>
	*	Before: ...,value1<p>
	*	After:  ...,value1,value1<p>
	*/
	def emitDUP(frame:Frame) =
	{
		frame.push();
		jvm.emitDUP();
	}
	/**	generate code to pop the value on the top of the operand stack.
	*/
	def emitPOP(frame:Frame) = 
	{
		frame.pop();
		jvm.emitPOP();
	}
	/** 	generate code to exchange an integer on top of stack to a floating-point number.
	*/
	def emitI2F(frame:Frame) = {
		jvm.emitI2F()
	}

  
	/**	generate code to return.
	*	<ul>
	*	<li>ireturn if the type is IntegerType or BooleanType
	*	<li>freturn if the type is RealType
	*	<li>return if the type is null
	*	</ul>
	*	@param in the type of the returned expression.
	*/

	def emitRETURN(in:Type,frame:Frame) = 
	{
		in match {
      		case VoidType => jvm.emitRETURN()
      		case (IntType) => {frame.pop();jvm.emitIRETURN()}
      		case (BoolType) => {frame.pop();jvm.emitIRETURN()}
      		case (FloatType) => {frame.pop();jvm.emitFRETURN()}
      		case (_) => {frame.pop();jvm.emitARETURN()}
    	}
	}
	/** generate code that represents a label	
	 *	@param label the label
	 *	@return code Label<label>:
	 */
	def emitLABEL(label:Int,frame:Frame) = jvm.emitLABEL(label)
  
	
	def emitGOTO(label:Int,frame:Frame) =  jvm.emitGOTO(label)

	def emitPROLOG(name:String,parent:String) = {
		val result = new StringBuffer();
		result.append(jvm.emitSOURCE(name + ".java"));
		result.append(jvm.emitCLASS("public " + name));
		result.append(jvm.emitSUPER(if (parent == "") "java/lang/Object" else parent));
		result.toString();
	}
  
  def emitLIMITSTACK(num:Int) = jvm.emitLIMITSTACK(num)
  
  def emitLIMITLOCAL(num:Int) = jvm.emitLIMITLOCAL(num)
  
  def emitEPILOG() = {
    val file = new FileWriter(filename)
    file.write(buff.toString())
    file.close()
  }
	/** print out the code to screen
	*	@param in the code to be printed out
	*/
	def printout(in:String) = {
		//println(in)
		buff.append(in);
	}
// def emitPUTSTATIC( lexeme:String, in: Type,frame:Frame)
//def emitWRITEVAR(inType:Type,index:Int,frame:Frame)
  def emitPUSHNULL(frame : Frame) = {
  	frame.push()
  	jvm.emitPUSHNULL()
  }	 
  def emitINITARRAY( sym : Symbol, in : ArrayType,frame : Frame) = {
  		frame.push()

  		val isLocal = sym.value.isInstanceOf[Index]
  		var result = emitPUSHICONST(in.dimen.value,frame) + {if (in.eleType != StringType)
  			jvm.emitNEWARRAY(getFullType (in.eleType)) else jvm.emitANEWARRAY(getFullType (in.eleType))}
  		if (isLocal) 
  			result + emitWRITEVAR(in,sym.value.asInstanceOf[Index].value,frame)
  		else  
  			result + emitPUTSTATIC(sym.value.asInstanceOf[CName].value+"."+sym.name,in,frame)  
  		//println(result)
  		//result
  }
        
 	def clearBuff() = buff.setLength(0);
  
  
}
		
