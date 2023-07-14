package IR;
import MIPS.*;

public class IRcommand_Epilogue extends IRcommand {
    int localsCounter;

    public IRcommand_Epilogue(int localsCounter)
    {
        this.localsCounter = localsCounter;
    }
    
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        int sp_offset = 4*localsCounter;
        MIPSGenerator.getInstance().epilogue(sp_offset);
    }
}
