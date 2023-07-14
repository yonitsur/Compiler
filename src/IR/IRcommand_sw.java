package IR;
import MIPS.*;
import TEMP.*;

public class IRcommand_sw extends IRcommand {

    TEMP src;
    TEMP dst;
    int offset;
    public IRcommand_sw(TEMP src, TEMP dst, int offset) {
        this.src = src;
        this.dst = dst;
        this.offset = offset;
    }
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        MIPSGenerator.getInstance().sw(src, offset, dst);
    }
}
