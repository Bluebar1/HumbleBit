package com.nickblaauboer;

import com.nickblaauboer.ALU.ALU_test;
import com.nickblaauboer.Assembler.assembler_test;
import com.nickblaauboer.bit.bit_test;
import com.nickblaauboer.longword.longword_test;
import com.nickblaauboer.memory.cpu_test3;
import com.nickblaauboer.memory.memory_test;
import com.nickblaauboer.multiplier.multiplier_test;
import com.nickblaauboer.rippleAdder.rippleAdder_test;

public class Main {
    public static void main(String[] args) {
        bit_test.runTests();
        longword_test.runTests();
        rippleAdder_test.runTests();
        multiplier_test.runTests();
        ALU_test.runTests();
        memory_test.runTests();
        //cpu_test.runTests();
        cpu_test3.runTests();
        //assembler_test.runTests(); // cpu now tested in assembler_test

    }
}

