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

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Binop_Sub_Integers extends IRcommand_Binop_Arithmetic
{
	public IRcommand_Binop_Sub_Integers(TEMP dst, TEMP t1, TEMP t2)
	{
		super(dst,t1,t2);
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		/******************************************/
		/* add the numbers					 */
		/******************************************/
		MIPSGenerator.getInstance().sub(dst,t1,t2);

		/******************************************/
		/* perform boundaries check				 */
		/******************************************/
		super.MIPSme();
	}
}
