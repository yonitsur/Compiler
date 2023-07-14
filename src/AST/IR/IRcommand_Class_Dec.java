package IR;
import MIPS.*;
import TYPES.*;
import java.util.*;
public class IRcommand_Class_Dec  extends IRcommand{
    TYPE_CLASS tc;
    public IRcommand_Class_Dec(TYPE_CLASS tc) {
        this.tc = tc;
    }
    public void MIPSme() { 
        MIPSGenerator.getInstance().data();
        MIPSGenerator.getInstance().label("vt_"+tc.name);
       //HashMap<String, String> t_map = tc.only_son_funcs;
        ArrayList<String> t_methods = tc.methods;
        ArrayList<String> t_methods_classes = tc.methods_classes;
        for (int i = 0; i < t_methods.size(); i++) {
            String func_name = t_methods.get(i);
            String class_name = t_methods_classes.get(i);
            MIPSGenerator.getInstance().vtable_entry(class_name, func_name);
            
        }
        MIPSGenerator.getInstance().text();                
    }
    
}
