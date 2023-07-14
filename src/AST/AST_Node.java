package AST;
import SYMBOL_TABLE.PARAM_LOCAL;
import TEMP.*;


public abstract class AST_Node
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int SerialNumber;
    public int line_number;

	//Useful for MIPS variable offset
	public PARAM_LOCAL paramLocal;

	public AST_Node(int line_number)
	{
		this.line_number = line_number;
	}
    
	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/
	public void PrintMe()
	{
		System.out.print("AST NODE UNKNOWN\n");
	}

	public void throwError(String error, int line_number) {
		System.out.format(error);
		throw new RuntimeException(Integer.toString(line_number));
	}
	/*****************************************/
	/* The default IR action for an AST node */
	/*****************************************/
	public TEMP IRme()
	{
		return null;
	}
}

