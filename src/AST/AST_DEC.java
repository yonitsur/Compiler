package AST;

import SYMBOL_TABLE.PARAM_LOCAL;
import TYPES.*;

public abstract class AST_DEC extends AST_Node
{
	public AST_DEC(int line_number) {
		super(line_number);
	}

	public TYPE SemantMe()
	{
		return null;
	}
}
