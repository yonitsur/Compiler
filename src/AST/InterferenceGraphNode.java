import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

class InterferenceGraphNode{
    public int temp_number;
    public Set<InterferenceGraphNode> neighbors;
    public Set<InterferenceGraphNode> move_Neighbors;
    public Set<InterferenceGraphNode> nonMove_Neighbors;
    public Set<InterferenceGraphNode> merged_in_temps;
    public Boolean isSpilled;
    public int register = -1;

    public InterferenceGraphNode(int temp_number) {
        this.temp_number = temp_number;
        this.neighbors = new HashSet<InterferenceGraphNode>();
        this.move_Neighbors = new HashSet<InterferenceGraphNode>();
        this.nonMove_Neighbors = new HashSet<InterferenceGraphNode>();
        this.merged_in_temps = new HashSet<InterferenceGraphNode>();
        this.isSpilled = false;
    }
    public void addNeighbor(InterferenceGraphNode neighbor, Instruction instruction) {
        this.neighbors.add(neighbor);
        if (instruction != null) // if the instruction is null - add to both move and non move neighbors
        {
            if (instruction.str_instruction.startsWith("move")) // check if MOV node
                this.move_Neighbors.add(neighbor); // add to move neighbors
            else
                this.nonMove_Neighbors.add(neighbor); // add to non move neighbors  
        }
             
    }
    public void removeNeighbor(InterferenceGraphNode neighbor) {
        this.neighbors.remove(neighbor);
        this.move_Neighbors.remove(neighbor);
        this.nonMove_Neighbors.remove(neighbor);
    }

    public void merge(InterferenceGraphNode node) {
        this.merged_in_temps.add(node); // add the merged node to the merged_in_temps set
        for (InterferenceGraphNode neighbor : node.move_Neighbors) { // for each move neighbor of the merged node
            if (neighbor.equals(this)) // if the neighbor is the current node
                continue; // continue to the next neighbor
            this.addNeighbor(neighbor, null);
            this.move_Neighbors.add(neighbor); 
            neighbor.removeNeighbor(node);
            neighbor.addNeighbor(this, null);
            neighbor.move_Neighbors.add(this);
        }
        for (InterferenceGraphNode neighbor : node.nonMove_Neighbors) { // for each non move neighbor of the merged node
            if (neighbor.equals(this)) // if the neighbor is the current node
                continue; // continue to the next neighbor
            this.addNeighbor(neighbor, null);
            this.nonMove_Neighbors.add(neighbor);
            neighbor.removeNeighbor(node);
            neighbor.addNeighbor(this, null);
            neighbor.nonMove_Neighbors.add(this);
        }
    }
    // check if register is used by any neighbor of node
    public Boolean isRegisterUsed(int register) {
        for (InterferenceGraphNode neighbor : this.neighbors) {
            if (neighbor.register == register) {
                return true;
            }
        }
        return false;
        
    }

    public Boolean isMoveNode() {
        return this.move_Neighbors.size() > 0;
    }

    public int getDegree() {
        return this.neighbors.size();
    }

    public int getDegree(ArrayList<InterferenceGraphNode> to_remove) {
        ArrayList<InterferenceGraphNode> neighbors = new ArrayList<InterferenceGraphNode>(this.neighbors);
        neighbors.removeAll(to_remove);
        return neighbors.size();
    }

    public Boolean equals(InterferenceGraphNode node) {
        return this.temp_number == node.temp_number;
    }

}