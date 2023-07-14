/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import SYMBOL_TABLE.PARAM_LOCAL;
import TEMP.*;
import MIPS.*;

public class IRcommand_Allocate extends IRcommand
{
	String var_name;
	String type;
	int val;
	String strval;

	public IRcommand_Allocate(String var_name, String type)
	{
		this.var_name = var_name;
		this.type = type;
		this.val = 0;
		this.strval = "";
	}
	public IRcommand_Allocate(String var_name, String type, int val)
	{
		this.var_name = var_name;
		this.type = type;
		this.val = val;
		this.strval = "";
	}
	public IRcommand_Allocate(String var_name, String type, String strval)
	{
		this.var_name = var_name;
		this.type = type;
		this.val = 0;
		this.strval = strval;
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		switch (this.type) {
			case "string":
				MIPSGenerator.getInstance().allocate_string(var_name, strval);
				break;
			case "int":
				MIPSGenerator.getInstance().allocate_int(var_name, val);
				break;
		}
	}
}
