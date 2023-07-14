package AST;
import SYMBOL_TABLE.PARAM_LOCAL;
import TEMP.*;
import TYPES.*;

public class AST_STMT_DEC_VAR extends AST_STMT
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_DEC_VAR var;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_DEC_VAR(AST_DEC_VAR var, int line_number)
	{
		super(line_number);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		this.var = var;
	}
	public TYPE SemantMe()
	{
		return SemantMe(null);
	}
	public TYPE SemantMe(TYPE retType){
		return SemantMe(retType,null);
	}
	public TYPE SemantMe(TYPE retType, PARAM_LOCAL paramLocal)
	{
		String ns = "";
		if (var.newInitialValue != null)
			ns = " new";
		
		System.out.format("AST_STMT_DEC_VAR: var %s:%s %s\n",var.name,ns,var.type);
		TYPE t = var.SemantMe(null, paramLocal);
		
		return t;
	}
	
	public void PrintMe()
	{
		var.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("STMT\nDEC\nVAR"));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);		
	}

    public TEMP IRme()
	{
		return var.IRme();
	}


}
