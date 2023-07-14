import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;
import java.util.regex.Pattern;

public class InterferenceGraph {

    static int k = 10;

    public ArrayList<Block> blocks;
    public ArrayList<InterferenceGraphNode> nodes;
    public ArrayList<InterferenceGraphNode> all_nodes; // never deleting nodes from this list

    public InterferenceGraph(ArrayList<Block> blocks) {
        this.nodes = new ArrayList<InterferenceGraphNode>();
        this.blocks = blocks;
        this.all_nodes = new ArrayList<InterferenceGraphNode>();
    }
    private boolean nodeExists(int temp_number) {
        for (InterferenceGraphNode node : this.nodes) {
            if (node.temp_number == temp_number) {
                return true;
            }
        }
        return false;
    }

    public InterferenceGraphNode getNode(int temp_number) {
        for (InterferenceGraphNode node : this.nodes) {
            if (node.temp_number == temp_number) {
                return node;
            }
        }
        return null;
    }

    public void removeNode(InterferenceGraphNode node) {
        if (node != null) {
            for (InterferenceGraphNode neighbor : node.neighbors) {
                neighbor.removeNeighbor(node);
            }
            this.nodes.remove(node);
        }
    }

    // add edge between two nodes and check if its move nodes
    public void addEdge(int temp_number1, int temp_number2, Instruction instruction) {
        InterferenceGraphNode node1 = this.getNode(temp_number1);
        InterferenceGraphNode node2 = this.getNode(temp_number2);
        if (node1 != null && node2 != null) {
            node1.addNeighbor(node2, instruction);
            node2.addNeighbor(node1, instruction);
        }
    }

    // create interference graph
    public void build_interference_graph(){
        //create nodes from read_temps and write_temps
        for (Block block : this.blocks) {
            for (Instruction instruction : block.instructions) {
                // for each read_temp
                for (int temp_number : instruction.read_temps) {
                    // if node doesn't exist
                    if (!this.nodeExists(temp_number)) {
                        
                        // create node
                        InterferenceGraphNode node = new InterferenceGraphNode(temp_number);
                        // add node to nodes
                        this.nodes.add(node);
                    }
                }
                // for each write_temp
                for (int temp_number : instruction.write_temps) {
                    // if node doesn't exist
                    if (!this.nodeExists(temp_number)) {
                        
                        // create node
                        InterferenceGraphNode node = new InterferenceGraphNode(temp_number);
                        // add node to nodes
                        this.nodes.add(node);
                    }
                }
            }
        }
    
        // for each block
        for (Block block : this.blocks) {
            // for each instruction
            for (Instruction instruction : block.instructions) {
                // for each live_out (that not null)
                if (instruction.live_out != null) {
                    for (int temp_number : instruction.live_out) {
                      
                        // for each live_out
                        for (int temp_number2 : instruction.live_out) {
                            // if the nodes are not the same
                            if (temp_number != temp_number2) {
                                // add the edge
                                this.addEdge(temp_number, temp_number2, instruction);
                            }
                        }
                    }
                }        
            }
        }
        // copy the nodes to 
        for (InterferenceGraphNode node : this.nodes) {
            this.all_nodes.add(node);
        }
    }

    // True if actually did simplified
    public Boolean simplify(Stack<InterferenceGraphNode> stack){
        // k = 10
        // Recursively remove non-MOV nodes with less than K neighbors, Push removed nodes into stack
        // go over all nodes and remove the ones with less than K neighbors and not MOV
        Boolean simplified = false;
        ArrayList<InterferenceGraphNode> to_remove = new ArrayList<InterferenceGraphNode>();
        for (InterferenceGraphNode node : this.nodes) {
            if (node.getDegree(to_remove) < k && !node.isMoveNode()) { // if not MOV and less than K neighbors
                // Push removed nodes into stack
                stack.push(node);
                // remove node
                to_remove.add(node); // mark node for removal
                simplified = true;
            }
        }
        for (InterferenceGraphNode node : to_remove) { // remove all nodes that were marked for removal
            this.removeNode(node);
        }
        return simplified;
    }
    // for every neighbor t of a, either t interferes with b or t is in in-significant degree
    public Boolean canBeCoalescedByGeorge(InterferenceGraphNode a, InterferenceGraphNode b){
        // go over all t neighbors of a
        for (InterferenceGraphNode t : a.neighbors) {
            // if t is neigbor of b or t has less than K neighbors
            if (t.neighbors.contains(b) || t.getDegree() < k) {
                return true;
            }
        }
        return false;
    }

