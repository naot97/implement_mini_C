/**
*	@author Dr.Nguyen Hua Phung
*	@version 1.0
*	28/6/2006
* 	This class provides facilities for method generation
*
*/
package mc.codegen



abstract class MachineCode {
  def emitPUSHNULL():String
  def emitICONST(i:Int):String
  def emitBIPUSH(i:Int):String
  def emitSIPUSH(i:Int):String
  def emitLDC(in:String):String
  def emitFCONST(i:String):String
  def emitILOAD(in:Int):String
  def emitFLOAD(in:Int):String
  def emitISTORE(in:Int):String
  def emitFSTORE(in:Int):String
  def emitALOAD(in:Int):String
  def emitASTORE(in:Int):String
  def emitIASTORE():String
  def emitFASTORE():String
  def emitBASTORE():String
  def emitAASTORE():String
  def emitIALOAD():String
  def emitFALOAD():String
  def emitBALOAD():String
  def emitAALOAD():String
  def emitGETSTATIC(lexeme:String, typ:String) :String
  def emitPUTSTATIC(lexeme:String, typ:String):String
  def emitGETFIELD(lexeme:String, typ:String) :String
  def emitPUTFIELD(lexeme:String, typ:String):String
  def emitIADD():String
  def emitFADD():String
  def emitISUB():String
  def emitFSUB():String
  def emitIMUL():String
  def emitFMUL() :String
  def emitIDIV():String  
  def emitFDIV() :String
  def emitIAND() :String
  def emitIOR() :String
  def emitIREM():String
  def emitIFACMPEQ(label:Int):String
  def emitIFACMPNE(label:Int):String
  def emitIFICMPEQ(label:Int):String
  def emitIFICMPNE(label:Int):String
  def emitIFICMPLT(label:Int):String
  def emitIFICMPLE(label:Int):String
  def emitIFICMPGT(label:Int):String
  def emitIFICMPGE(label:Int):String
  def emitIFEQ(label:Int):String
  def emitIFNE(label:Int):String
  def emitIFLT(label:Int):String
  def emitIFLE(label:Int) :String 
  def emitIFGT(label:Int) :String 
  def emitIFGE(label:Int)  :String
  def emitLABEL(label:Int):String
  def emitGOTO(label:Int) :String
  def emitINEG():String
  def emitFNEG():String
  def emitDUP() :String
  def emitDUPX2() :String
  def emitPOP() :String
  def emitI2F() :String
  def emitNEW(lexeme:String) :String
  def emitNEWARRAY(lexeme:String) :String
  def emitANEWARRAY(lexeme:String) :String
  def emitMULTIANEWARRAY ( typ:String,  dimensions:Int) :String
  def emitINVOKESTATIC(lexeme:String, typ:String):String
  def emitINVOKESPECIAL() :String
  def emitINVOKEVIRTUAL(lexeme:String, typ:String):String
  def emitINVOKESPECIAL(lexeme:String, typ:String):String
  def emitI()  :String
  def emitF():String 
  def emit() :String 
  def emitLIMITSTACK(in:Int) :String
  def emitFCMPL() :String
  def emitLIMITLOCAL(in:Int):String 
  def emitVAR(in:Int,varName:String,inType:String,fromLabel:Int,toLabel:Int) :String
  def emitMETHOD(lexeme:String,typ:String,isStatic:Boolean) :String
  def emitENDMETHOD() :String
  def emitSOURCE(lexeme:String):String 
  def emitCLASS(lexeme:String):String 
  def emitSUPER(lexeme:String):String 
  def emitSTATICFIELD(lexeme:String,typ:String,isFinal:Boolean) :String
  def emitINSTANCEFIELD(lexeme:String, typ:String):String
  def emitRETURN():String
  def emitIRETURN():String
  def emitFRETURN():String
  def emitARETURN():String
}

class JasminCode extends MachineCode {
	
	val END = "\n"
	val INDENT = "\t"
  
	override def emitPUSHNULL() = INDENT + "aconst_null" + END
  
