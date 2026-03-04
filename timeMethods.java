// Adapted from prac 13 timeMethods.java
import java.lang.Math.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class timeMethods {

     // Node class (carried from prac 13, keys now String for hashing)
     static class Node {
        String key;
        String value;
        Node(String key, String value) {
            this.key   = key;
            this.value = value;
        }
    }

    // Configuration
     static final int N           = 1 << 20;   // 2^20 = 1,048,576
    static final int REPETITIONS = 30;         // must be >= 30
    static final int LOOKUP_COUNT = 10_000;    // lookups per timed run

    static final int[]    N_VALUES = {750_000, 800_000, 850_000, 900_000, 950_000};
    static final double[] ALPHAS   = {   0.75,    0.80,    0.85,    0.90,    0.95};
    static final String[] EXPECTED = {    "4",     "5",   "6 2/3",  "10",   "20"};

    // Shared data array — generated once, reused by all experiments
    static Node[] data = new Node[N + 1]; // data[1..N]

    // Step 2: generate N shuffled key-value pairs
    //   key   = String of shuffled integer
     static void generateData() {
        int[] keys = new int[N + 1];
        for (int i = 1; i <= N; i++) keys[i] = i;

        // Fisher-Yates shuffle
        Random rng = new Random(42);
        for (int i = N; i > 1; i--) {
            int j = rng.nextInt(i) + 1;
            int tmp = keys[i]; keys[i] = keys[j]; keys[j] = tmp;
        }

        for (int i = 1; i <= N; i++)
            data[i] = new Node(Integer.toString(keys[i]), Integer.toString(i));
    }

     // Find next prime >= n  (table size m should be prime)
    static int nextPrime(int n) {
        if (n < 2) return 2;
        int c = (n % 2 == 0) ? n + 1 : n;
        while (!isPrime(c)) c += 2;
        return c;
    }
    static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; (long) i * i <= n; i += 2)
            if (n % i == 0) return false;
        return true;
    }

      // Main
     public static void main(String args[]) {

        // Formatters carried from prac 13
        DecimalFormat twoD  = new DecimalFormat("0.00");
        DecimalFormat fourD = new DecimalFormat("0.0000");
        DecimalFormat fiveD = new DecimalFormat("0.00000");

        System.out.printf("Generating %,d shuffled key-value pairs...%n", N);
        generateData();
        System.out.println("Done.\n");

        double[] openTimes    = new double[ALPHAS.length];
        double[] chainedTimes = new double[ALPHAS.length];

        Random rng = new Random(99);

        for (int k = 0; k < ALPHAS.length; k++) {
            int n = N_VALUES[k];
            int m = nextPrime(n + 1); // m > n so open table never fills

            System.out.printf("=== alpha=%.2f  n=%,d  m=%,d  1/(1-alpha)=%s ===%n",
                    ALPHAS[k], n, m, EXPECTED[k]);

            // Random lookup keys drawn from the n inserted entries
            String[] lookupKeys = new String[LOOKUP_COUNT];
            for (int i = 0; i < LOOKUP_COUNT; i++)
                lookupKeys[i] = data[rng.nextInt(n) + 1].key;
 openHash oht = new openHash(m);
            for (int i = 1; i <= n; i++)
                oht.insert(data[i].key, data[i].value);

            double runTime = 0, runTime2 = 0, time;
            for (int rep = 0; rep < REPETITIONS; rep++) {
                long start  = System.currentTimeMillis();
                for (String key : lookupKeys) oht.lookup(key);
                long finish = System.currentTimeMillis();
                time = (double)(finish - start);
                runTime  += time;
                runTime2 += (time * time);
            }
            double aveOpen  = runTime / REPETITIONS;
            double stdOpen  = Math.sqrt(runTime2 - REPETITIONS * aveOpen * aveOpen) / (REPETITIONS - 1);
            openTimes[k]    = aveOpen / 1000.0;

            System.out.println("Open hash:");
            System.out.println("  Average time = " + fiveD.format(aveOpen / 1000) + "s."
                    + " \u00B1 " + fourD.format(stdOpen) + "ms.");
            System.out.println("  Std dev      = " + fourD.format(stdOpen) + "ms.");
            System.out.println("  Repetitions  = " + REPETITIONS);

            chainedHash cht = new chainedHash(m);
            for (int i = 1; i <= n; i++)
                cht.insert(data[i].key, data[i].value);

            runTime = 0; runTime2 = 0;
            for (int rep = 0; rep < REPETITIONS; rep++) {
                long start  = System.currentTimeMillis();
                for (String key : lookupKeys) cht.lookup(key);
                long finish = System.currentTimeMillis();
                time = (double)(finish - start);
                runTime  += time;
                runTime2 += (time * time);
            }
            double aveChained = runTime / REPETITIONS;
            double stdChained = Math.sqrt(runTime2 - REPETITIONS * aveChained * aveChained) / (REPETITIONS - 1);
            chainedTimes[k]   = aveChained / 1000.0;

            System.out.println("Chained hash:");
            System.out.println("  Average time = " + fiveD.format(aveChained / 1000) + "s."
                    + " \u00B1 " + fourD.format(stdChained) + "ms.");
            System.out.println("  Std dev      = " + fourD.format(stdChained) + "ms.");
            System.out.println("  Repetitions  = " + REPETITIONS);
            System.out.println();
        }

        // Final summary table (matches spec layout)
        System.out.println("\n\f");
        System.out.println("Average time in seconds");
        System.out.printf("%-20s %-8s %-10s %-12s %-16s %-16s%n",
                "Method", "Alpha", "N", "1/(1-\u03B1)", "Open hash (s)", "Chained hash (s)");
        for (int k = 0; k < ALPHAS.length; k++) {
            System.out.printf("%-20s %-8.2f %-10s %-12s %-16s %-16s%n",
                    "Hash",
                    ALPHAS[k],
                    (N_VALUES[k] / 1000) + "K",
                    EXPECTED[k],
                    fiveD.format(openTimes[k]),
                    fiveD.format(chainedTimes[k]));
        }
         System.out.println("Repetitions = " + REPETITIONS
                + "    Lookups per run = " + LOOKUP_COUNT);
        System.out.println();
    }
}
