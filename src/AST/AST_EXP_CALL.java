package AST;
import TYPES.*;
import TEMP.*;
import IR.*;
import SYMBOL_TABLE.*;
public class AST_EXP_CALL extends AST_EXP
{
	
	public String funcName;
	public AST_EXP_LIST params;
	public AST_EXP_VAR var;
	public TYPE_CLASS var_class;
	public TYPE_FUNCTION func_type;
	
	public AST_EXP_CALL(String funcName,AST_EXP_LIST params, AST_EXP_VAR var, int line_number)
	{
		super(line_number);
		SerialNumber = AST_Node_Serial_Number.getFresh();

		this.funcName = funcName;
		this.params = params;
		this.var = var;
		this.name= funcName;
	}
	
	public void PrintMe()
	{
		
		System.out.format("FUNCTION CALL(%s).(%s):\n",name,funcName);

		if (var!=null) var.PrintMe();
		if (params != null) params.PrintMe();
		
			
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FUNCTION CALL(%s)\n",funcName));

		if (var!=null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (params != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,params.SerialNumber);		
	}
	public TYPE SemantMe()
	{
		return SemantMe(null);
	}
	public TYPE SemantMe(TYPE retuenType)
	{	 
		TYPE var_type;
		TYPE_CLASS tc;
		TYPE_FUNCTION tff;
		if(var!=null){
			var.SemantMe();
			var_type = SYMBOL_TABLE.getInstance().find(var.name);
			if(var_type==null){
				String errorString = String.format(">> ERROR [%d:%d] non existing variable %s\n",521,521,var.name);
				throwError(errorString, line_number);
			}	

			if(var_type.isClass()==false){
				String errorString = String.format(">> ERROR [%d:%d] call %s a non-class variable\n",53,53,var.name);
				throwError(errorString, line_number);
			}
			
			/* check if var_type(a class) has the function was callled */
			tc = (TYPE_CLASS) var_type;
			this.var_class = tc;
			/************************************/
			/* [3] Look for fiedlName inside tc */
			/************************************/
			for (TYPE_CLASS_VAR_DEC_LIST it=tc.data_members; it != null;it=it.tail)
			{
				if(it.head.t.isFunction())
				{		
					tff = (TYPE_FUNCTION) it.head.t;
					if(tff.name.equals(funcName))
					{
						if (params!=null){
							if(tff.params==null){
									String errorString = String.format(">> ERROR [%d:%d] function %s in class %s has wrong params\n",122,512,funcName,var.name);
									throwError(errorString, line_number);
								}

							TYPE_LIST t1 = params.SemantMe();
							TYPE_LIST t2 = tff.params;
							
							if((t1==null && t2!=null) || (t1!=null && t2==null)) {
								String errorString = String.format(">> ERROR [%d:%d] function %s in class %s has wrong params\n",452,452,funcName,var.name);
								throwError(errorString, line_number);
							}
							
							while (t1 != null && t2 != null)
							{
								if (!t1.head.name.equals(t2.head.name))
								{
									if (t1.head.isClass() && t2.head.isClass())
									{
										TYPE_CLASS t1_class = (TYPE_CLASS) t1.head;
										TYPE_CLASS t2_class = (TYPE_CLASS) t2.head;
										if (!t1_class.isSubClassOf(t2_class.name))
										{
											String errorString = String.format(">> ERROR [%d:%d] function %s in class %s has wrong params\n",472,472,funcName,var.name);
											throwError(errorString, line_number);
										}
									}
									else if (t1.head.isArray() && t2.head.isArray())
									{
										TYPE_ARRAY t1_array = (TYPE_ARRAY) t1.head;
										TYPE_ARRAY t2_array = (TYPE_ARRAY) t2.head;
										if (!t1_array.name.equals(t2_array.name))
										{
											String errorString = String.format(">> ERROR [%d:%d] function %s in class %s has wrong params\n",482,482,funcName,var.name);
											throwError(errorString, line_number);
										}
									}
									else if (t1.head.isFunction() && t2.head.isFunction())
									{
										TYPE_FUNCTION t1_func = (TYPE_FUNCTION) t1.head;
										TYPE_FUNCTION t2_func = (TYPE_FUNCTION) t2.head;
										if (!t1_func.name.equals(t2_func.name))
										{
											String errorString = String.format(">> ERROR [%d:%d] function %s in class %s has wrong params\n",492,492,funcName,var.name);
											throwError(errorString, line_number);
										}
									}
									else if(!(t1.head.isNil() && (t2.head.isArray() || t2.head.isClass()))){

						
										String errorString = String.format(">> ERROR [%d:%d] function %s in class %s has wrong params\n",572,572,funcName,var.name);
										throwError(errorString, line_number);
									}
				
								}
								t1 = t1.tail;
								t2 = t2.tail;
							}
							if (t1 != null || t2 != null)
							{
								String errorString = String.format(">> ERROR [%d:%d] function %s in class %s has wrong params\n",532,567,funcName,var.name);
								throwError(errorString, line_number);
							}
							
						}
						else if(tff.params!=null){
							String errorString = String.format(">> ERROR [%d:%d] function %s in class %s has wrong params\n",122,512,funcName,var.name);
							throwError(errorString, line_number);
						}
						return tff.returnType;
					}
				}

				
			}

			String errorString = String.format(">> ERROR [%d:%d] non existing function %s in class %s\n",527,522,funcName,var.name);
			throwError(errorString, line_number);
		}


		TYPE t = null;
		
		t =  SYMBOL_TABLE.getInstance().find(funcName);
		this.func_type = (TYPE_FUNCTION)t;
		
		
		if (t==null){
			String errorString = String.format(">> ERROR [%d:%d] non existing function %s\n",51,51,funcName);
			throwError(errorString, line_number);
		} 
	
		if (t.isFunction() == false)
		{
			String errorString = String.format(">> ERROR [%d:%d] call %s a non-function variable\n",53,53,"fieldName");
			throwError(errorString, line_number);
		}


		
		TYPE_FUNCTION tf = (TYPE_FUNCTION) t;
		t = SYMBOL_TABLE.getInstance().find (tf.returnType.name);

		if (t == null)
		{
			String errorString = String.format(">> ERROR [%d:%d] non existing return type %s\n",38,38,tf.returnType);
			throwError(errorString, line_number);
		}
        TYPE_LIST t1  = null;
        if (params != null)
		    t1  = params.SemantMe();

		TYPE_LIST t2  = tf.params;
        // remember: t1: params in the call, t2: params in the function declaration
		while (t1 != null && t2 != null)
		{
			if(t1.head==null && t2.head!=null){
				String errorString = String.format(">> ERROR [%d:%d] function call %s with wrong params\n",628,685,funcName);
				throwError(errorString, line_number);
			}
			if(t1.head!=null && t2.head==null){
				String errorString = String.format(">> ERROR [%d:%d] function call %s with wrong params\n",628,685,funcName);
				throwError(errorString, line_number);
			}
			if (t1.head.name.equals(t2.head.name) == false)
			{
				if (t1.head.isClass() && t2.head.isClass()){
					TYPE_CLASS tc1 = (TYPE_CLASS) t1.head;
					TYPE_CLASS tc2 = (TYPE_CLASS) t2.head;
					if (tc1.isSubClassOf(tc2.name) == false)
					{
						String errorString = String.format(">> ERROR [%d:%d] function call %s with wrong params\n",648,628,funcName);
						throwError(errorString, line_number);
					}
				}
                /* ok if expects type array and gets nil in callee */
                else if (!((t2.head.isArray()|| t2.head.isClass()) && t1.head.isNil()))
                {
                    String errorString = String.format(">> ERROR [%d:%d] function call %s with wrong params\n",634,658,funcName);
                    throwError(errorString, line_number);
                }
			}
			t1 = t1.tail;
			t2 = t2.tail;
		}
	
		
		return t;
		
	}

	public TEMP IRme()
	{
		
		TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
		int numParams = 0;
		AST_EXP_LIST curr = params;
		while (curr != null) {
			numParams++;
			curr = curr.tail;
		}
		curr = params;

		// Temporarily, if printInt or printString, just print the int/string instead of calling the function
		// TODO: implement the function call
		if (name.equals("PrintInt")) {
			IR.getInstance().Add_IRcommand(new IRcommand_PrintInt(curr.head.IRme()));
			return null;
		}
		if (name.equals("PrintString")) {
			IR.getInstance().Add_IRcommand(new IRcommand_PrintString(curr.head.IRme()));
			return null;
		}

		// reverse the order of curr
		AST_EXP_LIST prev = null; // the previous node processed
		AST_EXP_LIST next = null; // the next node to process
		while (curr != null) { // while there are more nodes to process
			next = curr.tail; // store a pointer to the next node
			curr.tail = prev; // reverse the link
			prev = curr; // move prev to the current node
			curr = next; // move curr to the next node
		}
		curr = prev; // set curr to the head of the reversed list


		for (int i = 0; i < numParams; i++)
		{
			TEMP t1 = curr.head.IRme();
			IR.getInstance().Add_IRcommand(new IRcommand_Push(t1));
			curr = curr.tail;
		}
		if (var!=null) // v (a class instance) method call
		{
			TEMP var_address = ((AST_EXP_VAR_SIMPLE)(var)).IRme();
			int offset = var_class.vtable.get(funcName);
			IR.getInstance().Add_IRcommand(new IRcommand_Push(var_address));
			numParams++;
			IR.getInstance().Add_IRcommand(new IRcommand_virtual_call(var_address, var_class.name, funcName, offset));
		}
		else if(func_type.owner != null){
			TEMP t1 = TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommand(new IRcommand_Load_This(t1));
			IR.getInstance().Add_IRcommand(new IRcommand_Push(t1));
			numParams++;
			int offset = func_type.owner.vtable.get(funcName);
			IR.getInstance().Add_IRcommand(new IRcommand_virtual_call(t1, func_type.owner.name, funcName, offset));

		}
		else{
			
			
			
			String label = funcName;
			if (!name.equals("main")) label = "f_" + label;
			IR.getInstance().Add_IRcommand(new IRcommand_jal(label));
		}
		// pop params from stack
		IR.getInstance().Add_IRcommand(new IRcommand_Pop_Params(numParams));
		// load return value to temp
		IR.getInstance().Add_IRcommand(new IRcommand_load_return_value(t));
		return t;

	
		
		

		
	}

}

		