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

public class IRcommand_Prologue extends IRcommand
{
	int localsCounter;

	public IRcommand_Prologue(int localsCounter)
	{
		this.localsCounter = localsCounter;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		int sp_offset = 4*localsCounter;
		MIPSGenerator.getInstance().prologue(sp_offset);
	}
}
