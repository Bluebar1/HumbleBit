package com.nickblaauboer.memory;

import com.nickblaauboer.ALU.ALU;
import com.nickblaauboer.bit.bit;
import com.nickblaauboer.longword.longword;
import com.nickblaauboer.rippleAdder.rippleAdder;

public class computer {

    private bit halted = new bit(0);
    private memory mem = new memory();
    private longword PC = new longword(0); // Start on byte 4 because preload starts writing at 4
    private longword SP = new longword(1020);
    private longword currentInstruction = new longword(0);
    private longword[] registers = new longword[16];
    private longword result = new longword(0);
    private longword op1 = new longword(0);
    private longword op2 = new longword(0);

    private longword op1Mask = new longword(0);
    private longword op2Mask = new longword(0);
    private longword resultMask = new longword(0);
    private longword opCodeMask = new longword(0);
    private longword addressMask = new longword(0);
    private longword conditionCodeMask = new longword(0);
    private longword conditionSignMask = new longword(0);
    private longword conditionAddressMask = new longword(0);
    private longword immediateMask;

    private static int opCode;
    private bit[] comparisonBits = new bit[]{ new bit(0), new bit(0)};
    int cc;
    private longword branchAddress = new longword(0);
    private longword addressSign = new longword(0);



    public computer() {

        for (int i = 0; i < 16; i++)
            registers[i] = new longword(0);

        op1Mask.setBit(4, new bit(1));
        op1Mask.setBit(5, new bit(1));
        op1Mask.setBit(6, new bit(1));
        op1Mask.setBit(7, new bit(1));

        op2Mask.setBit(8, new bit(1));
        op2Mask.setBit(9, new bit(1));
        op2Mask.setBit(10, new bit(1));
        op2Mask.setBit(11, new bit(1));

        resultMask.setBit(12, new bit(1));
        resultMask.setBit(13, new bit(1));
        resultMask.setBit(14, new bit(1));
        resultMask.setBit(15, new bit(1));

        opCodeMask.setBit(0, new bit(1));
        opCodeMask.setBit(1, new bit(1));
        opCodeMask.setBit(2, new bit(1));
        opCodeMask.setBit(3, new bit(1));

        for (int i = 4; i < 32; i++)
            addressMask.setBit(i, new bit(1));

        conditionCodeMask.setBit(4, new bit(1));
        conditionCodeMask.setBit(5, new bit(1));

        conditionSignMask.setBit(6, new bit(1));

        for (int i = 7; i < 16; i++)
            conditionAddressMask.setBit(i, new bit(1));

        immediateMask = op2Mask.or(resultMask);
    }

    public void run() {
        while (halted.getValue() == 0) {
            fetch();
            decode();
            execute();
            store();
        }
    }

    private void fetch() {
        currentInstruction = mem.read(PC); //copy instructions from memory by program counter
        PC.set(rippleAdder.add(PC, new longword(2)).getSigned());

    }

    private void decode() {
        opCode = currentInstruction.and(opCodeMask).rightShift(28).getSigned();
        int regNum1, regNum2;

        switch (opCode) {
            case 1 : // move
            case 3 : // jump
                op1.set(currentInstruction.and(immediateMask).rightShift(16).getSigned());
                if (op1.getBit(24).getValue() == 1)  // check for negative numbers
                    for (int i = 0; i < 24; i++) // fill bits 0-23 with 1
                        op1.setBit(i, new bit(1));
                break;

            case 4 : // compare
                regNum1 = currentInstruction.and(op2Mask).rightShift(20).getSigned();
                regNum2 = currentInstruction.and(resultMask).rightShift(16).getSigned();
                op1.set(registers[regNum1].getSigned());
                op2.set(registers[regNum2].getSigned());
                break;

            case 6 : // call, return, push, pop
                if (currentInstruction.getBit(5).getValue() == 0) {
                    if (currentInstruction.getBit(4).getValue() == 0) { // push
                        int regNum3 = currentInstruction.and(resultMask).rightShift(16).getSigned();
                        op1.set(regNum3);
                    } else { // call
                        op1.set(PC.getSigned());
                        if (op1.getBit(24).getValue() == 1)  // check for negative numbers
                            for (int i = 0; i < 24; i++) // fill bits 0-23 with 1
                                op1.setBit(i, new bit(1));
                    }

                } else if (currentInstruction.getBit(5).getValue() == 1) {
                    if (currentInstruction.getBit(4).getValue() == 0) { // pop
                        int regNum4 = currentInstruction.and(resultMask).rightShift(16).getSigned();
                        op1.set(regNum4);

                    }

                }
                break;

            case 2 : // interrupt
            default : // operations and conditionals
                regNum1 = currentInstruction.and(op1Mask).rightShift(24).getSigned();
                regNum2 = currentInstruction.and(op2Mask).rightShift(20).getSigned();
                op1.set(registers[regNum1].getSigned());
                op2.set(registers[regNum2].getSigned());
                break;
        }



    }


