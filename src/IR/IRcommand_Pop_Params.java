package IR;
import MIPS.*;

public class IRcommand_Pop_Params extends IRcommand {
    
    int numParams;
    public IRcommand_Pop_Params(int numParams) {
        this.numParams = numParams;
    }
    
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        
        MIPSGenerator.getInstance().popParams(numParams);
    }
}
