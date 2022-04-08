package com.nickblaauboer.multiplier;

import com.nickblaauboer.bit.bit;
import com.nickblaauboer.longword.longword;
import com.nickblaauboer.rippleAdder.rippleAdder;

public class multiplier {

    public static longword multiply(longword a, longword b) { // multiplicand, multiplier

        bit[] register = new bit[64];
        longword rightHalf = new longword();

        // fill left side of register with multiplier
        // and fill right side with zero
        for (int i = 0; i < 64; i++) {
            if (i<32) register[i] = b.getBit(i);
            else register[i] = new bit(0);
        }

        // shift and add 32 times
        for (int j = 0; j < 32; j++) {
            //System.out.println(rightHalf);
            if (register[0].getValue() == 1) { // check first value before shifting

                register = leftShift64bit(register); // execute left shift

                int index = 0; // set the new rightHalf after shifting
                for (int k = 32; k < 64; k++)
                    rightHalf.setBit(index++, register[k]);

                rightHalf = rippleAdder.add(rightHalf, a); // execute addition on rightHalf and multiplicand

                int sumIndex = 0; // set second half of register to rightHalf
                for (int l = 32; l < 64; l++)
                    register[l] = rightHalf.getBit(sumIndex++);

            } else { // only shift for 0
                register = leftShift64bit(register);
                int index = 0; // set the new rightHalf after shifting
                for (int k = 32; k < 64; k++)
                    rightHalf.setBit(index++, register[k]);
            }
        }

        return rightHalf;
    }


    // longword is required to be 32 bit, so the left shift would not work.
    // Alternative solutions:
    //  1. Splitting the register into two longwords
    //  and performing shift on both of them
    //  2. Changing longword constructor and methods to support 64 bit
    private static bit[] leftShift64bit(bit[] bits) {
        bit[] temp = new bit[64];
        System.arraycopy(bits, 1, temp, 0, 63);
        temp[63] = new bit(0);
        return temp;
    }
}
