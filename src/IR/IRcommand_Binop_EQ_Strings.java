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

public class IRcommand_Binop_EQ_Strings extends IRcommand
{
	public TEMP t1;
	public TEMP t2;
	public TEMP s0;
	public TEMP s1;
	public TEMP s2;
	public TEMP s3;
	public TEMP dst;

	public IRcommand_Binop_EQ_Strings(TEMP dst, TEMP t1, TEMP t2, TEMP s0, TEMP s1, TEMP s2, TEMP s3)
	{
		this.dst = dst;
		this.t1 = t1;
		this.t2 = t2;
		this.s0 = s0;
		this.s1 = s1;
		this.s2 = s2;
		this.s3 = s3;
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		/******************************************/
		/* Inline string comparison */
		/******************************************/

		MIPSGenerator.getInstance().li(dst,1);
		MIPSGenerator.getInstance().move(s0,t1);
		MIPSGenerator.getInstance().move(s1,t2);

		String label_str_eq_loop = getFreshLabel("str_eq_loop");
		/******************************************/
		/* STR EQ LOOP */
		/******************************************/
		MIPSGenerator.getInstance().label(label_str_eq_loop);

		MIPSGenerator.getInstance().lb(s2,0, s0);
		MIPSGenerator.getInstance().lb(s3,0, s1);

		String label_neq_label = getFreshLabel("neq_label");
		MIPSGenerator.getInstance().bne(s2,s3,label_neq_label);

		String label_str_eq_end = getFreshLabel("str_eq_end");
		MIPSGenerator.getInstance().beqz(s2, label_str_eq_end);

		MIPSGenerator.getInstance().addu(s0,s0,1);
		MIPSGenerator.getInstance().addu(s1,s1,1);

		MIPSGenerator.getInstance().jump(label_str_eq_loop);

		/******************************************/
		/* NEQ LABEL */
		/******************************************/
		MIPSGenerator.getInstance().label(label_neq_label);
		MIPSGenerator.getInstance().li(dst,0);

		/******************************************/
		/* STR EQ END */
		/******************************************/
		MIPSGenerator.getInstance().label(label_str_eq_end);
	}
}
