/**
 * NAME: Yuxiao Ran
 * ACCOUNT: cs12fet
 * DATE: 11/12/18
 */

import java.util.ArrayList;
import java.util.List;

/**
 * -----Class Description-----
 * A TritonData class that records proof id and 
 * transactions.
 */
public class TritonData {

    private List<String> transactions;
    private int proofId;

    /**
     * Triton Data Constructor
     * @param None
     */
    public TritonData(){
        this.transactions = new ArrayList<String>();
        this.proofId = 1;
    }

    /**
     * Constructor if specific values are specified
     * @param proofId initial proofId
     * @param transactions initial transactions
     */
    public TritonData(int proofId, List<String> transactions){
        this.transactions = transactions;
        this.proofId = proofId;
    }

    /**
     * Get transactions
     * @return transactions of this TritonData
     */
    public List<String> getTransactions() {
        return this.transactions;
    }

    /**
     * Get proofId
     * @return proofId of this TritonData
     */
    public int getProofId() {
        return this.proofId;
    }

    /**
     * Convert TritonData to String
     * @return the String representation of this TritonData
     */
    public String toString(){
        String toReturn = "";
        toReturn += "DATA Start--------------------\n";
        toReturn += ("Proof of work: " + this.getProofId() + "\n");
        List<String> myTrans = this.getTransactions();
        for(int index = 0; index < myTrans.size(); index++) {
            toReturn += ("Transaction " + index + "\n");
            toReturn += ("Transaction Content: " + myTrans.get(index) + "\n");
        }
        toReturn += "DATA End----------------------\n";
        return toReturn;
    }
}
