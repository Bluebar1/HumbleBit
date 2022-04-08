package com.nickblaauboer.memory;

import com.nickblaauboer.longword.longword;

public class memory_test {

    private static boolean passed = true;
    private static int[] testValues =    new int[]{-1, 4, -10, 18, 59};
    private static int[] testAddresses = new int[]{1, 25, 56, 86, 38};

    public static void runTests() {

        System.out.print("\nMemory Tests Running ... ");

        memory mem = new memory();
        int index = 0;
        for (int i : testValues) {
            longword testValue = new longword();
            testValue.set(i);
            longword testAddress = new longword();
            testAddress.set(testAddresses[index++]);
            mem.write(testAddress, testValue);
        }

        index = 0;
        for (int i : testValues) {
            longword t = new longword();
            t.set(testAddresses[index++]);
            if (mem.read(t).getSigned() != i) {
                System.err.println("MEMORY FAILED");
                passed = false;
            }

        }

        if (passed)
            System.out.print("PASSED");
        else System.err.print("FAILED");


    }


}
