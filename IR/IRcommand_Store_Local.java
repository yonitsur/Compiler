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
import SYMBOL_TABLE.PARAM_LOCAL;

public class IRcommand_Store_Local extends IRcommand
{
	TEMP src;
	PARAM_LOCAL paramLocal;

	public IRcommand_Store_Local(TEMP src, PARAM_LOCAL paramLocal)
	{
		this.src      = src;
		this.paramLocal = paramLocal;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		int localOffset = paramLocal.getLocalOffset();
		if (paramLocal.varType == PARAM_LOCAL.VAR_TYPE.CLASS_FIELD){
			MIPSGenerator.getInstance().store_field(src, localOffset);
		}
		else
			MIPSGenerator.getInstance().store_local(src,localOffset);
	}
}
