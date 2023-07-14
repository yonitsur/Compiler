package AST;
import TEMP.*;
import IR.*;
import MIPS.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_EXP_VAR_FIELD extends AST_EXP_VAR
{
	public AST_EXP_VAR var;
	public String fieldName;
	public TYPE_CLASS var_class;
	public TYPE fieldType;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_VAR_FIELD(AST_EXP_VAR var,String fieldName, int line_number)
	{
		
		super(line_number);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.format("====================== var -> var DOT ID( %s )\n",fieldName);
		this.var = var;
		this.fieldName = fieldName;
		this.name = var.name;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.format("FIELD\nNAME\n(___.%s)\n",fieldName);

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null) var.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FIELD\nVAR\n___.%s",fieldName));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);		
	}
	public TYPE SemantMe()
	{
		return SemantMe(null);
	}
	public TYPE SemantMe(TYPE returnType)
	{


		TYPE t = null;
		TYPE_CLASS tc = null;
		
		/******************************/
		/* [1] Recursively semant var */
		/******************************/
		if (var != null) t = var.SemantMe(returnType);
		
		/*********************************/
		/* [2] Make sure type is a class */
		/*********************************/
		if (t.isClass() == false)
		{
			String errorString = String.format(">> ERROR [%d:%d] access %s field of a non-class variable\n",36,96,fieldName);
			throwError(errorString, line_number);
		}
		else
		{
			tc = (TYPE_CLASS) t;
			this.var_class= tc;
		}
		
		/************************************/
		/* [3] Look for fiedlName inside tc */
		/************************************/
		for (TYPE_CLASS_VAR_DEC_LIST it=tc.data_members;it != null;it=it.tail)
		{		
			if (it.head.name.equals(fieldName))
			{
				return it.head.t;
			}
		}
		while(tc.father != null){
			TYPE_CLASS father = (TYPE_CLASS) tc.father;
			for (TYPE_CLASS_VAR_DEC_LIST it=father.data_members;it != null;it=it.tail)
			{
				if (it.head.name.equals(fieldName))
				{
					fieldType = it.head.t;
					return it.head.t;
				}
			}
			tc=tc.father;
		}
		/*********************************************/
		/* [4] fieldName does not exist in class var */
		/*********************************************/
		String errorString = String.format(">> ERROR [%d:%d] field %s does not exist in class %s (var name : %s)\n",6,996,fieldName, tc.name,var.name);							
		throwError(errorString, line_number);
		
		return null; // This line is unreachable, but necessary for compilation of the class
	}

	public TEMP IRme()
	{
		return IRme(false);
	}

	/**
	 *
	 * @param isLeft - This statement is the left hand side of an assignment
	 * @return
	 */
	public TEMP IRme(boolean isLeft)
	{
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP varAddress = var.IRme();
		if(var_class.field_map.size() == 0){
			
			IR.getInstance().Add_IRcommand(new IRcommand_field_access_abort(varAddress));
			return null;
		}
		
		// get field offset
		int offset = var_class.field_map.get(fieldName);

		if (isLeft) return varAddress;

		//field access
		IR.getInstance().Add_IRcommand(new IRcommand_field_access(dst,varAddress,var_class.name,offset));
		return dst;
	}
}
