package com.lukasz;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static List<String> getInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter instructions: ");
        String line;
        List<String> instructions = new ArrayList<>();


        while(!(line = scanner.nextLine()).isEmpty()) {
            instructions.add(line);
        }

        return instructions;
    }

    public static void main(String[] args) {
        List<String> instructions = getInput();
        Solution solution = new Solution(instructions);
        Solution2 solution2 = new Solution2(instructions);
        System.out.println("First recovered frequency is " + solution.getFirstRecovered());
        System.out.println("Program 1 send " + solution2.getProgram1SendValues() + " values.");
    }
}
