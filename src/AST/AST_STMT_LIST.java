package AST;
import SYMBOL_TABLE.PARAM_LOCAL;
import TEMP.*;
import TYPES.*;

public class AST_STMT_LIST extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_STMT head;
	public AST_STMT_LIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_LIST(AST_STMT head,AST_STMT_LIST tail, int line_number)
	{
		super(line_number);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (head != null) System.out.print("====================== stmts -> stmt stmts\n");
		if (tail == null) System.out.print("====================== stmts -> stmt      \n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
	}

	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST STATEMENT LIST */
		/**************************************/
		System.out.print("AST NODE STMT LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (head != null) head.PrintMe();
		if (tail != null) tail.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"STMT\nLIST\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}
	public TYPE SemantMe()
	{
		return SemantMe(null);
	}
    public TYPE SemantMe(TYPE returnType)
	{
		if (head != null) head.SemantMe(returnType);	
		if (tail != null) tail.SemantMe(returnType);
		return null;
	}

	public TYPE SemantMe(TYPE returnType, int localIndex)
	{
		if (head != null) {
			if (head instanceof AST_STMT_DEC_VAR){
				PARAM_LOCAL.VAR_TYPE varType = PARAM_LOCAL.VAR_TYPE.LOCAL;
				PARAM_LOCAL paramLocal = new PARAM_LOCAL(localIndex, varType);
				head.SemantMe(returnType, paramLocal);
				++localIndex;
			} else {
				head.SemantMe(returnType);
			}
		}
		if (tail != null) {
			tail.SemantMe(returnType, localIndex);
		}

		return null;
	}

	public TEMP IRme(String epilogue_label)
	{
		if (head != null) {
			if (head instanceof AST_STMT_RETURN || head instanceof AST_STMT_IF
				|| head instanceof AST_STMT_WHILE){
				head.IRme(epilogue_label);
			} else {
				head.IRme();
			}
		}
		if (tail != null) tail.IRme(epilogue_label);
		
		return null;
	}
}
