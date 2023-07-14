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
import TEMP.*;
import MIPS.*;

public class IRcommand_Store_Global extends IRcommand
{
	String var_name;
	TEMP src;
	
	public IRcommand_Store_Global(String var_name, TEMP src)
	{
		this.src      = src;
		this.var_name = var_name;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().store_global(var_name,src);
	}
}
