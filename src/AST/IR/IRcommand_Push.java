package IR;
import TEMP.*;
import MIPS.*;

public class IRcommand_Push  extends IRcommand{
    TEMP t ;
    
    public IRcommand_Push(TEMP t) { 
        this.t = t;     
    }
    
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        MIPSGenerator.getInstance().push(t);
    }
}
    

