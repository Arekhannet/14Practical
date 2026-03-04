public class openHash {
    private String[] keys;
    private String[] values;
    private int m;
    private int count;
    private static final int PROBE_PRIME = 7;

    public openHash(int m) {
         this.m = m;
        keys   = new String[m + 1]; // indices [1..m], d[0] unused
        values = new String[m + 1];
        count  = 0;
    }

    // Polynomial rolling hash
    public int hash(String key) {
      long h = 0;
        for (int i = 0; i < key.length(); i++)
            h = (h * 31 + key.charAt(i)) % m;
        return (int)(h % m) + 1;
    }

    // Insert key-value pair... update value if key already present
    public void insert(String key, String value) {
        if (isFull()) throw new RuntimeException("Hash table is full");
        int i = hash(key);
        int r = 1;
        while (keys[i] != null && !keys[i].equals(key)) {
            i = (i - 1 + r * PROBE_PRIME) % m + 1;
            r++;
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

