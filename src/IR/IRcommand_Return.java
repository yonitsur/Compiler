package IR;
import TEMP.*;
import MIPS.*;

public class IRcommand_Return  extends IRcommand{
    TEMP t ;
    String epilogueLabel;
    
    public IRcommand_Return(TEMP t, String epilogueLabel) { 
        this.t = t;
        this.epilogueLabel = epilogueLabel;
    }
    public IRcommand_Return(String epilogueLabel) { 
        this.t = null;
        this.epilogueLabel = epilogueLabel;
    }
    
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
    if (t!=null) MIPSGenerator.getInstance().return_value(t, epilogueLabel);
    else MIPSGenerator.getInstance().return_null(epilogueLabel);
    }
}