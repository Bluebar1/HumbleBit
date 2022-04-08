package com.nickblaauboer.Assembler;

import com.nickblaauboer.memory.computer;

import java.util.Arrays;


public class assembler_test {

    private static boolean passed = true;

    public static void runTests() {
        System.out.print("\nRunning assembler tests... ");

        // program to a store 10+5 in R3
//        String[] program1 =  Assembler.assemble(new String[] {"Move r1 10", "Move r2 5", "Add r1 r2 r3", "halt"});
//        computer cpu1 = new computer();
//        cpu1.preload(program1);
//        cpu1.run();
//        if (cpu1.getRegisterValueAt(3) != 15) passed = false;
//
//        // program to multiply 4*6 into r15
//        String[] program2 =  Assembler.assemble(new String[] {"Move r1 4", "Move r2 6", "Multiply r1 r2 r15", "halt"});
//        computer cpu2 = new computer();
//        cpu2.preload(program2);
//        cpu2.run();
//        if (cpu2.getRegisterValueAt(15) != 24) passed = false;
//
//        // branch if equal, prints registers if successful
//        String[] program3 = Assembler.assemble(new String[] {"Move r1 4", "Move r2 4","Compare r1 r2","branchIfEqual 4", "halt", "halt", "interrupt 0"});
//        computer cpu3 = new computer();
//        cpu3.preload(program3);
//        cpu3.run();
//        // jump over instruction to put 5 in R1
//        String[] program4 = Assembler.assemble(new String[]{"jump 4", "move r1 5", "interrupt 0", "halt"});
//        computer cpu4 = new computer();
//        cpu4.preload(program4);
//        cpu4.run();
//        if (cpu4.getRegisterValueAt(1) == 5) passed = false;
//        // branch if greater than
//        String[] program5 = Assembler.assemble(new String[]{"Move r1 5", "Move r2 3", "Compare r1 r2", "branchifGreaterThan 2", "halt", "interrupt 0"});
//        computer cpu5 = new computer();
//        cpu5.preload(program5);
//        cpu5.run();
//        // branch if equal
//        String[] program6 = Assembler.assemble(new String[] {"Move r1 -3", "Move r2 -3", "Compare r1 r2", "branchifEqual 2", "halt", "interrupt 0"});
//        computer cpu6 = new computer();
//        cpu6.preload(program6);
//        cpu6.run();
//        // branch if greater or equal
//        String[] program7 = Assembler.assemble(new String[] {"Move r1 10", "Move r2 2", "Compare r1 r2", "branchIfGreaterOrEqual 2", "halt", "interrupt 0"});
//        computer cpu7 = new computer();
//        cpu7.preload(program7);
//        cpu7.run();
//        // branch if not equal
//        String[] program8 = Assembler.assemble(new String[] {"Move r1 10", "Move r2 2", "Compare r1 r2", "branchIfNotEqual 2", "halt", "interrupt 0"});
//        computer cpu8 = new computer();
//        cpu8.preload(program8);
//        cpu8.run();

        String[] program9 = Assembler.assemble(new String[] {"Move r1 3", "move r2 4", "push r1","push r2", "call 12", "return",
                "pop r15", "pop r1", "pop r2", "add r1 r2 r3", "push r3", "push r15", "return"});
        System.out.println(Arrays.toString(program9));
        computer cpu9 = new computer();
        cpu9.preload(program9);
        cpu9.run();
        //System.out.println(cpu9.getRegisterValueAt(15));

        //

        if (passed)
            System.out.println("PASSED");
        else System.err.print("FAILED");

    }
}
