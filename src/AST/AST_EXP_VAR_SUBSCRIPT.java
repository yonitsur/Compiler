package AST;
import TYPES.*;
import TEMP.*;
import IR.*;
import MIPS.*;

public class AST_EXP_VAR_SUBSCRIPT extends AST_EXP_VAR
{
	public AST_EXP_VAR var;
	public AST_EXP subscript;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_VAR_SUBSCRIPT(AST_EXP_VAR var,AST_EXP subscript, int line_number)
	{
		super(line_number);
		System.out.print("====================== var -> var [ exp ]\n");
		this.var = var;
		this.subscript = subscript;
		this.name = var.name;
		this.fieldName = subscript.name;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.PrintMe();
		if (subscript != null) subscript.PrintMe();
	}
	public TYPE SemantMe()
	{
		return SemantMe(null);
	}
	public TYPE SemantMe(TYPE returnType)
	{
        TYPE t = var.SemantMe();
        if (t == null){
            String errorString = String.format(">> ERROR [%d:%d] type var in var[exp] doesn't exist \n",23,23);
            throwError(errorString, line_number);
        } 
        // check that subscript is integer type
        if(subscript.SemantMe() != TYPE_INT.getInstance())
        {
            String errorString = String.format(">> ERROR [%d:%d] subscript is not integer type\n",23,23);
            throwError(errorString, line_number);
        } 


		// check that var is array type
        if (t.isArray())
        {
            // return the type of the elements of the array
            return ((TYPE_ARRAY)t).type;
        }
        else
        {
            String errorString = String.format(">> ERROR [%d:%d] subscript is not array type\n",24,24);
            throwError(errorString, line_number);
        }
        return null; // This line is unreachable, but necessary for compilation of the class
	}
    
	public TEMP IRme()
	{
		TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP tvar = var.IRme();
		TEMP tsub = subscript.IRme();
		IR.getInstance().Add_IRcommand(new IRcommand_Array_Access(t,tvar,tsub));
		return t; /// ?


	}


}

