package IR;
import MIPS.*; 
import TEMP.*;
import java.util.*;
public class IRcommand_field_access_abort extends IRcommand {
    TEMP obj;
    public IRcommand_field_access_abort(TEMP obj) {
        this.obj = obj;
    }
    public void MIPSme() {
        if (obj==null) {
            MIPSGenerator.getInstance().printErrorComment("invalid_ptr_dref");
            return;
        }
        MIPSGenerator.getInstance().ErrorPointerDerf(obj);
    }
}
