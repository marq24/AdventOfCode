package de.emac.aoc;

import java.math.BigInteger;

public class D03 {

    public static void main(String[] args) {
        Util.tStart(0);
        String[] lines = Util.readFileLineByLine("d03.txt");
        v1(lines);
        int a = v2(lines, 1, 1);
        int b = v2(lines, 3, 1);
        int c = v2(lines, 5, 1);
        int d = v2(lines, 7, 1);
        int e = v2(lines, 1, 2);
        System.out.println("res2: " + BigInteger.valueOf(a).multiply(BigInteger.valueOf(b)).multiply(BigInteger.valueOf(c)).multiply(BigInteger.valueOf(d)).multiply(BigInteger.valueOf(e)));
        Util.tEnd(0);
    }

    public static void v1(String[] lines) {
        int treeCount = 0;
        int pos = 0;
        for (String line: lines){
            if(line.charAt(pos) == '#'){
                treeCount++;
            }
            pos = (pos + 3) % (line.trim().length());
        }
        System.out.println("res1: " +treeCount);
    }

    public static int v2(String[] lines, int right, int down) {
        int treeCount = 0;
        int pos = 0;
        for (int i=0; i<lines.length; i = i + down){
            if(lines[i].charAt(pos) == '#'){
                treeCount++;
            }
            pos = (pos + right) % (lines[i].trim().length());
        }
        return treeCount;
    }
}
