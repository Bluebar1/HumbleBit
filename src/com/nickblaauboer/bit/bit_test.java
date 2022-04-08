package com.nickblaauboer.bit;

import com.nickblaauboer.bit.bit;

public class bit_test {

    // static flag to track if all tests passed, set to false from error printing methods
    private static boolean passed = true;

    // creates new bit(s) for each test, prints error if failed.
    // checks if all unit tests were passed and prints results
    public static void runTests() {
        System.out.print("Basic Unit Tests Running ... ");

        // set(int)
        bit testBit = new bit(0);
        testBit.set(1);
        if (testBit.getValue() != 1) printError("set(int)");

        // toggle()
        testBit = new bit(0);
        testBit.toggle();
        if (testBit.getValue() != 1) printError("toggle()");

        // set()
        testBit = new bit(0);
        testBit.set();
        if (testBit.getValue() != 1) printError("set()");

        // clear()
        testBit = new bit(1);
        testBit.clear();
        if (testBit.getValue() != 0) printError("clear()");

        // and
        if (new bit(0).and(new bit(1)).getValue() != 0)
            printErrorAt(0,1,"and");

        // or
        if (new bit(0).or(new bit(1)).getValue() != 1)
            printErrorAt(0, 1, "or");

        // xor
        if (new bit(0).xor(new bit(1)).getValue() != 1)
            printErrorAt(0,1,"xor");

        // not
        if (new bit(0).not().getValue() != 1)
            System.out.println("not() failed");

//        if (passed) System.out.println("UNIT TESTS ALL PASSED");
//        else System.out.println("Unit Test(s) FAILED!");

        //System.out.println(new bit(0).toString());

        //System.out.println("\nStarting Exhaustive Tests...");
        //System.out.println("Method / Expected / Actual");
        exhaustiveTesting();
        if (passed) System.out.print("PASSED");
    }

    // create 2 bits and test operations and(bit), or(bit), xor(bit), not()
    // tests every possible combination with 2 for loops
    private static void exhaustiveTesting() {

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                bit bit1 = new bit(i);
                bit bit2 = new bit(j);

                // and
                if (bit1.and(bit2).getValue() == (i & j))
                    printTest("and", bit1.and(bit2).getValue(), (i & j));
                else printErrorAt(i, j, "and");

                // or
                if (bit1.or(bit2).getValue() == (i | j))
                    printTest("or", bit1.or(bit2).getValue(), (i | j));
                else printErrorAt(i, j, "or");

                // xor
                if (bit1.xor(bit2).getValue() == (i ^ j))
                    printTest("xor", bit1.xor(bit2).getValue(), (i ^ j));
                else printErrorAt(i, j, "xor");

                // not
                if (bit1.not().getValue() == ((i == 0) ? 1 : 0))
                    printTest("not()", bit1.not().getValue(), (i == 0) ? 1 : 0);
                else printErrorAt(i, j, "not");
            }
        }
    }


    // print-out of test
    private static void printTest(String test, int expected, int actual) {
        //System.out.println(test +" / "+ expected +" / "+ actual);
    }

    private static void printErrorAt(int i, int j, String operation) {
        System.out.println("ERROR: "+ i +" "+ operation +" "+ j);
        passed = false;
    }

    private static void printError(String operator) {
        System.out.println("ERROR: "+ operator +" test FAILED!");
        passed = false;
    }
}


