package com.lukasz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
    private List<String> instructions;
    private Map<Character, Long> registers;
    private int index;
    private long frequencyOfLastSound;
    private long firstRecovered;

    public Solution(List<String> instructions) {
        this.instructions = instructions;
        this.registers = new HashMap<>();
        index = 0;
        frequencyOfLastSound = 0;
        firstRecovered = 0;

        startProgram();
    }

    private void startProgram() {
        while(index < instructions.size()) {
            String instruction = instructions.get(index);
            String command = instruction.substring(0, 3);

            char reg = findRegister(instruction);
            long value = findValue(instruction);

            executeCommand(command, reg, value);
        }
    }

    private char findRegister(String instruction) {
        char reg = instruction.charAt(4);
        if(!registers.containsKey(reg)) {
            registers.put(reg, 0l);
        }

        return reg;
    }

    private long findValue(String instruction) {
        String valueReg = "";
        long value = 0;
        if(instruction.length() > 6) {
            valueReg = instruction.substring(6);
            try {
                value = Integer.parseInt(valueReg);
            } catch (NumberFormatException e) {
                value = registers.get(instruction.charAt(6));
            }
        }

        return value;
    }

    private void executeCommand(String command, char reg, long value) {

        switch (command) {
            case "snd":
                if( (reg > 47 && reg < 58) ) {
                    frequencyOfLastSound = reg - 48;
                }else {
                    frequencyOfLastSound = registers.get(reg);
                }
                break;

            case "set":
                registers.put(reg, value);
                break;

            case "add":
                value += registers.get(reg);
                registers.put(reg, value);
                break;

            case "mul":
                value *= registers.get(reg);
                registers.put(reg, value);
                break;

            case "mod":
                value = registers.get(reg) % value;
                registers.put(reg, value);
                break;

            case "rcv":
                if( (reg > 48 && reg < 58) || registers.get(reg) > 0 ) {
                    if(firstRecovered == 0) {
                        firstRecovered = frequencyOfLastSound;
                        index = instructions.size();
                    }
                }
                break;

            case "jgz":
                if( (reg > 48 && reg < 58) || registers.get(reg) > 0 ) {
                    index += (value - 1);
                }
                break;

        }
        index++;
    }

    public long getFirstRecovered() {
        return firstRecovered;
    }
}
