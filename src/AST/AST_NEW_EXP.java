package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_NEW_EXP extends AST_Node
{
    public String t;
    public AST_EXP e;
   
	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_NEW_EXP(String t, AST_EXP e, int line_number)
	{
		super(line_number);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		String expStr = e!=null ? "[exp]" : "";
		System.out.format("====================== newExp -> NEW type %s\n", expStr);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
        this.t = t;
        this.e = e;
	}
    public AST_NEW_EXP(String  t, int line_number)
	{
		this(t,null,line_number);
	}
    public void PrintMe()
	{
		
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.print("AST NODE newExp\n");

		/**************************************/
		/* RECURSIVELY PRINT */
		/**************************************/
       
		if (e != null) e.PrintMe();
			
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("newExp: %s ",t));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (e  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,e.SerialNumber);
	}
    public TYPE SemantMe(String name){
        // its just: new ID. for example: new Dog
        if (this.e == null){
            TYPE_CLASS tc = (TYPE_CLASS)SYMBOL_TABLE.getInstance().find(this.t);
            if (tc == null){
                String errorString = String.format(">> ERROR [%d:%d] non existing type %s\n",-5,-5,this.t);
                throwError(errorString, line_number);
            }
			
			int offset = 4;
			for (TYPE_CLASS_VAR_DEC_LIST it = tc.data_members; it != null; it = it.tail) {
				//check if the field is a function
				if (!(it.head.t instanceof TYPE_FUNCTION)) {
					tc.field_map.put(it.head.name, offset);
					offset += 4;
				}
			}
            return tc;
        }
        // its: new ID[exp]. for example: new int[5].
        else{
            TYPE tt = SYMBOL_TABLE.getInstance().find(this.t);
            if (tt == null){
                String errorString = String.format(">> ERROR [%d:%d] non existing type %s\n",-51,-51,this.t);
                throwError(errorString, line_number);
            }
            TYPE t_e = this.e.SemantMe();
            if (t_e.name != "int"){
                String errorString = String.format(">> ERROR [%d:%d] array size has to be integral value \n",-1,-1);
                throwError(errorString, line_number);
            }
            // check that the size is greater than 0
            if (this.e instanceof AST_EXP_INT){
                AST_EXP_INT te_int = (AST_EXP_INT)this.e;
                if (te_int.value <= 0){
                    String errorString = String.format(">> ERROR [%d:%d] array size has to be greater than 0\n",-10,-10);
                    throwError(errorString, line_number);
                }
            }

            return new TYPE_ARRAY(tt,name); // update name in AST_VAR_DEC when we know it
    	}

    }

	public TYPE SemantMe(){
		return SemantMe("");
	}

	public TEMP IRme()
	{
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
		if (this.e == null){
			
			TYPE_CLASS tc = (TYPE_CLASS)SYMBOL_TABLE.getInstance().find(this.t);
			// int offset = 4;

			// for (TYPE_CLASS_VAR_DEC_LIST it = tc.data_members; it != null; it = it.tail) {
			// 	//check if the field is a function
			// 	if (!(it.head.t instanceof TYPE_FUNCTION)) {
			// 		tc.field_map.put(it.head.name, offset);
			// 		offset += 4;
			// 	}
			// 	else{
			// 		// tc.vtable.put(it.head.name, offsetv);
			// 		// offsetv += 4;
			// 	}

			// }
			IR.getInstance().Add_IRcommand(new IRcommand_NewObject(dst,tc.name, (tc.field_map.size()+1)*4));
			AST_EXP val;
			for (AST_CFIELD_LIST it = tc.cfields; it != null; it = it.tail) {
				AST_CFIELD itt = it.head;
				if (itt.v != null && itt.v.initialValue != null) {
					val = itt.v.initialValue;
					TEMP tmp = TEMP_FACTORY.getInstance().getFreshTEMP();
					IR.getInstance().Add_IRcommand(new IRcommand_store_field_initialvalue(tmp, itt.v.name, val, dst, tc.field_map.get(itt.v.name), tc.name));
				}
			}
			
		} else {
			TEMP size = this.e.IRme();
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_NewArray(dst, size));
		}
		return dst;
	}
}