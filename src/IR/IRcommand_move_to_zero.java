package IR;
import MIPS.MIPSGenerator;
import TEMP.*;
public class IRcommand_move_to_zero extends IRcommand {
    TEMP t;
    public IRcommand_move_to_zero(TEMP t) {
        this.t = t;
    }
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        // t = 0
        MIPSGenerator.getInstance().move(t, "$zero");
    }
    
}
