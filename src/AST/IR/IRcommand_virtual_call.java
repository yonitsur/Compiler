package IR;
import MIPS.*;
import TEMP.*;


public class IRcommand_virtual_call extends IRcommand {
    public TEMP var_address;
    public String funcName;
    public int offset;
    public String  class_name;
 
    public IRcommand_virtual_call(TEMP var_address,String funcName,String class_name, int offset) {
        this.funcName = funcName;
        this.offset = offset;
        this.class_name = class_name;
        this.var_address = var_address;
    }

    public void MIPSme() {
        MIPSGenerator.getInstance().virtual_call(var_address,funcName,offset,class_name);

    }
    
}
