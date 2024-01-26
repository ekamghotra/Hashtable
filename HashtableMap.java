import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

    protected class Pair {

        public KeyType key;
        public ValueType value;

        public Pair(KeyType key, ValueType value) {
            this.key = key;
            this.value = value;
        }
    }

    protected LinkedList<Pair>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public HashtableMap(int capacity) {
        table = new LinkedList[capacity];

	for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList();
        }
    }

    @SuppressWarnings("unchecked")
    public HashtableMap() { // with default capacity = 32
        table = new LinkedList[32];

	for (int i = 0; i < 32; i++) {
            table[i] = new LinkedList();
        }
    }

     /**
     * Adds a new key,value pair/mapping to this collection.
     *
     * @param key the key of the key,value pair
     * @param value the value that key maps to
     * @throws IllegalArgumentException if key already maps to a value
     * @throws NullPointerException if key is null
     */
    public void put(KeyType key, ValueType value) throws IllegalArgumentException {
        if (key == null) {
		throw new NullPointerException();
	}
        if (containsKey(key)) {
		throw new IllegalArgumentException();
	}

        int index = Math.abs(key.hashCode()) % table.length;
        table[index].add(new Pair(key, value));
        size++;

        if (((double)size)/table.length >= 0.75) {
		rehash();
	}
    }

     /**
     * Checks whether a key maps to a value in this collection.
     *
     * @param key the key to check
     * @return true if the key maps to a value, and false is the
     *         key doesn't map to a value
     */
    public boolean containsKey(KeyType key) {
        int index = (Math.abs(key.hashCode()) % table.length);

        for (Pair pair : table[index]) {
            if (pair.key.equals(key)) {
		return true;
	    }
        }

        return false;
    }

    /**
     * Retrieves the specific value that a key maps to.
     *
     * @param key the key to look up
     * @return the value that key maps to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    public ValueType get(KeyType key) throws NoSuchElementException {
        if (!containsKey(key)) {
		throw new NoSuchElementException();
	}

        int index = Math.abs(key.hashCode()) % table.length;
        int indexSearch = 0;

        for (Pair pair : table[index]) {
            if (pair.key.equals(key)) {
		break;
	    }

            indexSearch++;
        }

        return table[index].get(indexSearch).value;
    }

    /**
     * Remove the mapping for a key from this collection.
     *
     * @param key the key whose mapping to remove
     * @return the value that the removed key mapped to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    public ValueType remove(KeyType key) throws NoSuchElementException {
        if (!containsKey(key)) {
		throw new NoSuchElementException();
	}

        int index = Math.abs(key.hashCode()) % table.length;
        int indexSearch = 0;

        for (Pair pair : table[index]) {
            if (pair.key.equals(key)) {
		break;
	    }

            indexSearch++;
        }

        Pair removePair = table[index].remove(indexSearch);
        size--;

        return removePair.value;
    }

    /**
     * Removes all key,value pairs from this collection.
     */
    public void clear() {
        int tableSize = table.length;

        table = new LinkedList[tableSize];

        for (int i = 0; i < tableSize; i++) {
            table[i] = new LinkedList();
        }
    }

    /**
     * Retrieves the number of keys stored in this collection.
     *
     * @return the number of keys stored in this collection
     */
    public int getSize() {
        return size;
    }

    /**
     * Retrieves this collection's capacity.
     *
     * @return the size of the underlying array for this collection
     */
    public int getCapacity() {
        return table.length;
    }

    /**
     * This private helper method dynamically grows hashtable by doubling its capacity and rehashing
     * all pairs whenever its load factor becomes greater than or equal to 75%. We do this by going
     * through each index to determine what indexes are not empty and add it to the new table. We end
     * by setting the new table we made in this method to the original table.
     */
    private void rehash() {
        LinkedList<Pair>[] newTable = new LinkedList[table.length*2];

        for (int i = 0; i < newTable.length; i++) {
		newTable[i] = new LinkedList();
	}

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && !table[i].isEmpty()) {
                for (Pair pair : table[i]) {
                    int index = Math.abs(pair.key.hashCode()) % newTable.length;
                    newTable[index].add(pair);
                }
            }
        }

        table = newTable;
    }





    /**
     * Tests implementation of put by adding a null reference and confirming a NullPointerException is thrown
     */
    @Test
    public void jUnitTest1() {
        HashtableMap<Integer, Integer> hashtableMap1 = new HashtableMap<>();

        try {
            hashtableMap1.put(null, 1);
            Assertions.fail("NullPointerException expected");
        }
	catch (NullPointerException e) {}
    }

    /**
     * Tests implementation of get by confirming the right key value is returned after values are added
     */
    @Test
    public void jUnitTest2() {
        HashtableMap<Integer, Integer> hashtableMap2 = new HashtableMap<>();

        hashtableMap2.put(1, 2);
        hashtableMap2.put(3, 4);
        hashtableMap2.put(5, 6);
	hashtableMap2.put(7, 8);
	hashtableMap2.put(9, 10);

        if (hashtableMap2.get(2) != 4) {
		 Assertions.fail("Incorrect key");
	}
    }

    /**
     * Tests the implementation of remove by confirming the correct value is removed and the hash map updates accordingly
     */
    @Test
    public void jUnitTest3() {
        HashtableMap<Integer, Integer> hashtableMap3 = new HashtableMap<>();

        hashtableMap3.put(1, 2);
        hashtableMap3.put(3, 4);
        hashtableMap3.put(5, 6);
        hashtableMap3.put(7, 8);
        hashtableMap3.put(9, 10);

        if (hashtableMap3.remove(2) != 3) {
		Assertions.fail("remove() failed");
	}

        if (hashtableMap3.getSize() != 4) {
		Assertions.fail("remove() failed");
	}
    }

    /**
     * Tests implementation of getCapacity by confirming the correct capacity of 32 to an untouched Hashtable
     */
    @Test
    public void jUnitTest4() {
        HashtableMap<Integer, Integer> hashtableMap4 = new HashtableMap<>();

        if (hashtableMap4.getCapacity() != 32) {
                Assertions.fail("Incorrect capacity");
        }
    }

    /**
     * Tests implementation of getSize by confirming the right size after adding 5 (key/value)'s
     */
    @Test
    public void jUnitTest5() {
        HashtableMap<Integer, Integer> hashtableMap5 = new HashtableMap<>();

        hashtableMap5.put(1, 2);
        hashtableMap5.put(3, 4);
        hashtableMap5.put(5, 6);
        hashtableMap5.put(7, 8);
        hashtableMap5.put(9, 10);

        if (hashtableMap5.getSize() != 5) {
                Assertions.fail("Incorrect size");
        }
    }

}
