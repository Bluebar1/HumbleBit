package com.nickblaauboer.memory;

import com.nickblaauboer.bit.bit;
import com.nickblaauboer.longword.longword;

public class memory {

    private bit[] mem; // system memory

    public memory() {
        this.mem = new bit[8192]; // 1024 bytes
        for (int i = 0; i < 8192; i++)
            mem[i] = new bit(0);
    }

    public longword read(longword address) {
        int startingIndex = address.leftShift(3).getSigned(); // Multiply by 8
        longword retVal = new longword();
        for (int i = 0; i < 32; i++)
            retVal.setBit(i, mem[i+startingIndex]);
        return retVal;
    }

    public void write(longword address, longword value) {
        int startingIndex = address.leftShift(3).getSigned();
        for (int i = 0; i < 32; i++) {
            mem[i + startingIndex] = value.getBit(i);
        }
    }

    public void printMemory() {
        for (bit _bit : mem)
            System.out.print(_bit.getValue() + " ");
    }

    public void printAtIndex(int index) {
        index *= 8;
        for (int i = 0; i < 32; i++)
            System.out.print(mem[i+index]);
    }
}
