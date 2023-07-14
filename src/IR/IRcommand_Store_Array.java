package IR;
import MIPS.*;
import TEMP.*;
public class IRcommand_Store_Array extends IRcommand {

    TEMP tvar;
    TEMP tsub;
    TEMP src;
    
    // this is constructor for IRcommand_Store_Array
    public IRcommand_Store_Array(TEMP tvar, TEMP tsub, TEMP src) {
        this.tvar = tvar;
        this.tsub = tsub;
        this.src = src;
    }

    
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        MIPSGenerator.getInstance().array_store(tvar, tsub, src);
    }
    
}