    // Conservatively merge unconstrained MOV related nodes using Briggs/George heuristics
    // True if actually did coalesce
    public Boolean coalesce(){
        // go over all pairs of nodes
        Boolean coalesced = false;
        ArrayList<InterferenceGraphNode> to_remove = new ArrayList<InterferenceGraphNode>();
        for (InterferenceGraphNode node1 : this.nodes) {
            for (InterferenceGraphNode node2 : this.nodes) {
                // if they are not the same node
                if (!node1.equals(node2)) {   
                    // if they have only move edges (no non-move edges)
                    if (!node1.nonMove_Neighbors.contains(node2) && node1.move_Neighbors.contains(node2)) {
                        // if they can be coalesced by George
                        if (canBeCoalescedByGeorge(node1, node2)) {
                            // merge nodes
                            node1.merge(node2); // merge node2 into node1
                            // remove node2
                            to_remove.add(node2); // mark node for removal
                            coalesced = true;

                        }
                    }
                }
            }
        }
        for (InterferenceGraphNode node : to_remove) { // remove all nodes that were marked for removal
            this.removeNode(node);
        }
        return coalesced;
    }


    //TODO: changelike in simplify with to_remove list
    // Spill some nodes and remove nodes Push removed nodes into stack
    // True if actually did potentialSpill
    public Boolean potentialSpill(Stack<InterferenceGraphNode> stack){
        if (this.nodes.size() > k) {
            ArrayList<InterferenceGraphNode> to_remove = new ArrayList<InterferenceGraphNode>();
            // go over all nodes
            for (InterferenceGraphNode node : this.nodes) {
                // if node has more than K neighbors
                if (node.getDegree(to_remove) >= k) {
                    // Push removed nodes into stack
                    stack.push(node);
                    node.isSpilled = true; // mark node as spilled
                    // remove node
                    to_remove.add(node);
                    return true;
                }
            }
            for (InterferenceGraphNode node : to_remove) { // remove all nodes that were marked for removal
                this.removeNode(node);
            }
        }
        return false;
    }

    // Assign actual registers (from simplify/spill stack) , True iff successful
    public void select(Stack<InterferenceGraphNode> stack){
        InterferenceGraphNode node;
        // go over all nodes in stack
        while (!stack.empty()) {
            node = stack.pop();
            // go over all registers
            for (int i = 0; i < k; i++) {
                // if register is not used by any neighbor
                if (!node.isRegisterUsed(i)) {
                    // assign register to node
                    node.register = i;
                    // for every merged_in node of node - assign register to it
                    for (InterferenceGraphNode merged_in_node : node.merged_in_temps) {
                        merged_in_node.register = i;
                    }
                    break;
                }
            }
        }
    }

    // All we care for register allocation is the live_out of each instruction
    public void performRegisterAllocation(){
        // 1. create interference graph
        build_interference_graph();
        Stack<InterferenceGraphNode> stack = new Stack<InterferenceGraphNode>();
      
        // do simplify
        // do coalesce
        // if can do simplify do it
        // else do potential spill
        // if can do simplify do it
        // else do select

        while (simplify(stack) || coalesce() || potentialSpill(stack)) {
            // do nothing
        }
        select(stack);
    }

    private void sort_nodes_by_temp_number(){
        // sort nodes by temp_number
        Collections.sort(this.all_nodes, new Comparator<InterferenceGraphNode>() {
            @Override
            public int compare(InterferenceGraphNode node1, InterferenceGraphNode node2) {
                return node2.temp_number - node1.temp_number;
            }
        });
    }

    // input: list of lines of mips code with temps
    // inplace change list to: list of lines of mips code with registers
    public ArrayList<String> performCodeGeneration(ArrayList<String> lines){
        sort_nodes_by_temp_number();
        // for each line in lines if appears temp_i replace with according register by rellevant node
        ArrayList<String> new_lines = new ArrayList<String>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            // for each node in all_nodes
            for (int j = 0; j < this.all_nodes.size(); j++) {
                InterferenceGraphNode node = this.all_nodes.get(j);
                // if line contains temp_i
                String str = "Temp_" + node.temp_number;
                String replacement = "\\$t" + node.register;
                
                if (line.contains("Temp_" + node.temp_number)) {
                    // replace with according register
                    line = line.replaceAll(str, replacement);
                }
            }
            new_lines.add(line);
        }
        return new_lines;
    }

    public void writeCodeToFile(String filename, ArrayList<String> lines){
        try {
            FileWriter myWriter = new FileWriter(filename);
            for (String line : lines) {
                myWriter.write(line + "\r\n");
            }
            myWriter.close();


          } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }


}
