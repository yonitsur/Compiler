package AST;
import TEMP.*;
import TYPES.*;
import IR.*;
import SYMBOL_TABLE.*;

public class AST_DEC_CLASS extends AST_DEC
{
	/********/
	/* NAME */
	/********/
	public String name;
	public String father;
	public AST_CFIELD_LIST data_members;
	
	/****************/
	/* DATA MEMBERS */
	/****************/
	//public AST_CFIELD_LIST data_members;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_CLASS(String name, AST_CFIELD_LIST data_members, int line_number)
	{
		this(name, null, data_members, line_number);
	}
	public AST_DEC_CLASS(String name, String father, AST_CFIELD_LIST data_members, int line_number)
	{
		super(line_number);
        /******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
        /*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.father = father;
		this.data_members = data_members;
	}
	/*********************************************************/
	/* The printing message for a class declaration AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		System.out.format("CLASS DEC = %s\n",name);
		if (data_members != null) data_members.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("CLASS\n%s",name));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (data_members != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,data_members.SerialNumber);		
	}
	
	public TYPE SemantMe()
	{	
		/*************************/
		/* [1] Begin Class Scope */
		/*************************/

		if (SYMBOL_TABLE.getInstance().find(name) != null)
		{
			String errorString = String.format(">> ERROR2 [%d:%d] variable %s already exists in scope\n",45,44,name);				
			throwError(errorString, line_number);
		}
		
		SYMBOL_TABLE.getInstance().beginScope();

		/***************************/
		/* [2] Semant Data Members */
		/***************************/
		
		TYPE_CLASS tc = new TYPE_CLASS(null,name,null);
		
		if(father!=null){
			
			TYPE_CLASS fatherType = (TYPE_CLASS)SYMBOL_TABLE.getInstance().find(father);

			if(fatherType == null){
				String errorString = String.format(">> ERROR1 [%d:%d] class %s does not exist\n",44,44,father);				
				throwError(errorString, line_number);
			}
			tc.father = fatherType;
			TYPE_CLASS_VAR_DEC_LIST fdm = fatherType.data_members;
			tc.data_members = fdm;
			SYMBOL_TABLE.getInstance().enter(name,tc);
			TYPE_CLASS_VAR_DEC_LIST dm = data_members.SemantMe(fdm,tc);
			tc.father= fatherType;
			tc.data_members = dm;
				
			
		}
		else{

			SYMBOL_TABLE.getInstance().enter(name,tc);
			tc.data_members = data_members.SemantMe(tc);
		}
		
		/*****************/
		/* [3] End Scope */
		/*****************/

		SYMBOL_TABLE.getInstance().endScope();

		/************************************************/
		/* [4] Enter the Class Type to the Symbol Table */
		/************************************************/
		
		tc.cfields = data_members;
		SYMBOL_TABLE.getInstance().enter(name,tc);
		AST_CFIELD_LIST cf = tc.cfields;
        int indexm = 0;
		int index_field_map = 4;
        while (cf != null && cf.head != null){
			
            if (cf.head.f != null){
				tc.methods.add(cf.head.f.name);
				tc.methods_classes.add(tc.name);		
				//tc.only_son_funcs.put(cf.head.f.name,tc.name);
                tc.vtable.put(cf.head.f.name, indexm);
                indexm+=4;
            }
			else{
		
				tc.field_map.put(cf.head.v.name, index_field_map);
				index_field_map+=4;

			}
            cf = cf.tail;
            
        }
		TYPE_CLASS tf = tc.father;
        TYPE_CLASS tccc = tc;
        while(tf!=null){
			
            AST_CFIELD_LIST ctf = tf.cfields;
			
            while (ctf != null && ctf.head != null){
                if (ctf.head.f != null && !tccc.vtable.containsKey(ctf.head.f.name)){
					tccc.methods.add(ctf.head.f.name);
					tccc.methods_classes.add(tf.name);
                    // tccc.only_son_funcs.put(ctf.head.f.name,tf.name);
					tccc.vtable.put(ctf.head.f.name, indexm);
                    indexm+=4;
                }
				else if (ctf.head.v != null && !tccc.field_map.containsKey(ctf.head.v.name)){
					System.out.println("here4444444444444444"+ ctf.head.v.name);
					tccc.field_map.put(ctf.head.v.name, index_field_map);
					index_field_map+=4;

				}
                ctf = ctf.tail;
            }
            tc=tf;
            tf = tf.father;
        }
		/*********************************************************/
		/* [5] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;		
	}
	public TEMP IRme()
	{
		TYPE_CLASS tc = (TYPE_CLASS)SYMBOL_TABLE.getInstance().find(name);
		
		IR.getInstance().Add_IRcommand(new IRcommand_Class_Dec(tc));
		AST_CFIELD_LIST cf = tc.cfields;
		while (cf != null && cf.head != null){
			if(cf.head.f!=null){
				cf.head.f.IRme(tc.name);
			}
			cf = cf.tail;
		}
		return null;
	
		
	}
}
