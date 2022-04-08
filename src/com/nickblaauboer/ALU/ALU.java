package com.nickblaauboer.ALU;

import com.nickblaauboer.bit.bit;
import com.nickblaauboer.longword.longword;
import com.nickblaauboer.multiplier.multiplier;
import com.nickblaauboer.rippleAdder.rippleAdder;

public class ALU {

    public static longword doOP(bit[] operation, longword a, longword b) {

        StringBuilder strRet = new StringBuilder();
        for (bit _bit : operation)
            strRet.append(_bit.getValue());
        String str = strRet.toString();

        switch (str) {
            case "1000":
                return a.and(b);
            case "1001":
                return a.or(b);
            case "1010":
                return a.xor(b);
            case "1011":
                return a.not();
            case "1100":
                return new longword(); //a.leftShift(1);
            case "1101":
                return a.rightShift(1);
            case "1110":
                return rippleAdder.add(a,b);
            case "1111":
                return rippleAdder.subtract(a,b);
            case "0111":
                return multiplier.multiply(a,b);
        }
        return null;
    }
}
