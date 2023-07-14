package IR;
import TEMP.*;
import MIPS.*;
import java.util.*;

public class IRcommand_Class_Methods_Dec extends IRcommand {
    public String class_name;
    public HashMap<String,Integer> vtable;
    public IRcommand_Class_Methods_Dec(String class_name, HashMap<String,Integer> vtable) {
        this.class_name = class_name;
        this.vtable = vtable;
    }
    public void MIPSme() {
        MIPSGenerator.getInstance().data();
        MIPSGenerator.getInstance().label(class_name+"_vtable");
        for (String key : vtable.keySet()) {
            MIPSGenerator.getInstance().vtable_entry(class_name, key);
        }
        MIPSGenerator.getInstance().text();
    }
    
}
