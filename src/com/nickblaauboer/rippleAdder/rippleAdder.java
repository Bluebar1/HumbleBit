package com.nickblaauboer.rippleAdder;

import com.nickblaauboer.bit.bit;
import com.nickblaauboer.longword.longword;

public class rippleAdder {

    public static longword add(longword a, longword b) {

        longword result = new longword(); //initialized as 0
        bit carry = new bit(0);
        int i = 31;

        while (i != 0) {
            // checking for 110 and 111
            if (a.getBit(i).and(b.getBit(i)).getValue() == 1) {
                if (carry.getValue() == 1)
                    result.setBit(i--, new bit(1));
                else
                    result.setBit(i--, new bit(0));

                carry.set(1);
            }
            // checking for 011 and 101
            else if (a.getBit(i).xor(b.getBit(i)).getValue() == 1) {
                if (carry.getValue() == 1)
                    result.setBit(i--, new bit(0));
                else
                    result.setBit(i--, new bit(1));
            }
            // checking for 001 and 000
            else {
                if (carry.getValue() == 1) {
                    result.setBit(i--, new bit(1));
                    carry.set(0);
                } else  // 0 0 0
                    i--;
            }
        }

        if (result.getBit(1).getValue() == 1)
            result.setBit(0, new bit(1)); // sets sign bit

        return result;
    }

    public static longword subtract(longword a, longword b) {
        longword _1 = new longword();
        _1.setBit(31, new bit(1));
        longword d2 = add(b.not(), _1); // 2's Compliment for negation
        return add(a,d2);
    }
}
