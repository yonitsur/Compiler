package AST;
import TYPES.*;
import IR.IR;
import IR.IRcommand_Return;
import TEMP.*;


public class AST_STMT_RETURN extends AST_STMT
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_EXP exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_RETURN(int line_number){
		this(null,line_number);
    }
	public AST_STMT_RETURN(AST_EXP exp, int line_number)
	{
		super(line_number);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		String expStr = exp!= null ? "exp" : "";
		System.out.format("====================== stmt -> RETURN %s \n",expStr);

		this.exp = exp;
	}

	/************************************************************/
	/* The printing message for a function declaration AST node */
	/************************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE STMT RETURN\n");

		/*****************************/
		/* RECURSIVELY PRINT exp ... */
		/*****************************/
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"RETURN");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

    /* Check that exp Type agrees with function return Type */
    public TYPE SemantMe(){
        return SemantMe(null);
    }

    public TYPE SemantMe(TYPE returnType){      
        if (returnType == TYPE_VOID.getInstance()){
            if (exp != null){
                String errorString = String.format(">> ERROR [%d:%d] return expression in void function\n",11,1);
                throwError(errorString, line_number);
            }
			return null;
        }
        else{ // returnType is not void
            if (exp == null){
                String errorString = String.format(">> ERROR [%d:%d] return expression is missing\n",11,01);
                throwError(errorString, line_number);
            }
        }
        
        TYPE t = null;
        if (exp != null) {
            t = exp.SemantMe();
            if (t == null) {
                String errorString = String.format(">> ERROR [%d:%d] return expression type is null\n",747,77);
				throwError(errorString, line_number);
			}
        }
        if (returnType != null){
            if (returnType.isClass() && t.isNil()){
                return t;
            }
            if((t.isClass() && !returnType.isClass()) || (t.isClass() && returnType.isClass() && !t.isClass() )){
                    String errorString = String.format(">> ERROR [%d:%d] return expression type is not the same as function return type: %s\n",838,88,returnType.name);
                    throwError(errorString, line_number);
            
            }		
            if(t.isClass() && (returnType != null) && returnType.isClass()) {
                System.out.format("t.name: %s	returnType.name: %s\n",	t.name,returnType.name);
                TYPE_CLASS tClass = (TYPE_CLASS)t;
                TYPE_CLASS returnTypeClass = (TYPE_CLASS)returnType;
                if(!tClass.isSubClassOf(returnTypeClass.name)) {
                    String errorString = String.format(">> ERROR [%d:%d] return expression type is not the same as function return type: %s\n",838,880,returnType.name);
                    throwError(errorString, line_number);
                }
                return t;
            }

            if (returnType != null && t != null){
                if (returnType.isArray()){
                    if (t.isArray()){
                        TYPE_ARRAY tArray = (TYPE_ARRAY)t;
                        TYPE_ARRAY returnTypeArray = (TYPE_ARRAY)returnType;
                        if (!tArray.type.name.equals(returnTypeArray.type.name)){
                            String errorString = String.format(">> ERROR [%d:%d] return expression type is not the same as function return type: %s\n",8,88,returnType.name);
                            throwError(errorString, line_number);
                        }
                    }
                    else if(!t.isNil()){
                        String errorString = String.format(">> ERROR [%d:%d] return expression type is not the same as function return type: %s\n",8,88,returnType.name);
                        throwError(errorString, line_number);
                    }
                    return t;
                }
            }
        }

		if(t!=returnType){
            
			String errorString;
			if (returnType == null) errorString = String.format(">> ERROR [%d:%d] return expression type is not the same as function return type\n",87,87);
			else errorString = String.format(">> ERROR [%d:%d] return expression type is not the same as function return type: %s\n",885,858,returnType.name);
			throwError(errorString, line_number);
		}
		
		
        return t;
    }

    public TEMP IRme(String epilogue_label){
        if (exp != null){
            TEMP t = exp.IRme();
            IR.getInstance().Add_IRcommand(new IRcommand_Return(t, epilogue_label));
        }
        else{
            IR.getInstance().Add_IRcommand(new IRcommand_Return(epilogue_label));
        }
        return null;
    }
}
