package AST;
import TEMP.*;
import IR.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_FUNC extends AST_DEC
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public String returnTypeName;
	public String name;
	public AST_TYPE_NAME_LIST params;
	public AST_STMT_LIST body;
	public int localsCounter;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_FUNC(
		String returnTypeName,
		String name,
		AST_TYPE_NAME_LIST params,
		AST_STMT_LIST body,
		int line_number)
	{
		super(line_number);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.format("AST_DEC_FUNC(%s)(%S)\n",name,returnTypeName);
		this.returnTypeName = returnTypeName;
		this.name = name;
		this.params = params;
		this.body = body;
	}

	/************************************************************/
	/* The printing message for a function declaration AST node */
	/************************************************************/
	public void PrintMe()
	{
		/*************************************************/
		/* AST NODE TYPE = AST NODE FUNCTION DECLARATION */
		/*************************************************/
		System.out.format("FUNC(%s):%s\n",name,returnTypeName);

		/***************************************/
		/* RECURSIVELY PRINT params + body ... */
		/***************************************/
		if (params != null) params.PrintMe();
		if (body   != null) body.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FUNC(%s)\n:%s\n",name,returnTypeName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (params != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,params.SerialNumber);		
		if (body   != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);		
	}

	public TYPE SemantMe()
	{
		return SemantMe((TYPE_CLASS_VAR_DEC_LIST) null, null);
	}
	
	

	public TYPE SemantMe(TYPE_CLASS_VAR_DEC_LIST classFields, String class_name)
	{
		TYPE t;
		TYPE returnType = null;
		TYPE_LIST type_list = null;

		/*******************/
		/* [0] return type */
		/*******************/
		returnType = SYMBOL_TABLE.getInstance().find(returnTypeName);
		if (returnType == null)
		{
			String errorString = String.format(">> ERROR [%d:%d] non existing return type %s\n",6,6,returnType);	
			throwError(errorString, line_number);		
		}

        // check if name exists in this scope already
        if (SYMBOL_TABLE.getInstance().is_already_in_this_scope(name)){
            String errorString = String.format(">> ERROR [%d:%d] variable %s already exists in scope / name cant be type / lib_func\n",46,44,name);
            throwError(errorString, line_number);
        }


		TYPE_FUNCTION ty;
		if (class_name != null){
			TYPE_CLASS type_class = (TYPE_CLASS)SYMBOL_TABLE.getInstance().find(class_name);
			ty = new TYPE_FUNCTION(returnType,name,type_list,type_class);
		} else {
			ty = new TYPE_FUNCTION(returnType,name,type_list,null);
		}

		//Add function name to symbol table for recursion
		SYMBOL_TABLE.getInstance().enter(name,ty);


		/****************************/
		/* [1] Begin Function Scope */
		/****************************/
		SYMBOL_TABLE.getInstance().beginScope();

		/****************************/
		/* Add class				*/
		/****************************/
		for (TYPE_CLASS_VAR_DEC_LIST it1 = classFields; it1  != null; it1 = it1.tail){
			SYMBOL_TABLE.getInstance().enter(it1.head.name,it1.head.t);
		}
		/***************************/
		/* [2] Semant Input Params */
		/***************************/
		
		int param_counter = class_name!=null ? 1 : 0;
		for (AST_TYPE_NAME_LIST it = params; it  != null; it = it.tail)
		{
			t = SYMBOL_TABLE.getInstance().find(it.head.type);
			if (t == null)
			{
				String errorString = String.format(">> ERROR [%d:%d] non existing type %s\n",2,2,it.head.type);	
				throwError(errorString, line_number);
			}
			// check if type is void - variable can't of type void
            if (t == TYPE_VOID.getInstance()){
                String errorString = String.format(">> ERROR [%d:%d] variable %s can't of type void\n",11,11, it.head.name);
                throwError(errorString, line_number);
            }

			//Add data for offset (for IRme)
			PARAM_LOCAL.VAR_TYPE varType = PARAM_LOCAL.VAR_TYPE.PARAM;
			PARAM_LOCAL paramLocal = new PARAM_LOCAL(param_counter, varType);
			++param_counter;

			type_list = new TYPE_LIST(t,type_list);
			SYMBOL_TABLE.getInstance().enter(it.head.name,t,paramLocal);
		}

		/*******************/
		/* [3] Semant Body */
		/*******************/
		//reverse type_list
        TYPE_LIST prev = null;
		TYPE_LIST curr = type_list;
		TYPE_LIST next = null;
		while (curr != null) {
			next = curr.tail;
			curr.tail = prev;
			prev = curr;
			curr = next;
		}
		type_list = prev;

		body.SemantMe(returnType,0);
		localsCounter = SYMBOL_TABLE.getInstance().getScopeLocals();
		

		/*****************/
		/* [4] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();


		/*********************************************************/
		/* [6] Return value is irrelevant for class declarations */
		/*********************************************************/
		return ty;		
	}
	public TEMP IRme()
	{
		return IRme("");
	}
	public TEMP IRme(String class_name)
	{
		//String func_label = name.equals("main") ? name: IRcommand.getFreshLabel(name);
		String func_label = name;
		if(class_name != ""){
			func_label = class_name + "_" + name;
			
		}
        if (!name.equals("main") && !name.equals("PrintInt") && !name.equals("PrintString")) func_label = "f_" + func_label;
		IR.getInstance().Add_IRcommand(new IRcommand_Label(func_label));
		/**prolog**/
		IR.getInstance().Add_IRcommand(new IRcommand_Prologue(localsCounter));
		// epilogue label
		String epilogue_label = IRcommand.getFreshLabel("epilogue");
		if (body != null) body.IRme(epilogue_label);
		/**epilogue**/
		IR.getInstance().Add_IRcommand(new IRcommand_Label(epilogue_label));
		IR.getInstance().Add_IRcommand(new IRcommand_Epilogue(localsCounter));
		return null;
	}
}
