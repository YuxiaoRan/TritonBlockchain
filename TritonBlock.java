/**
 * NAME: Yuxiao Ran
 * ACCOUNT: cs12fet
 * DATE: 11/12/18
 */

/**
 * -----Class Description-----
 * A TritonBlock class constructs blocks that store 
 * TritonData. It implements a hash function that maps 
 * each block to a String hash value.
 */
public class TritonBlock {
	/*Class variables, all the attributes of the block*/
    private int index;
    private long timestamp;
    private TritonData data;
    private String prev_hash;
    private String self_hash;

    /**
     * Constructor, builds a block with passed in variables, 
     * then creates a hash for curr block
     * @param index block index
     * @param timestamp system timestamp when the block is created
     * @param data TritonData stored in this block
     * @param prev_hash hash value of the previous block
     */
    public TritonBlock(int index, long timestamp, TritonData data, String prev_hash){
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.prev_hash = prev_hash;
        this.self_hash = this.hashBlock();
    }

    /**
     * Hash this block
     * @return hashing of this block
     */
    private String hashBlock(){
        // define constants used
        final long A_PRIME = 17;
        final long TIME = this.getTimestamp();
        final int PID = this.getData().getProofId();
        final int[] RAND = {7, -5, 3, -2, 1};
        final int LEN = 5;
        final int IND = this.getIndex();
        long var = TIME;
        // my hashing algorithm
        while(var > (long)Integer.MAX_VALUE || var < (long)Integer.MIN_VALUE) {
            var /= A_PRIME;
        }
        int new_var = (int)var;
        int hashValue = (new_var * RAND[IND % LEN]) / (PID % (int)A_PRIME);
        Integer hashInt = new Integer(hashValue);
        return hashInt.toString();
    }

    /**
     * Get block index
     * @return the index of this block
     */
    public int getIndex(){
        return this.index;
    }

    /**
     * Get timestamp
     * @return timestamp of this block
     */
    public long getTimestamp(){
        return this.timestamp;
    }

    /**
     * Get data block
     * @return TritonData stored in this block
     */
    public TritonData getData(){
        return this.data;
    }

    /**
     * Get previous hash
     * @return hashing of the previous block
     */
    public String getPrev_hash(){
        return this.prev_hash;
    }

    /**
     * Get current hash
     * @return hashing of this block
     */
    public String getSelf_hash(){
        return this.self_hash;
    }

    /**
     * Convert the block to String form
     * @return the String representation of this block
     */
    public String toString(){
        String toReturn = "\n";
        toReturn += ("TritonBlock " + this.getIndex() + "\n");
        toReturn += ("Index: " + this.getIndex() + "\n");
        toReturn += ("Timestamp: " + this.getTimestamp() + "\n");
        toReturn += ("Prev Hash: " + this.getPrev_hash() + "\n");
        toReturn += ("Hash: " + this.getSelf_hash() + "\n");
        toReturn += this.getData().toString();
        return toReturn;
    }
}
