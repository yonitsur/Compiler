package AST;
import TEMP.*;
import TYPES.*;

public abstract class AST_EXP extends AST_Node
{
	public AST_EXP(int line_number) {
		super(line_number);
	}
	public String name;
	public TYPE SemantMe(TYPE returnType)
	{
		return null;
	}
	public TYPE SemantMe()
	{
		return null;
	}
	public TEMP IRme()
	{
		return null;
	}
}
