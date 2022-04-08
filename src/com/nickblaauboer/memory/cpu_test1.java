package com.nickblaauboer.memory;

import com.nickblaauboer.Assembler.Assembler;

public class cpu_test1 {

    private static boolean passed = true;

    // Halt
    private static String[] testBits1 = {"0000000000000000"};

    // Move R2 9, Print registers, halt
    private static String[] testBits2 = {"0001 0010 0000 1001", "0010 0000 0000 0000", "0000 0000 0000 0000"};

    // Move R1 -1, Print registers, halt
    private static String[] testBits3 = {"0001 0001 1111 1111", "0010 0000 0000 0000", "0000 0000 0000 0000"};

    // Move R1 1, Move R2 2, add R1 R2 R3, Print registers, halt
    private static String[] testBits4 = {"0001 0001 0000 0001", "0001 0010 0000 0010", "1110 0001 0010 0011", "0010 0000 0000 0000", "0000 0000 0000 0000"};


    public static void runTests() {
        System.out.print("\nCPU Tests Running ... ");

        computer cpu1 = new computer();
        cpu1.preload(testBits1);
        cpu1.run();

        computer cpu2 = new computer();
        cpu2.preload(testBits2);
        cpu2.run();

        computer cpu3 = new computer();
        cpu3.preload(testBits3);
        cpu3.run();

        computer cpu4 = new computer();
        cpu4.preload(testBits4);
        cpu4.run();

        System.out.println("Passed.");

    }
}
