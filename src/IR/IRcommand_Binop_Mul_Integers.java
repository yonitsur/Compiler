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

public class IRcommand_Binop_Mul_Integers extends IRcommand_Binop_Arithmetic
{
	
	public IRcommand_Binop_Mul_Integers(TEMP dst,TEMP t1,TEMP t2)
	{
		super(dst,t1,t2);
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		/******************************************/
		/* mul the numbers					 */
		/******************************************/
		MIPSGenerator.getInstance().mul(dst,t1,t2);

		/******************************************/
		/* perform boundaries check				 */
		/******************************************/
		super.MIPSme();
	}
}
