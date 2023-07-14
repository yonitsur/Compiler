package AST;
import IR.*;
import TEMP.*;
import TYPES.*;

public class AST_EXP_BINOP extends AST_EXP
{
	int OP;
	public AST_EXP left;
	public AST_EXP right;

	public TYPE type;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_EXP left,AST_EXP right,int OP, int line_number)
	{
		super(line_number);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.OP = OP;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		String sOP="";
		
		/*********************************/
		/* CONVERT OP to a printable sOP */
		/*********************************/
		if (OP == 0) {sOP = "+";}
		if (OP == 1) {sOP = "-";}
		if (OP == 2) {sOP = "*";}
		if (OP == 3) {sOP = "/";}
		if (OP == 4) {sOP = "=";}
		if (OP == 5) {sOP = "<";}
		if (OP == 6) {sOP = ">";}

		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE BINOP EXP\n");
		System.out.format("BINOP EXP(%s)\n",sOP);

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null) left.PrintMe();
		if (right != null) right.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("BINOP(%s)",sOP));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,left.SerialNumber);
		if (right != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,right.SerialNumber);
	}
	public TYPE SemantMe()
	{
		TYPE t1 = null;
		TYPE t2 = null;
    
		if (left  != null) t1 = left.SemantMe();
		if (right != null) t2 = right.SemantMe();

		this.type = t1;

        if ((OP != 4) && (t1 != t2))
        {
			String errorString = ">> ERROR  non compatible types for binary operator";
            throwError(errorString, line_number);
        }
		

        // + - int/string
        if (OP == 0 && (t1 == TYPE_INT.getInstance() || t1 == TYPE_STRING.getInstance()))
            return t1;
        // -*/<> int
        if ((OP == 1 || OP ==2 || OP == 3 | OP == 5 || OP == 6) && t1 == TYPE_INT.getInstance()){

            // division by zero check
            if (OP == 3 && right instanceof AST_EXP_INT && ((AST_EXP_INT)right).value == 0){
                String errorString = ">> ERROR division by zero";
                throwError(errorString, line_number);
            }
            return t1;
        }
        // =

       

        if (OP == 4){
            // int/string = int/string
            if (t1 == t2){
                if (t1 == TYPE_INT.getInstance() || t1 == TYPE_STRING.getInstance() || t1 == TYPE_NIL.getInstance())
                    return TYPE_INT.getInstance();
            } // nil = class
            else if ((t1 == TYPE_NIL.getInstance() && t2.isClass()) || (t2 == TYPE_NIL.getInstance() && t1.isClass()))
                return TYPE_INT.getInstance();
            // nil = array
            else if ((t1 == TYPE_NIL.getInstance() && t2.isArray()) || (t2 == TYPE_NIL.getInstance() && t1.isArray()))
                return TYPE_INT.getInstance();
            
            if (t1.isClass() && t2.isClass()){
                TYPE_CLASS t1Class = (TYPE_CLASS)t1;
                TYPE_CLASS t2Class = (TYPE_CLASS)t2;
                // if t1class is not a subclass of t2class and t2class is not a subclass of t1class: throw error
                if (!t1Class.isSubClassOf(t2Class.name) && !t2Class.isSubClassOf(t1Class.name)){
                    String errorString = ">> ERROR non compatible types for binary operator";
                    throwError(errorString, line_number);
                }

                return TYPE_INT.getInstance();
            }
            
        }
         

        String errorString = ">> ERROR non compatible types for binary operator";
		throwError(errorString, line_number);
		return null; // This line is unreachable, but necessary for compilation of the class
	}
	public TEMP IRme()
	{
		TEMP t1 = null;
		TEMP t2 = null;
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
				
		if (left  != null) t1 = left.IRme();
		if (right != null) t2 = right.IRme();
		
		if (OP == 0)
		{
			
			if (this.type == TYPE_INT.getInstance()){
				IR.
						getInstance().
						Add_IRcommand(new IRcommand_Binop_Add_Integers(dst,t1,t2));
			}else if (this.type == TYPE_STRING.getInstance()){
				
				IR.
						getInstance().
						Add_IRcommand(new IRcommand_Binop_Add_Strings(dst,t1,t2));
			}
		}
		if (OP == 1)
		{
			IR.
					getInstance().
					Add_IRcommand(new IRcommand_Binop_Sub_Integers(dst,t1,t2));
		}
		if (OP == 2)
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_Mul_Integers(dst,t1,t2));
		}
		if (OP == 3)
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_Div_Integers(dst,t1,t2));
		}
		if (OP == 4)
		{
			if (this.type == TYPE_INT.getInstance()) {
				IR.
						getInstance().
						Add_IRcommand(new IRcommand_Binop_EQ_Integers(dst, t1, t2));
			} else if (this.type == TYPE_STRING.getInstance()){
				TEMP s0 = TEMP_FACTORY.getInstance().getFreshTEMP();
				TEMP s1 = TEMP_FACTORY.getInstance().getFreshTEMP();
				TEMP s2 = TEMP_FACTORY.getInstance().getFreshTEMP();
				TEMP s3 = TEMP_FACTORY.getInstance().getFreshTEMP();
				IR.
						getInstance().
						Add_IRcommand(new IRcommand_Binop_EQ_Strings(dst,t1,t2,s0,s1,s2,s3));
			}
		}
		if (OP == 5)
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_LT_Integers(dst,t1,t2));
		}
		if (OP == 6)
		{
			IR.
					getInstance().
					Add_IRcommand(new IRcommand_Binop_GT_Integers(dst,t1,t2));
		}
		return dst;
	}

}
