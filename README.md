# HumbleBit
32-Bit Operating System

## Overview
Humblebit is a 32-bit assembly language made in java using binary.  
It contains an arithmetic-logic unit (ALU), assembler, 4-step computer (fetch, decode, execute, and store), and 8192 bits of memory.  
The assembly language has 9 operators, 9 keywords, and 4 conditionals.  


## Bit 
A bit represents a single point in memory, being either a 1 or a 0, and is the base or all functionality in this program.  
It has 8 possible operations: set, toggle, clear, getValue, AND, OR, XOR, and NOT.  

The XOR operation (bit.java) :    
```java
    @Override
    public bit xor(bit other) {

        switch (this.value) {
            case 0:
                switch (other.value) {
                    case 0:
                        return new bit(0);
                    case 1:
                        return new bit(1);
                }
            case 1:
                return new bit(other.not().value);
        }

        return null;
    }
```
 


## Longword
A longword is made up of 32-bits, and is responsible for storing all types of variables used in the program.  
It has 12 operations: getBit, setBit, AND, OR, XOR, NOT, rightShift, leftShift, getUnsigned, getSigned, copy, and set.

The rightShift operation (longword.java) :  
```java
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
```

## Arithmetic Logic Unit (ALU)
The ALU has one method, doOP (do operation), which takes in an array of bits and two longwords as parameters.  
To do the specified operation on the longwords, the operation is converted to String and passed through a switch statement, returning the result.
You can view this method in the ALU.java file.

## Assembler
The assembler is responsible for taking in instructions in assembly instruction format, checking for valid syntax, and converting to a binary String that can be read and executed by the Computer.  
To achieve this, I use a token data structure. Each token can be one of 5 types: OPERATOR, KEYWORD, REGISTER, NUMBER, or CONDITIONAL, along with its binary equivelant.  
For efficient lookup of the pre-made tokens I use a HashMap<String, Token> to store each operations binary value.  

Adding the "AND" operation to the HashMap in populateReferenceTable() :  
```java
 RT.put("AND", new Token(Type.OPERATOR, "1000"));
```

## Computer 
The Computer class takes in instructions (String of 1's and 0's) from the Assembler and iterates through each in 4 steps: fetch, decode, execute, and store.  
Computer's run() method :  
```java
    public void run() {
        while (halted.getValue() == 0) {
            fetch();
            decode();
            execute();
            store();
        }
    }
```
When a computer object is initialized, it populates the needed "masks" for doing the binary operations.



