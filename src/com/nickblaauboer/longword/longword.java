package com.nickblaauboer.longword;

import com.nickblaauboer.bit.bit;

public class longword implements ILongword {

    bit[] bits;

    public longword() {
        bits = new bit[32]; // fills values with 0 by default
        for (int i = 0; i < 32; i++)
            bits[i] = new bit(0);
    }

    public longword(bit[] bits) {
        this.bits = bits;
    }

    public longword(int val) {
        bits = new bit[32]; // fills values with 0 by default
        this.set(val);
    }

    @Override
    public bit getBit(int i) {
        return bits[i];
    }

    @Override
    public void setBit(int i, bit value) {
        bits[i] = value;
    }

    @Override
    public longword and(longword other) {
        bit[] temp = new bit[32];

        for (int i = 0; i < bits.length; i++)
            temp[i] = bits[i].and(other.bits[i]);

        return new longword(temp);
    }

    @Override
    public longword or(longword other) {
        bit[] temp = new bit[32];

        for (int i = 0; i < bits.length; i++)
            temp[i] = bits[i].or(other.bits[i]);

        return new longword(temp);
    }

    @Override
    public longword xor(longword other) {
        bit[] temp = new bit[32];

        for (int i = 0; i < bits.length; i++)
            temp[i] = bits[i].xor(other.bits[i]);

        return new longword(temp);
    }

    @Override
    public longword not() {
        bit[] temp = new bit[32];

        for (int i = 0; i < bits.length; i++)
            temp[i] = bits[i].not();

        return new longword(temp);
    }

    @Override
    public longword rightShift(int amount) {
        bit[] temp = new bit[32];
        bit first = this.bits[0]; // store 1 for negative, 0 for positive
        for (int i = amount; i < 32; i++)
            temp[i] = this.bits[i-amount];

        for (int j = 0; j < amount; j++)
            temp[j] = first; // fill beginning slots with first

        return new longword(temp);
    }

    @Override
    public longword leftShift(int amount) {
        bit[] temp = new bit[32];

        for (int i = 0; i < 32 - amount; i++)
            temp[i] = this.bits[i+amount];

        for (int j = 32 - amount; j < 32; j++)
            temp[j] = new bit(0);

        return new longword(temp);
    }

    @Override
    public long getUnsigned() {
        //System.out.println(Integer.toUnsignedLong(getSigned()));
        return Integer.toUnsignedLong(getSigned());
    }

    @Override
    public int getSigned() {
        //System.out.println(Integer.parseUnsignedInt(strBuilder(), 2));
        return Integer.parseUnsignedInt(strBuilder(), 2);
    }

    @Override
    public void copy(longword other) {
        this.bits = other.bits;
    }

    @Override
    public void set(int value) { // convert int to unsigned 32 binary string
        String result = Long.toBinaryString( Integer.toUnsignedLong(value) | 0x100000000L ).substring(1);
        for (int i = 0; i < bits.length; i++)
            bits[i] = new bit(Integer.parseInt(String.valueOf(result.charAt(i))));
    }

    @Override
    public String toString() {
        StringBuilder strRet= new StringBuilder();
        for(bit i : bits) {
            strRet.append(i.getValue());
            strRet.append(",");
        }
        strRet.deleteCharAt(strRet.length()-1); //trim last ","
        return strRet.toString();
    }

    // returns string in format "000000110100100101"...
    public String strBuilder() {
        StringBuilder strRet= new StringBuilder();
        //for(bit i : bits) strRet.append(i.getValue());
        for( int i = 0; i < 32; i++) {
            strRet.append(this.bits[i].getValue());
        }
        return strRet.toString();
    }
}


