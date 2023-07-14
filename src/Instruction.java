
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Instruction{
    public ArrayList <Integer> read_temps;
    public ArrayList <Integer> write_temps;
    public String str_instruction;
    public Set <Integer> live_out;

    public int get_temp_number_from_sw_string(String sw_string){ // sw_string= "4(Temp_2)"
        String[] string_arr = sw_string.split("\\(");
        int temp_number = Integer.parseInt(string_arr[1].substring(5, string_arr[1].length()-1));
        return temp_number;

    }

    public Boolean op_is_in_list(String[] opList, String operaString){

        for (String op : opList) {
            if (operaString.equals(op)) {
                return true;
            }
        }
        return false;
    }

    public Boolean is_1_write_2_read(String operaString){ // if operation is 1 write and 2 read
        String[] oplist = {"add", "addi", "addu", "sub", "subi", "subu", "mul", "div"};
        return op_is_in_list(oplist, operaString);
    }

    public Boolean is_1_write_1_read(String operaString){ // if operation is 1 write and 1 read
        String[] oplist = {"move"};
        return op_is_in_list(oplist, operaString);
    }

    public Boolean is_0_write_2_read(String operaString){ // if operation is 0 write and 2 read
        String[] oplist = {"beq", "bne", "ble", "blt", "bge", "bgt", "beqz", "bnez"};
        return op_is_in_list(oplist, operaString);
    }

    public Boolean is_sw(String operaString){ // if operation is sw
        String[] oplist = {"sw","sb"};
        return op_is_in_list(oplist, operaString);
    }
    public Boolean is_lw(String operaString){ // if operation is lw
        String[] oplist = {"li", "lw", "la", "lb"};
        return op_is_in_list(oplist, operaString);
    }

    private int extractTempNumber(String tempString){ // tempString = "Temp_2"
        return Integer.parseInt(tempString.substring(5));
    }

    public Instruction(String instruction) {
        this.str_instruction = instruction;
        this.read_temps = new ArrayList<Integer>();
        this.write_temps = new ArrayList<Integer>();
        String[] string_arr = instruction.strip().split(" "); // split by space
        String opera = string_arr[0];
        if (string_arr.length <= 1) // if it's a label or a jump
            return;
        String[] operands = string_arr[1].split(","); // split by comma


        if (is_1_write_2_read(opera)){
                // update read_temps
            if (operands[1].startsWith("Temp")) // check if it's a temp
                this.read_temps.add(extractTempNumber(operands[1])); // add temp number
            if (operands[2].startsWith("Temp")) // check if it's a temp
                this.read_temps.add(extractTempNumber(operands[2])); // add temp number

            // update write_temps
            if (operands[0].startsWith("Temp")) // check if it's a temp
                this.write_temps.add(extractTempNumber(operands[0])); // add temp number
        }
        // 1 write, 1 read
        if (is_1_write_1_read(opera)){
            // update read_temp
            if (operands[1].startsWith("Temp")) // check if it's a temp
                this.read_temps.add(extractTempNumber(operands[1])); // add temp number
            // update write_temp
            if (operands[0].startsWith("Temp")) // check if it's a temp
                this.write_temps.add(extractTempNumber(operands[0])); // add temp number
        }

            // 0 write, 2 read
        if (is_0_write_2_read(opera)){
             
                // update read_temps
                if (operands[0].startsWith("Temp")) // check if it's a temp
                    this.read_temps.add(extractTempNumber(operands[0])); // add temp number
                if (operands[1].startsWith("Temp")) // check if it's a temp
                    this.read_temps.add(extractTempNumber(operands[1])); // add temp number
        }

            // SW-s
        if (is_sw(opera) || is_lw(opera)){
            // update read_temps
            if (operands[0].startsWith("Temp")){ // check if it's a temp
            int opnd1 = extractTempNumber(operands[0]);
            if (is_sw(opera)) this.read_temps.add(opnd1);
            else this.write_temps.add(opnd1);
            }
            if (operands[1].contains("Temp")){// check if it's a temp 0(Temp_2)
                int temp_number = get_temp_number_from_sw_string(operands[1]);
                this.read_temps.add(temp_number); // add temp number
            }
        }
    }
    
    public void performLivenessAnalysis(Set<Integer> live_in){        
        // update live_out
        this.live_out = new HashSet<Integer>(live_in);
        // remove write temps
        for (int i=0; i<this.write_temps.size(); ++i) {
            this.live_out.remove(this.write_temps.get(i));
        }
        // add read temps
        for (int i=0; i<this.read_temps.size(); ++i) {
            this.live_out.add(this.read_temps.get(i));
        }
    }
}