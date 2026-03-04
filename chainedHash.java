public class chainedHash {

    // Linked-list node for each chain
    private static class Node {
        String key;
        String value;
        Node   next;
        Node(String key, String value) {
            this.key = key; this.value = value; this.next = null;
        }
    }
 private Node[] table; 
    private int    m;
    private int    count;

    public chainedHash(int m) {
        this.m = m;
        table  = new Node[m + 1];
        count  = 0;
    }

    // Identical hash function to openHash -> index in [1..m]
    public int hash(String key) {
        long h = 0;
        for (int i = 0; i < key.length(); i++)
            h = (h * 31 + key.charAt(i)) % m;
        return (int)(h % m) + 1;
    }

    // Insert key-value pair; update value if key already in chain
    public void insert(String key, String value) {
        int i = hash(key);
        // Search chain for existing key
        for (Node curr = table[i]; curr != null; curr = curr.next) {
            if (curr.key.equals(key)) { curr.value = value; return; }
        }
        // Prepend new node to chain head (O(1))
        Node node = new Node(key, value);
        node.next = table[i];
        table[i]  = node;
        count++;
    }

    // Returns value for key, or null if not found
    public String lookup(String key) {
        int i = hash(key);
        for (Node curr = table[i]; curr != null; curr = curr.next)
            if (curr.key.equals(key)) return curr.value;
        return null;
    }

    // Removes key-value pair and returns value, or null if not found
    public String remove(String key) {
        int i = hash(key);
        Node curr = table[i], prev = null;
        while (curr != null) {
            if (curr.key.equals(key)) {
                if (prev == null) table[i] = curr.next;
                else              prev.next = curr.next;
                count--;
                return curr.value;
            }
            prev = curr; curr = curr.next;
        }
        return null;
    }

    public boolean isInTable(String key) { return lookup(key) != null; }
    public boolean isEmpty()             { return count == 0; }
    public int     size()                { return count; }
  
}

