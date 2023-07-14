package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import SYMBOL_TABLE.PARAM_LOCAL.VAR_TYPE;
import TEMP.*;
import IR.*;

public class AST_STMT_ASSIGN extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_EXP_VAR var;
	public AST_EXP exp;
	public AST_NEW_EXP newExp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN(AST_EXP_VAR var,AST_EXP exp, int line_number)
	{
		this(var,exp,null,line_number);
	}
	public AST_STMT_ASSIGN(AST_EXP_VAR var,AST_NEW_EXP newExp, int line_number)
	{
		this(var,null,newExp,line_number);
	}
	public AST_STMT_ASSIGN(AST_EXP_VAR var, AST_EXP exp, AST_NEW_EXP newExp, int line_number)
	{
		super(line_number);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var ASSIGN exp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.newExp = newExp;
		this.exp = exp;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
        
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (exp != null) exp.PrintMe();
		if (newExp != null) newExp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN\nleft := right\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
		if (newExp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,newExp.SerialNumber);
	}
	public TYPE SemantMe(TYPE returnType)
	{
		TYPE t1 = null;
		TYPE t2 = null;
        
        // Lookup the var in the symbol table - look up should include inheritance
        if (SYMBOL_TABLE.getInstance().find(var.name) == null)
		{
			String errorString = String.format(">> ERROR2 [%d:%d] variable %s doesnt exist in scope \n",484,44,var.name);	
            throwError(errorString, line_number);	
		}
        
		if (var != null) t1 = var.SemantMe();
		if (exp != null) t2 = exp.SemantMe();
		if (newExp != null) t2 = newExp.SemantMe(t1.name);
		if(var.name.equals(t1.name)){	
			String errorString = String.format(">> ERROR [%d:%d] type mismatch for var := exp \n",746,726);
			throwError(errorString, line_number);
		}
		else if((t1.isArray() && t2.isNil()) || (t1.isClass() && t2.isNil()))
        {
			
        }
		else if((t1==t2)){
			if ((newExp != null) && (!newExp.t.equals(t2.name)) ){
				String errorString = String.format(">> ERROR [%d:%d] type mismatch for var := exp\n",66,66226);
				throwError(errorString, line_number);
			}
		} 
        else if (t2.isClass() && t1.isClass()){
			TYPE_CLASS tt1=(TYPE_CLASS)t1;
			TYPE_CLASS tt2=(TYPE_CLASS)t2;
		
			if ((newExp != null) && (!newExp.t.equals(t2.name)) ){
				String errorString = String.format(">> ERROR [%d:%d] type mismatch for var := exp\n",66,66226);
				throwError(errorString, line_number);
			}
			//check if t2 is a subclass of t1
			if (!tt2.isSubClassOf(tt1.name)){
				String errorString = String.format(">> ERROR [%d:%d] type mismatch for var := exp\n",16,46);				
				throwError(errorString, line_number);
			}
		}
		else if (t1.isArray() && t2.isArray()){
			TYPE_ARRAY tt1=(TYPE_ARRAY)t1;
			TYPE_ARRAY tt2=(TYPE_ARRAY)t2;
			if (tt1.type != tt2.type || t1.name != t2.name || tt1.name != tt2.name ){
				String errorString = String.format(">> ERROR [%d:%d] type mismatch for var := exp\n",64,16);				
				throwError(errorString, line_number);	
			}

		}
		else{
			String errorString = String.format(">> ERROR [%d:%d] type mismatch for var := exp\n",16,46);				
			throwError(errorString, line_number);
		}
			
           
        
		return null;
	}

    public  TYPE SemantMe(){
        return SemantMe(null);
    }

	public TEMP IRme()
	{
		TEMP var_address = null;
		if(var instanceof AST_EXP_VAR_SIMPLE){
			// get offset of simple var
			
			TEMP src = exp != null ? exp.IRme() : newExp.IRme();
			
			PARAM_LOCAL paramLocal = ((AST_EXP_VAR_SIMPLE) var).paramLocal;
			if(paramLocal.varType == PARAM_LOCAL.VAR_TYPE.LOCAL)
				IR.getInstance().Add_IRcommand(new IRcommand_Store_Local(src,paramLocal));
			else if(paramLocal.varType == PARAM_LOCAL.VAR_TYPE.GLOBAL)
				IR.getInstance().Add_IRcommand(new IRcommand_Store_Global(var.name,src));
			
		}
		else if(var instanceof AST_EXP_VAR_FIELD){
			var_address = ((AST_EXP_VAR_FIELD)var).IRme(true);
			String fname = ((AST_EXP_VAR_FIELD) var).fieldName;
			if(((AST_EXP_VAR_FIELD) var).var_class.field_map.size()==0){
				//IR.getInstance().Add_IRcommand(new IRcommand_field_access_abort(var_address));
				return null;
			}
			int offset = ((AST_EXP_VAR_FIELD) var).var_class.field_map.get(fname); 
			if(exp instanceof AST_EXP_NIL){ //assign nil ($zero) to field
				IR.
				getInstance().
				Add_IRcommand(new IRcommand_Field_Set(var_address, null, offset));
			}
			else{
				TEMP src = exp != null ? exp.IRme() : newExp.IRme();
				
				IR.
				getInstance().
				Add_IRcommand(new IRcommand_Field_Set(var_address, src, offset));
			}
		} else if (var instanceof AST_EXP_VAR_SUBSCRIPT){
			// get offset of simple var
			TEMP src = exp != null ? exp.IRme() : newExp.IRme();
			TEMP tvar = ((AST_EXP_VAR_SUBSCRIPT) var).var.IRme();
			TEMP tsub = ((AST_EXP_VAR_SUBSCRIPT) var).subscript.IRme();
			IR.getInstance().Add_IRcommand(new IRcommand_Store_Array(tvar,tsub,src));
		}
		

		return null;
	}
    
    


}
