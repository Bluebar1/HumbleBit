package com.nickblaauboer.Assembler;


import com.nickblaauboer.longword.longword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Assembler {

    private static HashMap<String, Token> RT; // Reference Table

    // data structure for token creation
    public static class Token {
        public final Type t;
        public final String c; // further information
        public Token(Type t, String c) {
            this.t = t;
            this.c = c;
        }
    }

    private enum Type {
        OPERATOR, // AND, OR, XOR, NOT, LEFTSHIFT, RIGHTSHIFT, ADD SUBTRACT, MULTIPLY
        KEYWORD, // MOVE, HALT, INTERUPT, JUMP, CALL, RETURN, PUSH, POP
        REGISTER, // R0-R15
        NUMBER, // BYTE LENGTH INTEGER
        CONDITIONAL
    }

    public static String[] assemble(String[] input) {

        populateReferenceTable();

        List<Token> tokens = new ArrayList<Token>();
        String[] result = new String[input.length];

        for (String str : input) {
            int startIndex = 0; // tracks where the current word starts
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == ' ' || i == str.length()-1) { // searching for space or end of instruction
                    String word = str.substring(startIndex, (i == str.length()-1) ? i + 1 : i).toUpperCase();
                    // Tokenize each word
                    if (RT.containsKey(word)) {
                        //System.out.println("CONTAINS KEYWORD :: " + word);
                        tokens.add(RT.get(word));
                    }

                    else if (isNumber(word)) {
                        Token previousToken = tokens.get(tokens.size()-1);
                        if (previousToken.t == Type.KEYWORD && previousToken.c.equalsIgnoreCase("0011")) {
                            String st = new longword(Integer.parseInt(word)).strBuilder().substring(20, 32); // convert to 8bit string
                            tokens.add(new Token(Type.NUMBER, st));
                        }
                        else if (previousToken.t == Type.CONDITIONAL && previousToken.c.substring(0, 4).equalsIgnoreCase("0101")) { // comparison
                            String st = new longword(Integer.parseInt(word)).strBuilder().substring(22, 32); // convert to 10bit string
                            tokens.add(new Token(Type.NUMBER, st));
                        }
                        else if (previousToken.t == Type.KEYWORD && previousToken.c.substring(0, 4).equalsIgnoreCase("0110")) { // call, return, push, pop
                            String st = new longword(Integer.parseInt(word)).strBuilder().substring(22, 32); // convert to 10bit string
                            tokens.add(new Token(Type.NUMBER, st));
                        }
                        else {
                            String st = new longword(Integer.parseInt(word)).strBuilder().substring(24, 32); // convert to 8bit string
                            tokens.add(new Token(Type.NUMBER, st));
                        }
                    } else
                        error(word + " is not valid!");

                    startIndex = i + 1; // increasing starting index by one to ignore beginning whitespace on next loop
                }
            }
        }

        // Use list of tokens to check syntax and create String[] result

        // tracks which token is being looked at, is incremented differently based on how long each instruction is
        // (eg. "Add r1 r2 r3" would increase the tokenTracker 4 times because there is 4 words in the instruction)
        int tokenTracker = 0;

        for (int i = 0; i < input.length; i++) { // iterate over whole instruction

            //System.out.println("Looking at : " + tokens.get(tokenTracker).c);

            switch (tokens.get(tokenTracker).t) {

                case OPERATOR:
                    // expect 3 registers following
                    StringBuilder opString = new StringBuilder(tokens.get(tokenTracker).c);
                    // Search next 3 tokens, ensure all are registers
                    for (int j = tokenTracker + 1; j < tokenTracker + 4; j++) {
                        if (tokens.get(j).t != Type.REGISTER)
                            error("Operations must be followed by 3 registers");

                        opString.append(tokens.get(j).c); // append 4 bit address for each register
                    }
                    tokenTracker += 4; // operation instructions are 4 tokens long
                    result[i] = opString.toString();
                    break;

                case KEYWORD:
                    // expect number after interrupt
                    StringBuilder kwBuilder = new StringBuilder();
                    String kw = tokens.get(tokenTracker).c;
                    if (kw.equalsIgnoreCase("001000000000")) { // INTERRUPT
                        if (tokens.get(tokenTracker + 1).t == Type.NUMBER) { // syntax check, make sure a number token is after
                            kwBuilder.append(tokens.get(tokenTracker).c);
                            if (tokens.get(tokenTracker + 1).c.equals("00000001"))
                                kwBuilder.append("0001");
                            else if (tokens.get(tokenTracker + 1).c.equals("00000000"))
                                kwBuilder.append("0000");
                        }
                        tokenTracker += 2;
                    }
                    // expect register and number after move
                    else if (kw.equalsIgnoreCase("000100000000")) { // MOVE
                        if ((tokens.get(tokenTracker + 1).t == Type.REGISTER) &&
                                (tokens.get(tokenTracker + 2).t == Type.NUMBER)) {
                            kwBuilder.append("0001");
                            kwBuilder.append((tokens.get(tokenTracker + 1).c));
                            kwBuilder.append(tokens.get(tokenTracker + 2).c);
                        } else
                            error("Move must be followed by a register and a number");

                        tokenTracker += 3;
                    }

                    // expect nothing after halt command
                    else if (kw.equalsIgnoreCase("0000000000000000")) { // HALT
                        kwBuilder.append(kw);
                        tokenTracker++;
                    }
                    else if (kw.equalsIgnoreCase("0011")) {
                        if (tokens.get(tokenTracker + 1).t == Type.NUMBER) {
                            kwBuilder.append(kw);
                            kwBuilder.append(tokens.get(tokenTracker + 1).c);
                            tokenTracker += 2;
                        } else {
                            error("The move keyword must be followed by one number, the decimal address");
                        }
                    }
                    else if (kw.equalsIgnoreCase("01000000")) { // Compare
                        if (tokens.get(tokenTracker + 1).t == Type.REGISTER) {
                            kwBuilder.append(kw);
                            kwBuilder.append(tokens.get(tokenTracker + 1).c); //  Append 2 Registers
                            kwBuilder.append(tokens.get(tokenTracker + 2).c);
                            tokenTracker += 3;
                        } else {
                            error("The compare keyword must be followed by 2 registers");
                        }
                    }
                    else if (kw.substring(0,4).equalsIgnoreCase("0110")) { // call, return, push, pop
                        //System.out.println(kw.substring(4,6));
                        if (kw.substring(4,6).equalsIgnoreCase("00")) { // push
                            kwBuilder.append(kw);
                            kwBuilder.append(tokens.get(tokenTracker + 1).c); // append register
                            tokenTracker += 2;
                        } else if (kw.substring(4,6).equalsIgnoreCase("01")) { // pop
                            kwBuilder.append(kw);
                            kwBuilder.append(tokens.get(tokenTracker + 1).c); // append register
                            tokenTracker += 2;
                        } else if (kw.substring(4,6).equalsIgnoreCase("10")) { // call
                            //System.out.println("CALL HAS BEEN ENTERED");
                            kwBuilder.append(kw);
                            kwBuilder.append(tokens.get(tokenTracker + 1).c);
                            //System.out.println(kwBuilder);
                            tokenTracker += 2;
                        } else if (kw.substring(4,6).equalsIgnoreCase("11")) { // return
                            kwBuilder.append(kw);
                            tokenTracker++;
                        }
                    }
                    else
                        error("Keyword not found");

                    result[i] = kwBuilder.toString();
                    break;


//                case EQUALS:
//                    StringBuilder eqBuilder = new StringBuilder();
//                    Token nextToken = tokens.get(tokenTracker + 1);
//                    if (nextToken.t == Type.NUMBER) {
//                        eqBuilder.append(tokens.get(tokenTracker).c);
//                        eqBuilder.append(nextToken.c);
//                    }
//                    break;

                case CONDITIONAL:

                    StringBuilder conditionalBuilder = new StringBuilder();
                    Token nextcToken = tokens.get(tokenTracker + 1);
                    if (nextcToken.t == Type.NUMBER) {
                        conditionalBuilder.append(tokens.get(tokenTracker).c);
                        conditionalBuilder.append(nextcToken.c);
                    }
                    result[i] = conditionalBuilder.toString();
                    tokenTracker+=2;
                    break;

                case REGISTER:
                    error("Instructions cannot be started with a register");
                    break;


                case NUMBER:
                    error("Instructions cannot be started with a number");
                    break;

            }
        }

        return result;
    }

    private static void populateReferenceTable() {
        RT = new HashMap<>();
        // operators
        RT.put("AND", new Token(Type.OPERATOR, "1000"));
        RT.put("OR", new Token(Type.OPERATOR, "1001"));
        RT.put("XOR", new Token(Type.OPERATOR, "1010"));
        RT.put("NOT", new Token(Type.OPERATOR, "1011"));
        RT.put("LEFTSHIFT", new Token(Type.OPERATOR, "1100"));
        RT.put("RIGHTSHIFT", new Token(Type.OPERATOR, "1101"));
        RT.put("ADD", new Token(Type.OPERATOR, "1110"));
        RT.put("SUBTRACT", new Token(Type.OPERATOR, "1111"));
        RT.put("MULTIPLY", new Token(Type.OPERATOR, "0111"));
        // keywords
        RT.put("HALT", new Token(Type.KEYWORD, "0000000000000000"));
        RT.put("MOVE", new Token(Type.KEYWORD, "000100000000"));
        RT.put("INTERRUPT", new Token(Type.KEYWORD, "001000000000"));
        RT.put("JUMP", new Token(Type.KEYWORD, "0011"));
        RT.put("COMPARE", new Token(Type.KEYWORD, "01000000"));
        RT.put("CALL", new Token(Type.KEYWORD, "011010")); // 10 address bits after
        RT.put("RETURN", new Token(Type.KEYWORD, "0110110000000000")); // no variable bits
        RT.put("PUSH", new Token(Type.KEYWORD, "011000000000")); // 4 register bits after
        RT.put("POP", new Token(Type.KEYWORD, "011001000000")); //  4 register bits after

        // conditionals
        RT.put("BRANCHIFEQUAL", new Token(Type.CONDITIONAL, "010101"));
        RT.put("BRANCHIFNOTEQUAL", new Token(Type.CONDITIONAL, "010100"));
        RT.put("BRANCHIFGREATERTHAN", new Token(Type.CONDITIONAL, "010110"));
        RT.put("BRANCHIFGREATEROREQUAL", new Token(Type.CONDITIONAL, "010111"));



        // registers
        RT.put("R0", new Token(Type.REGISTER, "0000"));
        RT.put("R1", new Token(Type.REGISTER, "0001"));
        RT.put("R2", new Token(Type.REGISTER, "0010"));
        RT.put("R3", new Token(Type.REGISTER, "0011"));
        RT.put("R4", new Token(Type.REGISTER, "0100"));
        RT.put("R5", new Token(Type.REGISTER, "0101"));
        RT.put("R6", new Token(Type.REGISTER, "0110"));
        RT.put("R7", new Token(Type.REGISTER, "0111"));
        RT.put("R8", new Token(Type.REGISTER, "1000"));
        RT.put("R9", new Token(Type.REGISTER, "1001"));
        RT.put("R10", new Token(Type.REGISTER, "1010"));
        RT.put("R11", new Token(Type.REGISTER, "1011"));
        RT.put("R12", new Token(Type.REGISTER, "1100"));
        RT.put("R13", new Token(Type.REGISTER, "1101"));
        RT.put("R14", new Token(Type.REGISTER, "1110"));
        RT.put("R15", new Token(Type.REGISTER, "1111"));
    }

    private static boolean isNumber(String word) {
        int start = (word.charAt(0) == '-') ? 1 : 0; // start at index 1 if negative
        // iterate through word and make sure the start -> are ascii integers
        for (int i = start; i < word.length(); i++)
            if (!(word.charAt(i)>47 && word.charAt(i)<58))
                return false;
        return true;
    }

    private static void error(String message) {
        System.err.println("ERROR: "+ message);
    }
}

