package com.lukasz;

import java.util.*;

public class Solution2 {
    private List<String> instructions;
    private Queue<Long> program1Queue;
    private Queue<Long> program2Queue;
    private int program1SendValues;

    public Solution2(List<String> instructions) {
        this.instructions = instructions;
        program1Queue = new LinkedList<>();
        program2Queue = new LinkedList<>();
        Program program0 = new Program(0, program1Queue, program2Queue);
        Program program1 = new Program(1, program2Queue, program1Queue);

        while(true) {
            program0.step();
            program1.step();
            if(program0.isLocked() && program1.isLocked()) {
                program1SendValues = program1.sendValues;
                break;
            }
        }
    }

    public int getProgram1SendValues() {
        return program1SendValues;
    }

    class Program {
        private Map<Character, Long> registers;
        private Queue<Long> thisProgramQueue;
        private Queue<Long> otherProgramQueue;
        private int index;
        private int sendValues;
        private boolean locked;

        public Program(long id, Queue<Long> thisProgramQueue, Queue<Long> otherProgramQueue) {
            this.registers = new HashMap<>();
            this.thisProgramQueue = thisProgramQueue;
            this.otherProgramQueue = otherProgramQueue;
            registers.put('p', id);
            index = 0;
            sendValues = 0;
            locked = false;
        }

        public void step() {
            if(index < 0 || index > instructions.size()) {
                locked = true;
                return;
            }
            String instruction = instructions.get(index);
            String command = instruction.substring(0, 3);

            char reg = findRegister(instruction);
            long value = findValue(instruction);

            executeCommand(command, reg, value);
        }

        private char findRegister(String instruction) {
            char reg = instruction.charAt(4);
            if((reg > 58) && !registers.containsKey(reg)) {
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
                    long valueToSend = 0;
                    if( (reg > 47 && reg < 58) ) {
                        valueToSend = reg - 48;
                    }else {
                        valueToSend = registers.get(reg);
                    }
                    thisProgramQueue.offer(valueToSend);
                    sendValues++;
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
                    if(otherProgramQueue.isEmpty()) {
                        locked = true;
                        index--;
                    }else {
                        locked = false;
                        long newValue = otherProgramQueue.poll();
                        registers.put(reg, newValue);
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

        public boolean isLocked() {
            return locked;
        }

        public int getSendValues() {
            return sendValues;
        }
    }
}
