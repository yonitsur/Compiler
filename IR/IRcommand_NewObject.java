package IR;
import TEMP.*;
import MIPS.*;
import TYPES.*;
public class IRcommand_NewObject extends IRcommand {
    TEMP t;
    String name;
    int size;

    public IRcommand_NewObject(TEMP t, String name, int size) {
        this.t = t;
        this.name = name;
        this.size = size;

    }
    public void MIPSme() {
        // get the size of the object
       
        // allocate memory for the object
        MIPSGenerator.getInstance().new_object(t,name, size);
       
    }
   
    
}
