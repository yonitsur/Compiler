package AST;
import SYMBOL_TABLE.PARAM_LOCAL;
import TEMP.*;
import IR.*;
import MIPS.*;
import TYPES.*;

public class AST_DEC_LIST extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_DEC head;
	public AST_DEC_LIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_LIST(AST_DEC head,AST_DEC_LIST tail, int line_number)
	{
		super(line_number);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		this.head = head;
		this.tail = tail;
	}

    

	public TYPE SemantMe()
	{	
        

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (head != null) head.SemantMe();
		if (tail != null) tail.SemantMe();
		
		return null;	
	}

	public TYPE SemantMe(int globalIndex)
	{
		if (head != null) {
			if (head instanceof AST_DEC_VAR){
				PARAM_LOCAL.VAR_TYPE varType = PARAM_LOCAL.VAR_TYPE.GLOBAL;
				PARAM_LOCAL paramLocal = new PARAM_LOCAL(globalIndex, varType);
				((AST_DEC_VAR)head).SemantMe(null, paramLocal);

				++globalIndex;
			} else head.SemantMe();
		}
		if (tail != null) {
			tail.SemantMe(globalIndex);
		}
		return null;
	}

	/********************************************************/
	/* The printing message for a declaration list AST node */
	/********************************************************/
	public void PrintMe()
	{
		/********************************/
		/* AST NODE TYPE = AST DEC LIST */
		/********************************/
		System.out.print("AST NODE DEC LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (head != null) head.PrintMe();
		if (tail != null) tail.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"DEC\nLIST\n");
				
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}
	public TEMP IRme()
	{
		if (head != null) head.IRme();
		if (tail != null) tail.IRme();
		
		return null;			
	}
}
