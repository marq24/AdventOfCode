package de.emac.aoc;

import java.util.ArrayList;

public class D10 {

    public static void main(String[] args) {
        Util.tStart(0);
        String[] lines = Util.readFileLineByLine("d10.txt");
        v1(lines);
        v2(lines);
        Util.tEnd(0);
    }

    public static class Checker {
        static int pos = 0;
        static ArrayList<Integer> vals = new ArrayList<>();
        static {
            for (int i = 0; i < 10; i++) {
                vals.add(20 + i * 40);
            }
        }
    }

    static Checker checker = new Checker();
    public static void v1(String[] lines) {
        int result = 0;
        int registerX = 1;
        int cycle = 1;
        for(int l=0; l< lines.length; l++){
            String line = lines[l];
            if(line.equals("noop")){
                result = result + checkSignal(cycle, registerX);
                cycle++;

            } else {
                result = result + checkSignal(cycle, registerX);
                cycle++;

                result = result + checkSignal(cycle, registerX);
                cycle++;

                registerX = registerX + Integer.parseInt(lines[l].split(" ")[1], 10);
            }
        }
        // 13220 too high
        // -> 11220 correct
        System.out.println("res1: "+result);
    }

    private static int checkSignal(int cycle, int registerX) {
        if (cycle % checker.vals.get(checker.pos) == 0) {
            checker.pos++;
            System.out.println("cycle: " + cycle + " sigStrength: " + cycle * registerX);
            return cycle * registerX;
        }
        return 0;
    }

    private static class Display {
        boolean[][] display;
        int cLine = 0;
        int cPos = 0;

        public Display(int rows, int pixelPerRow){
            display = new boolean[rows][];
            for(int i=0; i<rows; i++){
                display[i] = new boolean[pixelPerRow];
            }
        }

        public void processPixel(int cycle, int registerXValue) {
            display[cLine][cPos] = cPos == registerXValue || cPos == registerXValue+1 || cPos == registerXValue+2;
            cPos++;
            // do we need a new line in next cycle?!
            if((cycle+1) % display[0].length == 0){
                cLine++;
                cPos = 0;
            }
        }

        public void print(){
            for(int i=0; i < display.length; i++) {
                printLN(display, i);
            }
        }
        private void printLN(boolean[][] display, int pos){
            for(int i=0; i<display[0].length;i++){
                if(display[pos][i]){
                    System.out.print("█");
                }else{
                    System.out.print(" ");
                }
            }
            System.out.println("");
        }
    }

    public static void v2(String[] lines) {
        Display display = new Display(6, 40);
        int registerXValue = 0;
        int cycle = 0;

        for(int l=0; l< lines.length; l++){
            String line = lines[l];
            if(line.equals("noop")){
                display.processPixel(cycle, registerXValue);
                cycle++;

            } else {
                display.processPixel(cycle, registerXValue);
                cycle++;

                display.processPixel(cycle, registerXValue);
                cycle++;

                registerXValue = registerXValue + Integer.parseInt(lines[l].split(" ")[1], 10);
            }

        }
        display.print();
    }
}
