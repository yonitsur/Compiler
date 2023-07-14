package IR;
import MIPS.*;
import TEMP.*;
public class IRcommand_load_return_value  extends IRcommand{
    TEMP dst;
   


    public IRcommand_load_return_value(TEMP dst) {
        this.dst = dst;
    }
    
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        MIPSGenerator.getInstance().move(dst, "$v0");
    }
    
}
