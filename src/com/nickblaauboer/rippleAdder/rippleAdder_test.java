package com.nickblaauboer.rippleAdder;

import com.nickblaauboer.longword.longword;

public class rippleAdder_test {

    public static void runTests() {

        System.out.print("\nRipple Adder Tests Running ... ");

        boolean passed = true;

        longword test5 = new longword();
        test5.set(5);
        longword test10 = new longword();
        test10.set(10);
        longword test20 = new longword();
        test20.set(20);
        longword testn30 = new longword();
        testn30.set(-30);

        longword test11 = new longword();
        test11.set(11);

        longword test7 = new longword();
        test7.set(7);

        //System.out.println("11+7= "+ rippleAdder.add(test11, test7).getSigned());
        if (rippleAdder.add(test7, test11).getSigned() != 18) passed = false;

        //System.out.println("11+(-30)= "+ rippleAdder.add(test11, testn30).getSigned());
        if (rippleAdder.add(test11, testn30).getSigned() != -19) passed = false;


        //System.out.println("5 + 10 = " + rippleAdder.add(test5, test10).getSigned());
        if (rippleAdder.add(test5, test10).getSigned() != 15) passed = false;

        //System.out.println("5 - 10 = "+ rippleAdder.subtract(test5, test10).getSigned());
        if (rippleAdder.subtract(test5, test10).getSigned() != -5) passed = false;

        //System.out.println("10 - (-30) = "+ rippleAdder.subtract(test10, testn30).getSigned());
        if (rippleAdder.subtract(test10, testn30).getSigned() != 40) passed = false;

        //System.out.println("-30 - 20 = "+ rippleAdder.subtract(testn30, test20).getSigned());
        if (rippleAdder.subtract(testn30, test20).getSigned() != -50) passed = false;


        if (passed)
            System.out.print("PASSED");
        else
            System.err.print("\rFAILED");
    }
}
