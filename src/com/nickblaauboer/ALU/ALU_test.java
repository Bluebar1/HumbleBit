package com.nickblaauboer.ALU;

import com.nickblaauboer.bit.bit;
import com.nickblaauboer.longword.longword;


public class ALU_test {

    private static boolean passed = true;
    private static int[] testValues = new int[]{-2, 3, 160, -50, 10034};

    public static void runTests() {
        System.out.print("\nALU Tests Running ... ");
        //System.out.println("Method / Expected / Actual");
        bit _0 = new bit(0);
        bit _1 = new bit(1);

        // iterates through every combination of the test values for each operation
        for (int val1 : testValues) {
            for (int val2 : testValues) {

                longword lw1 = new longword();
                longword lw2 = new longword();
                lw1.set(val1);
                lw2.set(val2);

                int and = ALU.doOP(new bit[]{_1, _0, _0, _0}, lw1, lw2).getSigned();
                if (and == (val1 & val2)) printTest("&", (val1 & val2), and);
                else printError("And Failed!");

                int or = ALU.doOP(new bit[]{_1, _0, _0, _1}, lw1, lw2).getSigned();
                if (or == (val1 | val2)) printTest("|", (val1 | val2), or);
                else printError("Or Failed!");

                int not = ALU.doOP(new bit[]{_1, _0, _1, _1}, lw1, lw2).getSigned();
                if (not == (~val1)) printTest("~", (~val1), not);
                else printError("Not Failed!");

//                int leftShift = ALU.doOP(new bit[]{_1, _1, _0, _0}, lw1, lw2).getSigned();
//                if (leftShift == (val1<<1)) printTest("<<", (val1<<1), leftShift);
//                else printError("Left Shift Failed");

                int rightShift = ALU.doOP(new bit[]{_1, _1, _0, _1}, lw1, lw2).getSigned();
                if (rightShift == (val1>>1)) printTest(">>", (val1>>1), rightShift);
                else printError("Right Shift Failed");

                int add = ALU.doOP(new bit[]{_1, _1, _1, _0}, lw1, lw2).getSigned();
                if (add == (val1 + val2)) printTest("+", (val1 + val2), add);
                else printError("Add failed");

                int subtract = ALU.doOP(new bit[]{_1, _1, _1, _1}, lw1, lw2).getSigned();
                if (subtract == (val1 - val2)) printTest("-", (val1 - val2), subtract);
                else printError("Subtract Failed");

                int multiply = ALU.doOP(new bit[]{_0, _1, _1, _1}, lw1, lw2).getSigned();
                if (multiply == (val1 * val2)) printTest("*", (val1 * val2), multiply);
                else printError("Multiply Failed");
            }
        }

        if (passed) System.out.print("PASSED");
        else System.err.print("FAILED");

    }

    private static void printError(String message) {
        System.err.println(message);
        passed = false;
    }

    private static void printTest(String test, int expected, int actual) {
        //System.out.println(test +" / "+ expected +" / "+ actual);
    }
}