    private void execute() {
        if (opCode == 0) // is halt
            halted.set(1);
        else if (opCode == 1)  // Move
            result.set(op1.getSigned());
        else if (opCode == 2) { // interrupt
            int intNumber = currentInstruction.and(resultMask).rightShift(16).getSigned();
            if (intNumber == 0) printRegisters();
            if (intNumber == 1) mem.printMemory();
        }
        else if (opCode == 4) { // Compare
            longword lw = ALU.doOP(new bit[] {new bit(1), new bit(1), new bit(1), new bit(1)}, op1, op2);
            if (lw.getSigned() == 0) {
                comparisonBits[1] = new bit(1);
            }
            else if (lw.getSigned() > 0) {
                comparisonBits[0] = new bit(1);
                comparisonBits[1] = new bit(0);
            } else {
                comparisonBits[0] = new bit(0);
                comparisonBits[1] = new bit(0);
            }
        }
        else if (opCode == 5) { // conditional

            cc = currentInstruction.and(conditionCodeMask).rightShift(26).getSigned();
            branchAddress = currentInstruction.and(conditionAddressMask).rightShift(16);

            // why is this needed if an address cannot be negative?
            addressSign = currentInstruction.and(conditionSignMask).rightShift(25);
            if (addressSign.getSigned() == 1)
                for (int i = 0; i < 23; i++)
                    branchAddress.setBit(i, new bit(1));
        }
        else if (opCode == 6) {
            if (currentInstruction.getBit(5).getValue() == 0) {
                // push
                if (currentInstruction.getBit(4).getValue() == 0) {
                    mem.write(SP, registers[op1.getSigned()]);
                    SP.set(rippleAdder.subtract(SP, new longword(4)).getSigned());
                }
                else { // call
                    mem.write(SP, op1);
                    SP.set(rippleAdder.subtract(SP, new longword(4)).getSigned());
                    branchAddress = currentInstruction.and(conditionAddressMask).rightShift(16);
                    PC.set(branchAddress.getSigned());
                }

            } else if (currentInstruction.getBit(5).getValue() == 1) {
                // pop
                if (currentInstruction.getBit(4).getValue() == 0) {
                    SP.set(rippleAdder.add(SP, new longword(4)).getSigned());
                    registers[op1.getSigned()].set(mem.read(SP).getSigned());

                } else { // return
                    if (SP.getSigned() == 1020) { // end program when final return is called
                        halted.set(1);
                    } else { // Set PC and pop otherwise
                        PC.set(mem.read(SP).getSigned());
                        SP.set(rippleAdder.add(SP, new longword(4)).getSigned());
                    }
                }
            }
        }
        else if (opCode != 3) { // make sure its an operation
            bit[] opcodeBits = new bit[]{
                    new bit(currentInstruction.getBit(0).getValue()),
                    new bit(currentInstruction.getBit(1).getValue()),
                    new bit(currentInstruction.getBit(2).getValue()),
                    new bit(currentInstruction.getBit(3).getValue())
            };
            result.set(ALU.doOP(opcodeBits, op1, op2).getSigned());
        }
    }

    private void store() {
        if (opCode == 1) { // Move
            int resNum = currentInstruction.and(op1Mask).rightShift(24).getSigned();
            registers[resNum].set(result.getSigned());// = result;

        }
        else if (opCode == 3) { // Jump
            PC.set(rippleAdder.add(PC, currentInstruction.and(addressMask).rightShift(16)).getSigned());
        }
        // This can be condensed
        else if (opCode == 5) { // Branch
            if (cc == 0) { // not equal
                if (comparisonBits[1].getValue() == 0) {
                    PC = rippleAdder.add(PC, branchAddress);
                }
            } else if (cc == 1) { // equal
                if (comparisonBits[1].getValue() == 1) {
                    PC = rippleAdder.add(PC, branchAddress);
                }
            } else if (cc == 2) { // greater
                if (comparisonBits[0].getValue() == 1) {
                    PC = rippleAdder.add(PC, branchAddress);
                }
            } else if (cc == 3) { // greater or equal
                if (comparisonBits[0].getValue() == 1 || comparisonBits[1].getValue() == 1) {
                    PC = rippleAdder.add(PC, branchAddress);
                }
            }
            else System.out.println("opcode 5 failed");
        }
        else if (opCode != 6){
            int resNum = currentInstruction.and(resultMask).rightShift(16).getSigned();
            registers[resNum].set(result.getSigned());
        }
    }

