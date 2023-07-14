package AST;
import TYPES.*;
import TEMP.*;

public class AST_STMT_CALL extends AST_STMT
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_EXP_CALL callExp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_CALL(AST_EXP_CALL callExp, int line_number)
	{
		super(line_number);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.format(">> AST_STMT_CALL\n");
		this.callExp = callExp;
	}
	
	public void PrintMe()
	{
		callExp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("STMT\nCALL"));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,callExp.SerialNumber);		
	}
	public TYPE SemantMe() 
	{
		return SemantMe(null);
	}
	public TYPE SemantMe(TYPE returnType) 
	{
				
		TYPE t = callExp.SemantMe();
		if (t == null)
		{

			String errorString = String.format(">> ERROR [%d:%d] function call returned null type\n",99,99);
			throwError(errorString, line_number);
		}
		return t;
	}
	
	public TEMP IRme()
	{
		if (callExp != null) callExp.IRme();
		
		return null;
	}
    
}
