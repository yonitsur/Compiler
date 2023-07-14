package AST;
import TYPES.*;
import TEMP.*;
import IR.*;

public class AST_EXP_NIL extends AST_EXP {
    public AST_EXP_NIL(int line_number)
	{
		super(line_number);
        SerialNumber = AST_Node_Serial_Number.getFresh();
    
        System.out.print("====================== exp -> NIL\n");
		this.name="nil";
    }
    public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE NIL\n");

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("NIL"));
    }
    public TYPE SemantMe() {
        return TYPE_NIL.getInstance();
    }
	public TEMP IRme()
	{
		TEMP z = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_move_to_zero(z));
		return z;
	}
	
}
