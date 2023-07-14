package AST;
import IR.*;
import TEMP.*;
import MIPS.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_VAR_SIMPLE extends AST_EXP_VAR
{
	/************************/
	/* simple variable name */
	/************************/
	
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_VAR_SIMPLE(String name, int line_number)
	{
		super(line_number);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.format("====================== var -> ID( %s )\n",name);
		this.name = name;

	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("SIMPLE\nVAR\n(%s)",name));
	}
	public TYPE SemantMe()
	{
		return  SemantMe(null);
	}
	public TYPE SemantMe(TYPE returnType)
	{
		TYPE t= SYMBOL_TABLE.getInstance().find(name);
		if (t==null)
		{
			String errorString = String.format(">> ERROR [%d:%d] non existing variable %s", 39,39,name);
			throwError(errorString, line_number);
		}
		if (name.equals(t.name)){
			String errorString = String.format(">> ERROR [%d:%d] variable name can't be equal to type name (%s) \n", 399,319,name);
			throwError(errorString, line_number);
		}
		this.paramLocal = SYMBOL_TABLE.getInstance().findParamLocal(name);
		return t;

	}

	public TEMP IRme()
	{
		
		TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_Load(t,name,paramLocal));
		
		return t;
	}
}
