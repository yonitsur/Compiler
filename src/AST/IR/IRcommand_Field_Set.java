package IR;
import MIPS.*;
import TEMP.*;

public class IRcommand_Field_Set extends IRcommand{

    public TEMP var_address;
    public TEMP src;
    public int offset;

    
    public IRcommand_Field_Set(TEMP var_address, TEMP src, int offset)
    {
        this.var_address = var_address;
        this.src = src;
        this.offset = offset;
   
    }
     
    public void MIPSme()
    {
        MIPSGenerator.getInstance().field_set(var_address, offset, src);
    }
    
}
