import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Block{ 
    public ArrayList <Instruction> instructions;
    public int leader;
    public ArrayList <Block> neighbors; // me block points to all my neighbors
    public Set <Integer> live_in;

    public Block(int leader) {
        this.leader = leader;
        this.instructions = new ArrayList<Instruction>();
        this.neighbors = new ArrayList<Block>();
    }

}