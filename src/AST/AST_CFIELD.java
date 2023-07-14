package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import SYMBOL_TABLE.PARAM_LOCAL.VAR_TYPE;
import TEMP.*;
import IR.*;
import MIPS.*;




public class AST_CFIELD extends AST_Node
{
	/************************/
	/* simple variable name */
	/************************/
	public AST_DEC_VAR v;
	public AST_DEC_FUNC f;
	public boolean is_var;
	public TYPE_CLASS typeclass;
	
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	
	public AST_CFIELD(AST_DEC_VAR v, int line_number)
	{
		this(v, null, line_number);

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== cFIELD -> VAR_DEC\n");
	}
	
    public AST_CFIELD(AST_DEC_FUNC f, int line_number)
	{
		this(null, f, line_number);

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== cFIELD -> FUNC_DEC\n");
	}

	public AST_CFIELD(AST_DEC_VAR v, AST_DEC_FUNC f, int line_number)
	{
		super(line_number);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
        this.v = v;
		this.f = f;	
		
	}


	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE cFIELD\n");
        if (v != null) v.PrintMe();
		if (f != null) f.PrintMe();
		

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("cFIELD\n"));

        if (v != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,v.SerialNumber);	
        if (f != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,f.SerialNumber);
		
	}
	/*****************/
	/* SEMANT ME ... */
	/*****************/
	public TYPE_CLASS_VAR_DEC SemantMe()
	{		
		return SemantMe(null,null);
	}
	public TYPE_CLASS_VAR_DEC SemantMe(TYPE_CLASS_VAR_DEC_LIST fdm)
	{		
		return SemantMe(fdm,null);
	}

	public TYPE_CLASS_VAR_DEC SemantMe(TYPE_CLASS tc)
	{		
		return SemantMe(null,tc);
	}
	private boolean check_lists(AST_TYPE_NAME_LIST params1, TYPE_LIST params2){
		if (params1!=null){
			TYPE_LIST lst1 = params1.SemantMe();
			TYPE_LIST lst2 = params2;		

			while (lst1 != null && lst2 != null)
			{
				if (!lst1.head.name.equals(lst2.head.name))
				{
					return false;
				}
				lst1 = lst1.tail;
				lst2 = lst2.tail;
			}
			if (lst1 != null || lst2 != null)
			{
				return false;
			}
			return true;
		}
		return params2 == null;
	}


	public TYPE_CLASS_VAR_DEC SemantMe(TYPE_CLASS_VAR_DEC_LIST fdm, TYPE_CLASS tc)
	{		
		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/

		// If field name appears among Father members, it must be same type and for functions, also same params. (same signature)
		this.typeclass = tc;
		if (v!=null){
			for (TYPE_CLASS_VAR_DEC_LIST it = fdm; it != null; it = it.tail) {
				TYPE_CLASS_VAR_DEC member = it.head;
				if (v.name.equals(member.name) && (member.t.isFunction() || !v.type.equals(member.t.name))) {
						String errorString = ">>>ERROR: 1The variable is not compatible with the parent\n";
						throwError(errorString, line_number);
					}
			}
			
			PARAM_LOCAL.VAR_TYPE varType = PARAM_LOCAL.VAR_TYPE.CLASS_FIELD;
			
			int offset = (tc.field_map.size()+1)*4;
			tc.field_map.put(v.name, offset);
			PARAM_LOCAL paramLocal = new PARAM_LOCAL(offset, varType); 
		
			
            TYPE_CLASS_VAR_DEC yy = new TYPE_CLASS_VAR_DEC(v.SemantMe(tc.name, paramLocal), v.name); //// here changed
			tc.data_members = new TYPE_CLASS_VAR_DEC_LIST(yy, tc.data_members);
            return yy;
		} else {		
			for (TYPE_CLASS_VAR_DEC_LIST it = fdm; it != null; it = it.tail) {
				TYPE_CLASS_VAR_DEC member = it.head;
				if (f.name.equals(member.name)) {
					if (member.t.isFunction()) {
						TYPE_FUNCTION parent_f = (TYPE_FUNCTION)(member.t);
						if (!f.returnTypeName.equals(parent_f.returnType.name)
						|| !check_lists(f.params, parent_f.params)){
							String errorString = ">>>ERROR: 2The variable is not compatible with the parent\n";
							throwError(errorString, line_number);
						}
					} else {
						String errorString = ">>>ERROR: 3The variable is not compatible with the parent\n";
						throwError(errorString, line_number);
					}
				}
			}
			System.out.println("fdm: " + fdm);
			TYPE_CLASS_VAR_DEC yy= new TYPE_CLASS_VAR_DEC(f.SemantMe(fdm, tc.name), f.name);
			tc.data_members = new TYPE_CLASS_VAR_DEC_LIST(yy, tc.data_members);
			return yy;
		}
	}
}
