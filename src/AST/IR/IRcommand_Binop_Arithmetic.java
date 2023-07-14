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

public class IRcommand_Binop_Arithmetic extends IRcommand
{
	public TEMP t1;
	public TEMP t2;
	public TEMP dst;

	protected static int MAX = 32767;
	protected static int MIN = -32768;

	public IRcommand_Binop_Arithmetic(TEMP dst, TEMP t1, TEMP t2)
	{
		this.dst = dst;
		this.t1 = t1;
		this.t2 = t2;
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		/*******************************/
		/* [1] Allocate 2 fresh labels */
		/*******************************/
		String label_end        = getFreshLabel("end");
		String label_Min  = getFreshLabel("Min");

		/******************************************/
		/* [2] check overflow (max)				*/
		/******************************************/
		MIPSGenerator.getInstance().li(t1,MAX);
		MIPSGenerator.getInstance().ble(dst,t1,label_Min);
		MIPSGenerator.getInstance().li(dst,MAX);
		MIPSGenerator.getInstance().jump(label_end);

		/******************************************/
		/* [3] check underflow (min)			*/
		/******************************************/
		MIPSGenerator.getInstance().label(label_Min);
		MIPSGenerator.getInstance().li(t1,MIN);
		MIPSGenerator.getInstance().bge(dst,t1,label_end);
		MIPSGenerator.getInstance().li(dst,MIN);

		/******************/
		/* [4] label_end: */
		/******************/
		MIPSGenerator.getInstance().label(label_end);
	}
}
