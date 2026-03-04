public class openHash {
    private String[] keys;
    private String[] values;
    private int m;
    private int count;
    private static final int PROBE_PRIME = 7;

    public openHash(int m) {
        
    }

    // Polynomial rolling hash
    public int hash(String key) {
      
    }

    // Insert key-value pair... update value if key already present
    public void insert(String key, String value) {
       
        }
        if (keys[i] == null) count++;
        keys[i]   = key;
        values[i] = value;
    }

    // Returns value associated with key, or null if not in table
    public String lookup(String key) {
       
        }
        return null;
    }

    // Removes key-value pair and returns value, or null if not found
    public String remove(String key) {
      
            }
            i = (i - 1 + r * PROBE_PRIME) % m + 1;
            r++; probes++;
        }
        return null;
    }

    // Reinsert any entries displaced by the deletion
    private void rehashFrom(int start) {
      
        }
    }

    public boolean isInTable(String key) { return lookup(key) != null; }
    public boolean isFull()              { return count >= m; }
    public boolean isEmpty()             { return count == 0; }
    public int     size()                { return count; }
}

