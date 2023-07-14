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
import TEMP.*;

public class IRcommand_Binop_Add_Strings extends IRcommand
{
	public TEMP t1;
	public TEMP t2;
	public TEMP dst;

	public IRcommand_Binop_Add_Strings(TEMP dst, TEMP t1, TEMP t2)
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
		/******************************************/
		/* add the numbers					 */
		/******************************************/
		// get fresh label
		String label = getFreshLabel("LenLoop");
		String label2 = getFreshLabel("LenLoop");
		String label3 = getFreshLabel("ConcatFirstLoop");
		String label4 = getFreshLabel("MidLoop");
		String label5 = getFreshLabel("ConcatSecondLoop");
		String label6 = getFreshLabel("ConcatLoopEnd");

		// get strings length
		TEMP t_length1 = TEMP_FACTORY.getInstance().getFreshTEMP();
		MIPSGenerator.getInstance().get_length(t_length1,t1, label);
		TEMP t_length2 = TEMP_FACTORY.getInstance().getFreshTEMP();
		MIPSGenerator.getInstance().get_length(t_length2,t2, label2);	;
		// allocate new string
		MIPSGenerator.getInstance().add(t_length2,t_length1,t_length2);
		MIPSGenerator.getInstance().addi(t_length2,t_length2,1);
		MIPSGenerator.getInstance().malloc(dst,t_length2);
		// concat strings
		MIPSGenerator.getInstance().concat_string(dst,t1,t2,label3, label4, label5, label6);
	}
}