    public void preload(String[] data) {

        int index = 0;
        boolean isFirstWord = true;
        longword input = new longword(0);
        int j = 0;
        for (String ins : data) {

            ins = ins.replaceAll("\\s", ""); // Allows for spaces in instructions

            if (isFirstWord) {
                for (int i = 0; i < 16; i++)
                    input.setBit(i, ins.charAt(i) == '0' ? new bit(0) : new bit(1));
            } else {
                for (int i = 0; i < 16; i++)
                    input.setBit(16 + i, ins.charAt(i) == '0' ? new bit(0) : new bit(1));
            }

            if (!isFirstWord) {
                mem.write(new longword(index), input);
                index += 4;
            } else if (j == data.length-1) {
                for (int k = 16; k < 32; k++)
                    input.setBit(k, new bit(0));

                mem.write(new longword(index), input);
            }

            isFirstWord = !isFirstWord;
            j++;

        }
    }

    private void printRegisters() {
        for (longword lw : registers)
            System.out.println(lw);
    }

    public int getRegisterValueAt(int index) { // used for tests
        return registers[index].getSigned();
    }



}

//    private void printStack() {
//        System.out.println("STACK PRINT OUT");
//        boolean flag = true;
//        int start = 1020;
//        while (flag) {
//            longword temp = mem.read(new longword(start));
//            System.out.println(mem.read(new longword(start)));
//            start = start - 4;
//            if (temp.getSigned() == 0) flag = false;
//        }
//    }


//        if (opCode != 1) {
//            if (opCode != 4) {
//
//                if (opCode == 6) {
//                    //System.out.println("OPCODE 6 DECODE");
//                    if (currentInstruction.getBit(5).getValue() == 0) {
//                        if (currentInstruction.getBit(4).getValue() == 0) { // push
//                            System.out.println("PUSH DECODE CALLED");
//                            int regNum3 = currentInstruction.and(resultMask).rightShift(16).getSigned();
//                            System.out.println(regNum3);
//                            //op1.set(registers[regNum3].getSigned());
//                            op1.set(regNum3);
//                        } else { // call
//                            //op1.set(currentInstruction.and(immediateMask).rightShift(16).getSigned());
//                            op1.set(PC.getSigned());
//                            if (op1.getBit(24).getValue() == 1)  // check for negative numbers
//                                for (int i = 0; i < 24; i++) // fill bits 0-23 with 1
//                                    op1.setBit(i, new bit(1));
//                        }
//
//                    } else if (currentInstruction.getBit(5).getValue() == 1) {
//                        if (currentInstruction.getBit(4).getValue() == 0) { // pop
//                            int regNum4 = currentInstruction.and(resultMask).rightShift(16).getSigned();
//                            //System.out.println("POP OP1 : " + regNum4 );
//                            op1.set(regNum4);
//                            //op1.set(registers[regNum4].getSigned());
//                            //registers[regNum4].set(op1.getSigned());
//
//
//                            //System.out.println("FINAL OP1 : " + op1.getSigned());
////                            for (longword lw : registers)
////                                System.out.println(lw);
//                        } else { // return
//                            //System.out.println("Return setting op1 to : " + mem.read(SP).getSigned());
//                            //op1.set(mem.read(SP).getSigned());
//                            //op1.set(mem.read(SP).getSigned());
//                            //PC.set(mem.read(SP).getSigned());
//                            //System.out.println(op1);
//                            //System.out.println("return called");
//                        }
//
//                    }
//                }
//                else {
//                    int regNum1 = currentInstruction.and(op1Mask).rightShift(24).getSigned();
//                    int regNum2 = currentInstruction.and(op2Mask).rightShift(20).getSigned();
//                    op1.set(registers[regNum1].getSigned());
//                    op2.set(registers[regNum2].getSigned());
//                }
//            } else {
//                // For compare, op1 and op2 are moved to the right so 4 bits so op2 and result masks are used
//                int regNum1 = currentInstruction.and(op2Mask).rightShift(20).getSigned();
//                int regNum2 = currentInstruction.and(resultMask).rightShift(16).getSigned();
//                op1.set(registers[regNum1].getSigned());
//                op2.set(registers[regNum2].getSigned());
//            }
//        }
//        else {
//            op1.set(currentInstruction.and(immediateMask).rightShift(16).getSigned());
//            if (op1.getBit(24).getValue() == 1)  // check for negative numbers
//                for (int i = 0; i < 24; i++) // fill bits 0-23 with 1
//                    op1.setBit(i, new bit(1));
//        }