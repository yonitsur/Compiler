package AST;

import SYMBOL_TABLE.PARAM_LOCAL;
import TEMP.TEMP;
import TYPES.*;

public abstract class AST_STMT extends AST_Node
{
	public AST_STMT(int line_number) {
		super(line_number);
	}
	/*********************************************************/
	/* The default message for an unknown AST statement node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST STATEMENT NODE");
	}
    /* Samentme */
	public  TYPE SemantMe() 
	{
		return null;
	}
	public  TYPE SemantMe(TYPE retType) 
	{
		return null;
	}
	public  TYPE SemantMe(TYPE retType, PARAM_LOCAL paramLocal)
	{
		return null;
	}

	public TEMP IRme(String epilogue_label)
	{
		return null;
	}
}
