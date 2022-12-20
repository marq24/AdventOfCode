package de.emac.aoc;

public class D08 {

    public static void main(String[] args) {
        Util.tStart(0);
        String[] lines = Util.readFileLineByLine("d08a.txt");
        v1(lines);
        v2(lines);
        Util.tEnd(0);
    }

    public static void v1(String[] lines) {
        int result = 0;
        for(int l=0; l< lines.length; l++){
            String line = lines[l];
        }
        System.out.println("res1: "+result);
    }

    public static void v2(String[] lines) {
        int result = 0;
        for(int l=0; l< lines.length; l++){
            String line = lines[l];
        }
        System.out.println("res2: "+result);
    }
}

