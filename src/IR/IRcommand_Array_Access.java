package IR;
import MIPS.*;
import TEMP.*;
public class IRcommand_Array_Access extends IRcommand {
    TEMP array;
    TEMP index;
    TEMP dst;
    public IRcommand_Array_Access( TEMP dst, TEMP array, TEMP index) {
        this.array = array;
        this.index = index;
        this.dst = dst;
    }
    
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {

        MIPSGenerator.getInstance().array_access(dst, array, index);
       
    
    }
    
}
