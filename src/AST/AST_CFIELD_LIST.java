package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_CFIELD_LIST extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_CFIELD head;
	public AST_CFIELD_LIST tail;
	

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_CFIELD_LIST(AST_CFIELD head,AST_CFIELD_LIST tail, int line_number)
	{
		super(line_number);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null) System.out.print("====================== cfield -> cfield cfields\n");
		if (tail == null) System.out.print("====================== cfields -> cfield      \n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
	}

	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST STATEMENT LIST */
		/**************************************/
		System.out.print("AST NODE CFIELD LIST\n");

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
			"CFIELD\nLIST\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}
	
	public TYPE_CLASS_VAR_DEC_LIST SemantMe()
	{
		return SemantMe(null,null);
	}
	public TYPE_CLASS_VAR_DEC_LIST SemantMe(TYPE_CLASS tc)
	{
		return SemantMe(null,tc);
	}
	public TYPE_CLASS_VAR_DEC_LIST SemantMe(TYPE_CLASS_VAR_DEC_LIST fdm)
	{
		return SemantMe(fdm,null);
	}
	
	public TYPE_CLASS_VAR_DEC_LIST SemantMe(TYPE_CLASS_VAR_DEC_LIST fdm, TYPE_CLASS tc){
		if (tail == null)
		{
			return new TYPE_CLASS_VAR_DEC_LIST(
					head.SemantMe(fdm,tc),fdm);
		}
		else
		{
			
			return new TYPE_CLASS_VAR_DEC_LIST(
				head.SemantMe(fdm,tc),
				tail.SemantMe(fdm,tc));
		}	
	}
	// public TEMP IRme()
	// {
	// 	// if (tail != null) tail.IRme();
	// 	// if (head != null) head.IRme();
	// 	return null;
	// }
}
