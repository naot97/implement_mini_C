/**
*	@author Dr.Nguyen Hua Phung
*	@version 1.0
*	28/6/2006
* 	This class provides facilities for method generation
*
*/
package mc.codegen;

import scala.collection.mutable.Stack
import mc.utils._

 class Frame( name:String,val returnType:Type) {
	var currentLabel = 0;
	var currOpStackSize = 0;
	var maxOpStackSize = 0;
	var currIndex = 0;
	var maxIndex = 0;
	var startLabel = Stack[Int]();
	var endLabel = Stack[Int]();
	var indexLocal = Stack[Int]();
	var conLabel = Stack[Int]();
	var brkLabel = Stack[Int]();
  
	def getCurrIndex() = currIndex
		
  def setCurrIndex(index:Int) =	currIndex = index
	
	
	/**
	*   return a new label in the method.
	*   @return an integer representing the label.
	*/
	def getNewLabel() =
	{
		val tmp = currentLabel;
		currentLabel = currentLabel + 1;
	  tmp;
	}
	/**
	*	simulate an instruction that pushes a value onto operand stack.
	*/
	def push() 
	{
		currOpStackSize = currOpStackSize + 1
		if (maxOpStackSize < currOpStackSize)
			maxOpStackSize = currOpStackSize;
		
	}
	/**
	*	simulate an instruction that pops a value out of operand stack.
	*/
	def pop() =
	{
		currOpStackSize = currOpStackSize - 1
		if (currOpStackSize < 0)
			throw IllegalRuntimeException("Pop empty stack");
	}
  def getStackSize() = currOpStackSize
	/**
	*	return the maximum size of the operand stack that the method needs to use.
	*	@return an integer that represent the maximum stack size
	*/
	def getMaxOpStackSize() = maxOpStackSize
	
	/** 
	*	check if the operand stack is empty or not.
	*	@throws IllegalRuntimeException if the operand stack is not empty.
	*/
	def checkOpStack() = if (currOpStackSize != 0) throw  IllegalRuntimeException("Stack not empty");
	
	/**
	*	invoked when parsing into a new scope inside a method.<p>
	*	This method will create 2 new labels that represent the starting and ending points of the scope.<p>
	*	Then, these labels are pushed onto corresponding stacks.<p>
	*	These labels can be retrieved by getStartLabel() and getEndLabel().<p>
	*	In addition, this method also saves the current index of local variable.
	*/
	
	def enterScope(isProc:Boolean) = 
	{
		val start = getNewLabel();
		val end = getNewLabel();
		startLabel.push(start);
		endLabel.push(end);
		indexLocal.push(currIndex);
		if (isProc) {
			maxOpStackSize = 0;
			maxIndex = 0;
		}
	}
	/**
	* 	invoked when parsing out of a scope in a method.<p>
	* 	This method will pop the starting and ending labels of this scope
	*	and restore the current index
	*/
	def exitScope() = 
	{
		if (startLabel.isEmpty || endLabel.isEmpty || indexLocal.isEmpty)
			throw  IllegalRuntimeException("Error when exit scope");		
		startLabel.pop();
		endLabel.pop();
		currIndex = indexLocal.pop();
	}
	/**
	*	return the starting label of the current scope.
	*	@return an integer representing the starting label
	*/
	def getStartLabel() = 
	{
		if (startLabel.isEmpty)
			throw IllegalRuntimeException("None start label");
		startLabel.top;
	}
	/**
	*	return the ending label of the current scope.
	*	@return an integer representing the ending label
	*/
	
	def getEndLabel() = 
	{
		if (endLabel.isEmpty)
			throw  IllegalRuntimeException("None end label");
		endLabel.top;
	}
	/**
	*	return a new index for a local variable declared in a scope. 
	*	@return an integer that represents the index of the local variable
	*/
	def getNewIndex() = 
	{
		val tmp = currIndex;
		currIndex = currIndex + 1;
		if (currIndex > maxIndex)
			maxIndex = currIndex;
		tmp;
	}
	/**
	*	return the maximum index used in generating code for the current method
	*	@return an integer representing the maximum index
	*/
	def getMaxIndex() = maxIndex
	/**
	*	invoked when parsing into a loop statement.<p>
	*	This method creates 2 new labels that represent the starting and ending label of the loop.<p>
	*	These labels are pushed onto corresponding stacks and are retrieved by getBeginLoopLabel() and getEndLoopLabel().
	*/
	def enterLoop() =
	{
		val con = getNewLabel();
		val brk = getNewLabel();
		conLabel.push(con);
		brkLabel.push(brk);
	}
	/**
	*	invoked when parsing out of a loop statement.
	*	This method will take 2 labels representing the starting and ending labels of the current loop out of its stacks.
	*/
	def exitLoop() =
	 {
		if (conLabel.isEmpty || brkLabel.isEmpty)
			throw IllegalRuntimeException("Error when exit loop");
		conLabel.pop();
		brkLabel.pop();
	}
	/**
	*	return the label of the innest enclosing loop to which continue statement would jump
	*	@return an integer representing the continue label
	*/
	def getContinueLabel() =
	{
		if (conLabel.isEmpty)
			throw  IllegalRuntimeException("None continue label");
		conLabel.top;
	}
	/**
	*	return the label of the innest enclosing loop to which break statement would jump
	*	@return an integer representing the break label
	*/	
	def getBreakLabel() =
	{
		if (brkLabel.isEmpty)
			throw IllegalRuntimeException("None break label");
	  brkLabel.top;
	}
}

