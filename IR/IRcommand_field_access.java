package IR;
import TEMP.*;
import MIPS.*;

public class IRcommand_field_access  extends IRcommand{

    
    public int offset;
    public TEMP dst;
    public TEMP varAddress;
    public String class_name;

    public IRcommand_field_access(TEMP dst, TEMP varAddress, String class_name, int offset){
        this.dst = dst;
        this.varAddress = varAddress;
        this.offset = offset;
        this.class_name = class_name;


    }

    public void MIPSme()
    {
        MIPSGenerator.getInstance().field_access(dst, offset, varAddress);
    }
    
}
