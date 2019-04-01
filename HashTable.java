/**
 * NAME: Yuxiao Ran
 * DATE: 11/12/18
 */

import java.util.*;
import java.lang.*;
import java.io.*;

/**
 * -----Class Description-----
 * A HashTable class maps String keys to hash values 
 * with my own hash function. Hash table is resized if 
 * load factor exceeds 2/3 after an insertion.
 */
public class HashTable implements IHashTable {
    
    //You will need a HashTable of LinkedLists. 
    
    private int nelems;  //Number of element stored in the hash table
    private int expand;  //Number of times that the table has been expanded
    private int collision;  //Number of collisions since last expansion
    private String statsFileName;     //FilePath for the file to write statistics upon every rehash
    private boolean printStats = false;   //Boolean to decide whether to write statistics to file or not after rehashing
    
    //You are allowed to add more :)
    private ArrayList<LinkedList<String>> hashes;
    private double loadFact; // load factor
    private int longest; // number of elements in the longest chain
    
    /**
     * Constructor for hash table
     * @param Initial size of the hash table
     */
    public HashTable(int size) {
        
        //Initialize
        this.hashes = new ArrayList<LinkedList<String>>(size);
        for(int i = 0; i < size; i++) {
            this.hashes.add(i, new LinkedList<String>());
        }
    }
    
    /**
     * Constructor for hash table
     * @param Initial size of the hash table
     * @param File path to write statistics
     */
    public HashTable(int size, String fileName){
        
        //Set printStats to true and statsFileName to fileName
        this.hashes = new ArrayList<LinkedList<String>>(size);
        for(int i = 0; i < size; i++) {
            this.hashes.add(i, new LinkedList<String>());
        }
        this.printStats = true;
        this.statsFileName = fileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean insert(String value) throws NullPointerException{

        if(value == null) {
            throw new NullPointerException("A null value is passed in.");
        }
        if(this.contains(value)) {
            return false;
        }
        int hashVal = this.myHash(value);
        if(this.hashes.get(hashVal).isEmpty()) {
            this.hashes.get(hashVal).add(value);
        } else {
            this.hashes.get(hashVal).add(value);
            // increase collision
            this.collision++;
        }
        this.nelems++;
        // update longest
        if(this.hashes.get(hashVal).size() > this.longest) {
            this.longest = this.hashes.get(hashVal).size();
        }
        // expand and rehash
        final double THRESHOLD = 2.0 / 3.0;
        if((double)(this.nelems) / this.hashes.size() > THRESHOLD) {
            this.loadFact = (double)(this.nelems) / this.hashes.size();
            try {
                this.collision = 0;
                this.rehash();
            } catch(IOException e) {
                System.out.println("IOException");
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(String value) throws NullPointerException{
        
        if(value == null) {
            throw new NullPointerException("A null value is passed in.");
        }
        if(!this.contains(value)) {
            return false;
        }
        int hashVal = this.myHash(value);
        this.hashes.get(hashVal).remove(value);
        this.nelems--;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(String value) throws NullPointerException{
        
        if(value == null) {
            throw new NullPointerException("A null value is passed in.");
        }
        int hashVal = this.myHash(value);
        return this.hashes.get(hashVal).contains(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printTable() {
        for(int i = 0; i < this.hashes.size(); i++) {
            Integer index = new Integer(i);
            if(this.hashes.get(i).isEmpty()) {
                System.out.println(index.toString() + ":");
            } else {
                String toPrint = index.toString() + ":";
                LinkedList<String> str = this.hashes.get(i);
                for(int j = 0; j < str.size(); j++) {
                    toPrint += " ";
                    toPrint += str.get(j);
                    toPrint += ",";
                }
                toPrint = toPrint.substring(0, toPrint.length() - 1);
                System.out.println(toPrint);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return this.nelems;
    }
    
    // helper functions:    
    /**
     * Hash a string element to an index.
     * @param elem the string element to store
     * @return its index in the hashtable
     */
    private int myHash(String elem) {
        final int START = 13;
        final int STEP = 31;
        final int[] RAND = {7, 5, 3, 2, 1};
        int hashValue = START;
        for(int i = 0; i < elem.length(); i++) {
            hashValue += STEP * (int)(elem.charAt(i));
            hashValue += RAND[i % RAND.length] * (int)(elem.charAt(i));
        }
        hashValue %= this.hashes.size();
        return hashValue;
    }
    
    /**
     * Expand and rehash the items into the table when load factor goes 
     * over the threshold.
     * @throws IOException
     */
    private void rehash() throws IOException{
        
        final int DOUBLE_SIZE = 2;
        ArrayList<LinkedList<String>> newHashes = 
                new ArrayList<LinkedList<String>>(
                        this.hashes.size() * DOUBLE_SIZE);
        for(int i = 0; i < this.hashes.size() * DOUBLE_SIZE; i++) {
            newHashes.add(i, new LinkedList<String>());
        }
        // reset
        this.nelems = 0;
        ArrayList<LinkedList<String>> temp = this.hashes;
        this.hashes = newHashes;
        int[] len = new int[temp.size()];
        for(int i = 0; i < temp.size(); i++) {
            if(temp.get(i).isEmpty()) {
                len[i] = 0;
            } else {
                len[i] = temp.get(i).size();
            }
            for(int j = 0; j < temp.get(i).size(); j++) {
                this.insert(temp.get(i).get(j));
            }
        }
        int max = 0;
        for(int num: len) {
            if(num > max) {
                max = num;
            }
        }
        this.longest = max;
        this.expand++;
        // print statistics
        if(this.printStats) {
            this.printStatistics();
        }
        // reset
        this.loadFact = 0;
        this.longest = 0;
        
    }
    
    /**
     * Print statistics whenever the hashtable is resized.
     * @throws IOException
     */
    private void printStatistics() throws IOException{
        String toPrint = this.expand + " resizes, load factor " + 
                         this.loadFact + ", " + this.collision + 
                         " collisions, " + this.longest +
                         " longest chain";
        BufferedWriter writer = new BufferedWriter(new FileWriter(
                this.statsFileName, true));
        writer.write(toPrint);
        writer.write("\n");
        writer.close();
    }
    
}
