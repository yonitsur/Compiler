package IR;
import MIPS.*;
public class IRcommand_jal extends IRcommand{
    String label;
    public IRcommand_jal(String label) {
        this.label = label;
    }
    
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        MIPSGenerator.getInstance().jal(label);
    }
    
}
