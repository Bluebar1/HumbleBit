package com.nickblaauboer.longword;


public class longword_test {

    private static boolean passed = true;
    private static int[] testValues = new int[]{1, 15, -45, -844, 2033};

    public static void runTests() {
        System.out.print("\nLongword Tests Running ... ");

        for (int i : testValues)
            for (int j : testValues) {
                longword lwI = new longword();
                longword lwJ = new longword();
                lwI.set(i);
                lwJ.set(j);

                if (lwI.and(lwJ).getSigned() != (i & j)) printError("AND");
                if (lwI.or(lwJ).getSigned() != (i | j)) printError("OR");
                if (lwI.xor(lwJ).getSigned() != (i ^ j)) printError("XOR");
                if (lwI.not().getSigned() != (~i)) printError("NOT");
                if (lwI.rightShift(3).getSigned() != (i >> 3)) printError("RIGHT SHIFT");
                if (lwI.leftShift(3).getSigned() != (i << 3)) printError("LEFT SHIFT");
            }

        if (passed) System.out.print("PASSED");
    }

    private static void printError(String operation) {
        System.err.print(operation + " FAILED!");
        passed = false;
    }
}

