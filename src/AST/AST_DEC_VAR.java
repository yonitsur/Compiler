package AST;
import TEMP.*;
import IR.*;
import TYPES.*;
import SYMBOL_TABLE.*;


public class AST_DEC_VAR extends AST_DEC
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public String type;
	public String name;
	public AST_EXP initialValue;
	public AST_NEW_EXP newInitialValue;
	//public String class_name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_VAR(String type,String name, int line_number)
	{
		this(type, name, null ,null, line_number);	}
   
    public AST_DEC_VAR(String type,String name,AST_NEW_EXP newInitialValue, int line_number)
	{
		this(type,name, null, newInitialValue, line_number);
	}

	public AST_DEC_VAR(String type,String name,AST_EXP initialValue, int line_number)
	{
		this(type,name, initialValue, null, line_number);
	}

	public AST_DEC_VAR(String type,String name, AST_EXP initialValue, AST_NEW_EXP newInitialValue, int line_number)
	{
		super(line_number);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		this.type = type;
		this.name = name;
		this.initialValue = initialValue;
		this.newInitialValue = newInitialValue;
	}

	/********************************************************/
	/* The printing message for a declaration list AST node */
	/********************************************************/
	public void PrintMe()
	{
		/********************************/
		/* AST NODE TYPE = AST DEC LIST */
		/********************************/
		if (initialValue != null) System.out.format("VAR-DEC %s :%s \n",name,type);
		else if (newInitialValue != null) System.out.format("VAR-DEC %s : new %s \n",name,type);
		else System.out.format("VAR-DEC %s :%s                \n",name,type);
		/**************************************/
		/* RECURSIVELY PRINT initialValue ... */
		/**************************************/
		if (initialValue != null) initialValue.PrintMe();
		if (newInitialValue != null) newInitialValue.PrintMe();
		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("VAR\nDEC(%s)\n:%s",name,type));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (initialValue != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,initialValue.SerialNumber);		
		if (newInitialValue != null)AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,newInitialValue.SerialNumber);
	}

	public TYPE SemantMe()
	{
		return SemantMe(null, null);
	}

	private boolean typesMatch(TYPE expType, TYPE t){
		if (expType  == t) return true;
		if ((t.isClass() || t.isArray()) && expType.isNil()) return true;
		if (t.isClass() && expType.isClass()){
			if (((TYPE_CLASS)expType).isSubClassOf(t.name)) return true;
		}
		return false;
	}

	public TYPE SemantMe(String class_name, PARAM_LOCAL paramLocal) // if not in class: class_name = null
	{
		if (class_name != null){ // if inside class
			if (newInitialValue!=null){
				String errorString = String.format(">> ERROR [%d:%d] newExp is not allowed in class\n",2,2);
                throwError(errorString, line_number);
			}
			if (!(initialValue instanceof AST_EXP_INT) 
			&& !(initialValue instanceof AST_EXP_STRING) 
			&& !(initialValue instanceof AST_EXP_NIL)
			&& initialValue != null){
				
				String errorString = String.format(">> ERROR [%d:%d]  exp is not constant\n",2,2);
                //throw new runtime exception
                throwError(errorString, line_number);
			}
		}

		TYPE t;
		/****************************/
		/* [1] Check If Type exists */
		/****************************/
		t = SYMBOL_TABLE.getInstance().find(type);
		if (t == null)
		{
            //if (!in_class){
            if (class_name == null){
                String errorString = String.format(">> ERROR [%d:%d] non existing type %s\n",2,3,type);
                throwError(errorString, line_number);
            }
            else{ // if inside class 
                // car dec in class: ok to declare field: class_name ID
                // if type of declared var != type of class name -> error
                if (!type.equals(class_name)){
                    String errorString = String.format(">> ERROR [%d:%d] non existing type %s\n",2,30,type);
                    throwError(errorString, line_number);
                }
                else{ // declared var in class with type of same class now declaring
                    return new TYPE_CLASS_VAR_DEC(null,class_name);
                }

            }
		}

		if(!t.name.equals(type)){
			String errorString = String.format(">> ERROR [%d:%d] non existing type %s\n",2,3,type);
			throwError(errorString, line_number);
		}

        // check if type is void - variable can't be declared as void
        if (t == TYPE_VOID.getInstance()){
            String errorString = String.format(">> ERROR [%d:%d] variable can't be declared as void\n",17,17);
            throwError(errorString, line_number);
        }
		
        

		/**************************************/
		/* [2] Check That Name does NOT exist */
		/**************************************/

        
        
        // check if name exists in this scope already
        if (SYMBOL_TABLE.getInstance().is_already_in_this_scope(name)){
            System.out.format("scope: %s\n",SYMBOL_TABLE.getInstance().get_top().name);
            String errorString = String.format(">> ERROR [%d:%d] variable %s already exists in scope\n",2,1,name);
            throwError(errorString, line_number);
        }


        if (initialValue != null){
			// check that type of exp is same as type of var

            TYPE expType = initialValue.SemantMe();


            if (!typesMatch(expType, t)){
                String errorString = ">> ERROR type of exp is not same as type of var\n";
                // throw exception and cath it in main
                throwError(errorString, line_number); 
            }
		}
		if (newInitialValue != null){

            TYPE newExpType = newInitialValue.SemantMe();
            
            // check that type of exp (in newExp) is same as type of var
            // check if type of newExp is same as type of t
            if (newExpType != t && !(t.isClass() && newExpType.isNil())){
                // if one is array and the other is not
                if (t.isArray() && newExpType.isArray()){

                    if (t.name.equals(newExpType.name)){
                        String errorString = ">> ERROR type of exp (in newExp) is not same as type of var -- 145\n";
						throwError(errorString, line_number);
                    }
                }
                // different types
                else{
					if (t.isClass() && newExpType.isClass()){
						
						// check if t2 is a subclass of t1
						TYPE_CLASS tt1=(TYPE_CLASS)t;
						TYPE_CLASS tt2=(TYPE_CLASS)newExpType;
						if (!tt2.isSubClassOf(tt1.name)){
							String errorString = String.format(">> ERROR [%d:%d] type mismatch for var := exp\n",16,46);
							throwError(errorString, line_number);
						}
					}
					else{
						String errorString = ">> ERROR type of exp (in newExp) is not same as type of var -- 146\n";
						throwError(errorString, line_number);
					}
                }

            }

		}

		/***************************************************/
		/* [3] Enter the Function Type to the Symbol Table */
		/***************************************************/
		SYMBOL_TABLE.getInstance().enter(name,t,paramLocal);
		this.paramLocal = paramLocal;


		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return t;		
	}
	public TEMP IRme() {
		return IRme(null);
	}
	public TEMP IRme(String owner) {
		AST_Node initVal = null;
		if (initialValue != null) {
			initVal = initialValue;
		} else if (newInitialValue != null) {
			initVal = newInitialValue;
		}
		// Allocate
		if (paramLocal.varType == PARAM_LOCAL.VAR_TYPE.GLOBAL && initVal == null) {
			IR.getInstance().Add_IRcommand(new IRcommand_Allocate(name, type));
		}
		
			
		//Store if necessary
		int val =0;
		String strval="";
		if (initVal != null && paramLocal.varType == PARAM_LOCAL.VAR_TYPE.GLOBAL){
			if (initVal instanceof AST_EXP_INT) {
				val = ((AST_EXP_INT)initVal).value;
				IR.getInstance().Add_IRcommand(new IRcommand_Allocate(name, type, val));
			}
			else if(initVal instanceof AST_EXP_STRING){
				strval = ((AST_EXP_STRING)initVal).value;
				IR.getInstance().Add_IRcommand(new IRcommand_Allocate(name, type, strval));
			}
		}	
		else if(initVal != null)  {
			IR.getInstance().Add_IRcommand(new IRcommand_Store_Local(initVal.IRme(),paramLocal));
		}
		
		return null;
	}
}