	override def emitICONST(i:Int) =  	
		if (i == -1) 
			INDENT + "iconst_m1" + END			
		 else if (i >= 0 && i <= 5)
			INDENT + "iconst_" + i + END			
		 else 
		throw  IllegalOperandException(i.toString)
	
	
	override def emitBIPUSH(i:Int) =  
		if ((i >= -128 && i < -1) || (i >5 && i <= 127))
			INDENT + "bipush " + i + END
		else
			throw IllegalOperandException(i.toString)
	
	
	override def emitSIPUSH(i:Int) =  
		if ((i >= -32768 && i < -128) || (i > 127 && i <= 32767))
			INDENT + "sipush " + i + END
		else
			throw IllegalOperandException(i.toString)
	
	
	override def emitLDC(in:String) =  INDENT + "ldc " + in + END
	
	
	override def emitFCONST(i:String)  =  
		if (i.equals("0.0")) 
			INDENT + "fconst_0" + END
		 else if (i.equals("1.0")) 
			INDENT + "fconst_1" + END
		 else if (i.equals("2.0")) 
			INDENT + "fconst_2" + END
		 else
			throw IllegalOperandException(i)
	
	
	override def emitILOAD(in:Int) =  
		if (in >= 0 && in <= 3)
			INDENT + "iload_"+ in + END
		else
			 INDENT + "iload "+ in + END
		
	
	override def emitFLOAD(in:Int)  =  
		if (in >= 0 && in <= 3)
			INDENT + "fload_" + in + END
		else
			INDENT + "fload " + in + END
		
	
	override def emitISTORE(in:Int) = 
		if (in >= 0 && in <= 3)
			INDENT + "istore_" + in + END
		else
			INDENT + "istore " + in + END
	
	
	override def emitFSTORE(in:Int) =  
		if (in >= 0 && in <= 3)
			INDENT + "fstore_" + in + END
		else
			INDENT + "fstore " + in + END
	

	override def emitALOAD(in:Int) = 
		if (in >= 0 && in <= 3)
			INDENT + "aload_" + in + END
		else
			INDENT + "aload " + in + END
		

	override def emitASTORE(in:Int) = 
		if (in >= 0 && in <= 3)
			INDENT + "astore_" + in + END
		else
			INDENT + "astore " + in + END
		

	override def emitIASTORE() =  INDENT + "iastore" + END
	
	override def emitFASTORE() =INDENT + "fastore" + END
	
	override def emitBASTORE() = INDENT + "bastore" + END
	
	override def emitAASTORE() = INDENT + "aastore" + END
	
	override def emitIALOAD() =INDENT + "iaload" + END
	
	override def emitFALOAD() = INDENT + "faload" + END
	
	override def emitBALOAD() = INDENT + "baload" + END	
  
	override def emitAALOAD() =INDENT + "aaload" + END
  
	override def emitGETSTATIC(lexeme:String, typ:String) = INDENT + "getstatic " + lexeme + " " + typ + END
	
	override def emitPUTSTATIC(lexeme:String, typ:String) = INDENT + "putstatic " + lexeme + " " + typ + END
  
  override def emitGETFIELD(lexeme:String, typ:String) = INDENT + "getfield " + lexeme + " " + typ + END
  
  override def emitPUTFIELD(lexeme:String, typ:String) = INDENT + "putfield " + lexeme + " " + typ + END
	
	override def emitIADD()  = INDENT + "iadd" + END
  
	override def emitFADD() = INDENT + "fadd" + END
	
  override def emitISUB() = INDENT + "isub" + END
	
	override def emitFSUB() =  INDENT + "fsub" + END
	
	override def emitIMUL() =  INDENT + "imul" + END
	
	override def emitFMUL() =  INDENT + "fmul" + END
	
	override def emitIDIV()  = INDENT + "idiv" + END
	
	override def emitFDIV() = INDENT + "fdiv" + END
	
	override def emitIAND() =  INDENT + "iand" + END
	
	override def emitIOR() = INDENT + "ior" + END
	
	override def emitIREM() = INDENT + "irem" + END
  
  override def emitIFACMPEQ(label:Int) = INDENT + "if_acmpeq Label" + label + END
  
  override def emitIFACMPNE(label:Int)  = INDENT + "if_acmpne Label" + label + END
	
	override def emitIFICMPEQ(label:Int) = INDENT + "if_icmpeq Label" + label + END
	
