package de.emac.aoc;

import java.util.*;

public class D03 {

    public static void main(String[] args) {
        Util.tStart(0);
        String[] lines = Util.readFileLineByLine("d03.txt");
        v1(lines);
        v2(lines);
        Util.tEnd(0);
    }

    public static void v1(String[] lines) {
        int score = 0;
        for(String line: lines){
            int len = line.length()/2;
            HashSet<Character> same = Util.asCharSet(line.substring(0, len));
            same.retainAll(Util.asCharSet(line.substring(len)));
            if(!same.isEmpty()){
                for(Character c: same){
                    score = score + getScore(c);
                }
            }
        }
        System.out.println("res1: "+score);
    }

    public static void v2(String[] lines) {
        int score = 0;
        for(int i=0; i< lines.length; i=i+3){
            HashSet<Character> same = Util.asCharSet(lines[i]);
            same.retainAll(Util.asCharSet(lines[i+1]));
            same.retainAll(Util.asCharSet(lines[i+2]));
            if(!same.isEmpty()){
                for(Character c: same){
                    score = score + getScore(c);
                }
            }
        }
        System.out.println("res2: "+score);
    }

    private static int getScore(Character x) {
        char c = x.charValue();
        if(Character.isLowerCase(c)){
            return ((int) c) - 96;
        }else{
            return ((int) c) - 38;
        }
    }
}

