package IR;
import TEMP.*;
import MIPS.*;
import AST.*;


public class IRcommand_store_field_initialvalue extends IRcommand{

    TEMP dst;
    String field_name;
    AST_EXP val;
    TEMP src;
    int offset;
    String class_name;
    public IRcommand_store_field_initialvalue(TEMP dst, String field_name, AST_EXP val, TEMP src, int offset, String class_name) {
        this.dst = dst;
        this.field_name = field_name;
        this.val = val;
        this.src = src;
        this.offset = offset;
        this.class_name = class_name;
    }
    public void MIPSme() {
        if (val instanceof AST_EXP_INT)
            MIPSGenerator.getInstance().load_int(dst, ((AST_EXP_INT)(val)).value);
        else if (val instanceof AST_EXP_STRING){
            MIPSGenerator.getInstance().load_string(dst, ((AST_EXP_STRING)(val)).value,  class_name+"_"+field_name);
        }
        else if (val instanceof AST_EXP_NIL){
            MIPSGenerator.getInstance().load_nil(dst);
        }
        
        MIPSGenerator.getInstance().sw(dst, offset, src);
            
        
    }
    
}
