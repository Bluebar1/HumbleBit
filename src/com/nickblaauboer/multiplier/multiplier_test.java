package com.nickblaauboer.multiplier;

import com.nickblaauboer.longword.longword;

public class multiplier_test {

    private static boolean passed = true;

    public static void runTests() {
        System.out.print("\nMULTIPLIER Tests Running ... ");

        longword a = new longword();
        longword b = new longword();
        longword c = new longword();
        a.set(-11);
        b.set(23);
        c.set(50);

        if (multiplier.multiply(a,b).getSigned() != (-11*23))
            printError("-11*23 FAILED");

        if (multiplier.multiply(a,c).getSigned() != (-11*50))
            printError("-11*50 FAILED");

        if (multiplier.multiply(b,c).getSigned() != (23*50))
            printError("23*50 FAILED");

        if (multiplier.multiply(a,a).getSigned() != (-11*-11))
            printError("-11*-11 FAILED");

        if (multiplier.multiply(a,a).getSigned() != (-11*-11))
            printError("-11*-11 FAILED");


        if (passed) System.out.print("PASSED");
        else System.out.println("FAILED");
    }

    private static void printError(String message) {
        System.err.println(message);
        passed = false;
    }
}
