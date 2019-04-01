/**
 * NAME: Yuxiao Ran
 * ACCOUNT: cs12fet
 * DATE: 11/12/18
 */

import java.util.*;

/**
 * -----Class Description-----
 * A TritonBlockChain class that constructs a blockchain 
 * with a list of TritonBlock objects.
 */
public class TritonBlockChain {

	private static final String MINE_REWARD = "1";
    /*Blockchain class variable*/
    private List<TritonBlock> blockchain;

    /**
     * Constructor, takes in genesis block data to start the blockchain
     * @param index genesis block index
     * @param timestamp system timestamp when the genesis block is created
     * @param data TritonData stored in this block
     * @param prev_hash hash value of the previous block
     */
    public TritonBlockChain(int index, long timestamp, TritonData data, String prev_hash) {
        this.blockchain = new ArrayList<TritonBlock>();
        TritonBlock gen = new TritonBlock(index, timestamp, data, prev_hash);
        this.blockchain.add(gen);
    }

    /**
     * Makes the next block after the proof of work from mining is finished
     * @param lastBlock the former block
     * @param newData new TritonData
     * @return a new TritonBlock
     */
    public TritonBlock makeNewBlock(TritonBlock lastBlock, TritonData newData) {
        int newIndex = lastBlock.getIndex() + 1;
        long newTimestamp = System.currentTimeMillis();
        String newPrevhash = lastBlock.getSelf_hash();
        TritonBlock newBlock = new TritonBlock(
                newIndex, newTimestamp, newData, newPrevhash);
        return newBlock;
    }

    /**
     * Mines the transaction and creates the block to add to the blockchain
     * @param curTransactions the current transactions
     * @return false if curTransactions is empty, true otherwise
     */
    public boolean beginMine(List<String> curTransactions) {
        if(curTransactions.isEmpty()) {
            return false;
        }
        int pid = this.proofOfWork();
        String str = "Triton coin earned: " + TritonBlockChain.MINE_REWARD;
        curTransactions.add(str);
        TritonData dat = new TritonData(pid, curTransactions);
        int size = this.getBlockchain().size();
        TritonBlock blk = this.makeNewBlock(
                this.getBlockchain().get(size - 1), dat);
        this.getBlockchain().add(blk);
        return true;
    }

    /**
     * Simple proof of work algorithm to prove cpu usage was used to mine block
     * @return proofId of current block
     */
    public int proofOfWork() {
        final int PRIME_NUM = 13;
        int lastProofId = this.getBlockchain().get(
                this.getBlockchain().size() - 1).getData().getProofId();
        lastProofId++;
        int mul = (lastProofId > PRIME_NUM) ? lastProofId : PRIME_NUM;
        while(true) {
            if(mul % lastProofId == 0 && mul % PRIME_NUM == 0) {
                break;
            }
            mul++;
        }
        return mul;
    }

    /**
     * Prints current blockchain
     * @return the String representation of this blockchain
     */
    public String toString() {
        String toReturn = "~~~~~~~~~~~~~~~~~~~~~\n";
        toReturn += "TRITON BLOCKCHAIN\n";
        toReturn += "~~~~~~~~~~~~~~~~~~~~~\n";
        for(TritonBlock block: this.getBlockchain()) {
            toReturn += block.toString();
        }
        return toReturn;
    }

    /**
     * Validates each block in the chain looking for any hash 
     * pointer descrepancies, which can point to a tampering problem
     * @return true if prevBlock's hash matches current 
     *         block's prevHash, false otherwise
     */
    public boolean validateChain() {
        if(this.getBlockchain().size() > 1) {
            for(int i = 1; i < this.getBlockchain().size(); i++) {
                if(!this.getBlockchain().get(i).getPrev_hash().equals(
                        this.getBlockchain().get(i - 1).getSelf_hash())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Get blockchain
     * @return this blockchain
     */
    public List<TritonBlock> getBlockchain() {
        return this.blockchain;
    }
}
