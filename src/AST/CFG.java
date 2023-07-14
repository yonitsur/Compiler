import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CFG {

    // graph fields:
    // leaders - array of leaders indexes
    public ArrayList <Integer> leaders;
    public ArrayList <Block> blocks; // this is the CFG
     
    public CFG() {
        this.leaders = new ArrayList<Integer>();
    }

    public int convert_label_to_line_index(ArrayList <String> lines, String label) {
        for (int i=0; i<lines.size(); ++i) {
            if (lines.get(i).startsWith(label)) {
                return i;
            }
        }
        return -1;

    }

    public void set_leaders_list(ArrayList <Integer> leaders, ArrayList <String> lines){
        String line;
        String next_line;
        for (int i=0; i<lines.size(); i++) {
            line = lines.get(i).strip();
            // start:
            // create leaders list: check if it's a jump or label
            if (is_label(line)) { // label
                this.leaders.add(i);
            }
            else if(i+1 < lines.size() && is_jump_or_brnch(line)){ // jump(/brunch)
                // if next line is not label - add leader
                next_line = lines.get(i+1).strip();
                if (!is_label(next_line)) {
                    this.leaders.add(i+1);
                }
            }
            
        }
    }


    public boolean is_jump_or_brnch(String line) {
        if ((line.startsWith("j ") || line.startsWith("jal")) || line.startsWith("beq")
                    || line.startsWith("bne") || line.startsWith("bgez") || line.startsWith("bgtz")
                    || line.startsWith("blez") || line.startsWith("bltz")
                ) {
                    return true;
                }
        return false;
    }
    public boolean is_jump_or_brnch_with_label(String line) {
        if ((line.startsWith("j ") || (line.startsWith("jal")) && !line.startsWith("jalr")) || line.startsWith("beq")
                    || line.startsWith("bne") || line.startsWith("bgez") || line.startsWith("bgtz")
                    || line.startsWith("blez") || line.startsWith("bltz")
                ) {
                    return true;
                }
        return false;
    }

    public boolean is_label(String line) {
        if (line.contains(":")) {
            return true;
        }
        return false;
    }

    public boolean is_jump_or_branch_or_label(String line) {
        if (is_jump_or_brnch(line) || is_label(line)) {
            return true;
        }
        return false;
    }


    public void init_block_list(ArrayList<Block> blocks, ArrayList <Integer> leaders, ArrayList <String> lines){
        for (int i=0; i<leaders.size(); ++i) { // 
            this.blocks.add(new Block(leaders.get(i))); // add new block with leader
        }

        // loop over blocks and add instructions of each block: 
        for (int i=0; i<this.blocks.size(); ++i) {
            if (i == this.blocks.size()-1) { // last block
                for (int j=this.blocks.get(i).leader; j<lines.size(); ++j) {
                    this.blocks.get(i).instructions.add(new Instruction(lines.get(j))); // add instructions from here to the next leader
                }
            }
            else{ // not last block
                for (int j=this.blocks.get(i).leader; j<this.blocks.get(i+1).leader; ++j) {
                    this.blocks.get(i).instructions.add(new Instruction(lines.get(j))); // add instructions from here to the next leader
                }
            }
        } 


        // set neighbors for each block:
        for (int i=0; i<this.blocks.size()-1; ++i) {
            this.blocks.get(i+1).neighbors.add(this.blocks.get(i)); // prev block <- curr block
        }
        this.blocks.get(0).neighbors.add(this.blocks.get(this.blocks.size()-1)); // first block -> last block
        for (int i=0; i<this.blocks.size(); ++i) { // caller <- label called
            Block curr_block = this.blocks.get(i);
            int index_of_last_instruction_in_block = curr_block.instructions.size()-1;
            String last_instruction_in_block = curr_block.instructions.get(index_of_last_instruction_in_block).str_instruction.strip();
              if (is_jump_or_brnch_with_label(last_instruction_in_block)) {
                String[] string_arr = last_instruction_in_block.split(" ");
                int string_arr_last_index = string_arr.length-1;
                string_arr = string_arr[string_arr_last_index].split(",");
                string_arr_last_index = string_arr.length-1;
                String jump_label = string_arr[string_arr_last_index];
                int jump_line_index = convert_label_to_line_index(lines, jump_label);
                
                int block_index = this.leaders.indexOf(jump_line_index);
                Block b = this.blocks.get(block_index);
                
                b.neighbors.add(this.blocks.get(i));
              }
        }


    }
    // return lines of input(with temps) file
    public ArrayList <String> createInterferenceGraph(String mips_code_file) throws IOException{
        // 1. read the mips code file
        // 2. create control flow graph
        // 2.1 create basic blocks
        // 2.2 create edges between basic blocks
        // 2.3 create edges between instructions
        // 3. save the graph in a field of this class 

        // start read the mips code file
        ArrayList <String> lines = new ArrayList<String>();
        FileReader fr = new FileReader(mips_code_file);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while (line != null) {
            lines.add(line);
            line = br.readLine();
        }
        br.close();
        this.blocks = new ArrayList<Block>();


        // create leaders list:
        set_leaders_list(this.leaders, lines);
        
        // create blocks list & set neighbors:
        init_block_list(this.blocks, this.leaders, lines);

        // now we have the control flow graph

        return lines;
    }

    public void performLivenessAnalysis(){
        //
        int last = this.blocks.size()-1; // last block index
        performLivenessAnalysis(this.blocks.get(last), new HashSet<Integer>());
    }

    public void performLivenessAnalysis(Block curr_block, Set<Integer> live_in){
        // 1. for each instruction in the block, perform liveness analysis
        // 2. update live_in and live_out
        // 3. update the graph
        // 4. call perform_liveness_analysis on all the neighbors of the block
        // 5. return
        // 1. for each instruction in the block, perform liveness analysis
        if (curr_block.live_in != null && curr_block.live_in.equals(live_in)) return; // not first time
        if (curr_block.live_in == null) { // first time
            curr_block.live_in = new HashSet<Integer>(); 
        }
        curr_block.live_in.addAll(live_in);
        
        Set<Integer> instr_live_in = new HashSet<Integer>(curr_block.live_in);
        Instruction curr_instruction = null;
        for (int i=curr_block.instructions.size()-1; i>=0; --i) { // loop over instructions in reverse order
            curr_instruction = curr_block.instructions.get(i); // current instruction
            curr_instruction.performLivenessAnalysis(instr_live_in); // perform liveness analysis
            // 2. update live_in and live_out
            instr_live_in = curr_instruction.live_out;
        }
        Set<Integer> curr_block_live_out = new HashSet<Integer>(curr_instruction.live_out);
        // 3. update the graph
        // 4. call perform_liveness_analysis on all the neighbors of the block
        for (int i=0; i<curr_block.neighbors.size(); ++i) {
            Set <Integer> live_in_copy = new HashSet<Integer>(curr_block_live_out);
            performLivenessAnalysis(curr_block.neighbors.get(i), live_in_copy);
        }
        // 5. return
    }
}
