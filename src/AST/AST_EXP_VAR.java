package AST;

import TYPES.*;

public abstract class AST_EXP_VAR extends AST_EXP
{
	public AST_EXP_VAR(int line_number) {
		super(line_number);
	}
	
	public String fieldName;
	public TYPE SemantMe(TYPE returnType)
	{
		return null;
	}
	public TYPE SemantMe()
	{
		return null;
	}
}