	override def emitIFICMPNE(label:Int)  = INDENT + "if_icmpne Label" + label + END
	
	
	override def emitIFICMPLT(label:Int)  = INDENT + "if_icmplt Label" + label + END
	
	
	override def emitIFICMPLE(label:Int)  = INDENT + "if_icmple Label" + label + END
	
	
	override def emitIFICMPGT(label:Int)  = INDENT + "if_icmpgt Label" + label + END
	
	
	override def emitIFICMPGE(label:Int)  =  INDENT + "if_icmpge Label" + label + END
	
	
	override def emitIFEQ(label:Int)  = INDENT + "ifeq Label" + label + END
	
	
	override def emitIFNE(label:Int)  = INDENT + "ifne Label" + label + END
	
	
	override def emitIFLT(label:Int)  = INDENT + "iflt Label" + label + END
	
	
	override def emitIFLE(label:Int)  = INDENT + "ifle Label" + label + END
	
	
	override def emitIFGT(label:Int)  = INDENT + "ifgt Label" + label + END
	
	
	override def emitIFGE(label:Int)  = INDENT + "ifge Label" + label + END
	
	
	override def emitLABEL(label:Int) = "Label" + label + ":" + END
	
	
	override def emitGOTO(label:Int)  = INDENT + "goto Label" + label + END
	
	
	override def emitINEG()= INDENT + "ineg" + END
	
	
	override def emitFNEG()= INDENT + "fneg" + END
	
	
	override def emitDUP() = INDENT + "dup" + END
	

	override def emitDUPX2() = INDENT + "dup_x2" + END
	
	
	override def emitPOP() = INDENT + "pop" + END
	
	
	override def emitI2F() = INDENT + "i2f" + END
	
  override def emitNEW(lexeme:String) = INDENT + "new " + lexeme + END
	
	override def emitNEWARRAY(lexeme:String) = INDENT + "newarray " + lexeme + END
	
  override def emitANEWARRAY(lexeme:String) = INDENT + "anewarray " + lexeme + END
	
	override def emitMULTIANEWARRAY ( typ:String,  dimensions:Int) = INDENT + "multianewarray " + typ + " " + dimensions + END
	
	
	override def emitINVOKESTATIC(lexeme:String, typ:String) = INDENT + "invokestatic " + lexeme + typ + END
	
	override def emitINVOKESPECIAL() = INDENT + "invokespecial java/lang/Object/<init>()V" +  END
  
	override def emitINVOKESPECIAL(lexeme:String, typ:String) = INDENT + "invokespecial " + lexeme + typ + END
	
	
	override def emitINVOKEVIRTUAL(lexeme:String, typ:String) =	 INDENT + "invokevirtual " + lexeme + typ + END
	
	
	override def emitI()  =	INDENT + "i" + END
	
	
	override def emitF()  =		 INDENT + "f" + END
	
	override def emit()  = INDENT + "" + END
	
	
	override def emitLIMITSTACK(in:Int) =	 ".limit stack " + in + END
	
	
	override def emitFCMPL()  =		 INDENT + "fcmpl" + END
	
	
	override def emitLIMITLOCAL(in:Int) = ".limit locals " + in + END
	
	
	override def emitVAR(in:Int,varName:String,inType:String,fromLabel:Int,toLabel:Int) = ".var " + in + " is " + varName + " " + inType + " from Label" + fromLabel + " to Label" + toLabel + END
	
	
	override def emitMETHOD(lexeme:String,typ:String,isStatic:Boolean) = 
		if (isStatic)
			 END + ".method public static " + lexeme + typ + END
		else
			 END + ".method public " + lexeme + typ + END
	
	
	override def emitENDMETHOD() =	 ".end method" + END
	

	override def emitSOURCE(lexeme:String) = ".source " + lexeme + END
	
	
	override def emitCLASS(lexeme:String) = ".class " + lexeme + END
	
	
	override def emitSUPER(lexeme:String) = ".super " + lexeme + END
	
	
	override def emitSTATICFIELD(lexeme:String,typ:String,isFinal:Boolean) =
		if (isFinal) (".field static final " + lexeme + " " + typ + END)
		else ".field static " + lexeme + " " + typ + END
			 			
	
	
	override def emitINSTANCEFIELD(lexeme:String, typ:String) =	 ".field " + lexeme + " " + typ + END
  
  override def emitRETURN():String = INDENT + "return" + END
  override def emitIRETURN() = INDENT + "ireturn" + END
  override def emitFRETURN() = INDENT + "freturn" + END
  override def emitARETURN() = INDENT + "areturn" + END
  
	
}