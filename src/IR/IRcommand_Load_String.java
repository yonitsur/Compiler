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

public class IRcommand_Load_String extends IRcommand
{
    TEMP dst;
    String str;
    String label;
    
    public IRcommand_Load_String(TEMP dst, String str, String label)
    {
        this.dst = dst;
        this.str = str;
        this.label = label;
    }
    
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().load_string(dst, str, label);
    }
}
    

