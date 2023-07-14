package IR;
import MIPS.*; 
import TEMP.*;

public class IRcommand_Load_This extends IRcommand{
    public TEMP dst;
    public IRcommand_Load_This(TEMP dst) {
        this.dst = dst;
    }
    public void MIPSme() {
        MIPSGenerator.getInstance().load_this(dst);
    }

    
}
