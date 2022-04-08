package com.nickblaauboer.memory;

import com.nickblaauboer.Assembler.Assembler;

/*
I think everything is working correctly, but am not sure how you would like the "return" keyword to work.
In the assignment document there are two return statements, the first one called is in the add function,
and the second one called is in main. The first one is called with 2 items in the stack (value and function call address),
The second return in main is called with one item in the stack (value 7).
Right now I have it so that if return is called with more than one item in the stack, it pops and jumps to the function
call address. When one item is in the stack, the cpu will be halted. In my tests I pop the stack into r10 before the
last return call, and check that value to make sure my tests passed.
I hope this is how you intended it to work.
 */



public class cpu_test3 {
    private static boolean passed = true;

    public static void runTests() {
        System.out.print("\nRunning cpu_test3 ... ");

        // Add 4 to 3 and store in r10
        String[] program1 = Assembler.assemble(new String[] {"Move r1 3", "move r2 4", "push r1","push r2", "call 14", "pop r10", "return",
                "pop r15", "pop r1", "pop r2", "add r1 r2 r3", "push r3", "push r15", "return"});
        computer cpu1 = new computer();
        cpu1.preload(program1);
        cpu1.run();
        if (cpu1.getRegisterValueAt(10) != 7) passed = false;

        // Multiply 3 by 4 and store in R10
        String[] program2 = Assembler.assemble(new String[] {"Move r1 3", "move r2 4", "push r1","push r2", "call 14", "pop r10", "return",
                "pop r15", "pop r1", "pop r2", "multiply r1 r2 r3", "push r3", "push r15", "return"});
        computer cpu2 = new computer();
        cpu2.preload(program2);
        cpu2.run();
        if (cpu2.getRegisterValueAt(10) != 12) passed = false;

        // subtracting 4 from 3, notice how r2 and r1 are pushed in a different order
        String[] program3 = Assembler.assemble(new String[] {"Move r1 3", "move r2 4", "push r2","push r1", "call 14", "pop r10", "return",
                "pop r15", "pop r1", "pop r2", "subtract r1 r2 r3", "push r3", "push r15", "return"});
        computer cpu3 = new computer();
        cpu3.preload(program3);
        cpu3.run();
        if (cpu3.getRegisterValueAt(10) != -1) passed = false;


        // jump over halt and store 5 in r10
        String[] program4 = Assembler.assemble(new String[]{"jump 2", "halt", "move r10 5"});
        computer cpu4 = new computer();
        cpu4.preload(program4);
        cpu4.run();
        if (cpu4.getRegisterValueAt(10) != 5) passed = false;
        // branch if greater than
        String[] program5 = Assembler.assemble(new String[]{"Move r1 5", "Move r2 3", "Compare r1 r2", "branchifGreaterThan 2", "halt", "move r10 5"});
        computer cpu5 = new computer();
        cpu5.preload(program5);
        cpu5.run();
        if (cpu5.getRegisterValueAt(10) != 5) passed = false;
        // branch if equal
        String[] program6 = Assembler.assemble(new String[] {"Move r1 -3", "Move r2 -3", "Compare r1 r2", "branchifEqual 2", "halt", "move r10 5"});
        computer cpu6 = new computer();
        cpu6.preload(program6);
        cpu6.run();
        if (cpu6.getRegisterValueAt(10) != 5) passed = false;
        // branch if greater or equal
        String[] program7 = Assembler.assemble(new String[] {"Move r1 10", "Move r2 2", "Compare r1 r2", "branchIfGreaterOrEqual 2", "halt", "move r10 5"});
        computer cpu7 = new computer();
        cpu7.preload(program7);
        cpu7.run();
        if (cpu7.getRegisterValueAt(10) != 5) passed = false;
        // branch if not equal
        String[] program8 = Assembler.assemble(new String[] {"Move r1 10", "Move r2 2", "Compare r1 r2", "branchIfNotEqual 2", "halt", "move r10 5"});
        computer cpu8 = new computer();
        cpu8.preload(program8);
        cpu8.run();
        if (cpu8.getRegisterValueAt(10) != 5) passed = false;
        // branch if equal, prints registers if successful
        String[] program9 = Assembler.assemble(new String[] {"Move r1 4", "Move r2 4","Compare r1 r2","branchIfEqual 4", "halt", "halt", "move r10 5"});
        computer cpu9 = new computer();
        cpu9.preload(program9);
        cpu9.run();
        if (cpu9.getRegisterValueAt(10) != 5) passed = false;



        if (passed)
            System.out.println("PASSED");
        else System.err.print("FAILED");

    }
}
