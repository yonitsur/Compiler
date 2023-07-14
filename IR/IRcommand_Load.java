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

public class IRcommand_Load extends IRcommand
{
	TEMP dst;
	String var_name;
	PARAM_LOCAL paramLocal;
	
	public IRcommand_Load(TEMP dst, String var_name, PARAM_LOCAL paramLocal)
	{
		this.dst      = dst;
		this.var_name = var_name;
		this.paramLocal = paramLocal;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
	
		if (paramLocal.varType == PARAM_LOCAL.VAR_TYPE.GLOBAL) {
			MIPSGenerator.getInstance().load_global(dst, var_name);
		} else if (paramLocal.varType == PARAM_LOCAL.VAR_TYPE.CLASS_FIELD){
			int localOffset = paramLocal.getLocalOffset();
			MIPSGenerator.getInstance().load_field(dst, localOffset);

		}else {
			int localOffset = paramLocal.getLocalOffset();
			MIPSGenerator.getInstance().load(dst, localOffset);
		}
	}
}
