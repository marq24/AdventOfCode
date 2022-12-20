package de.emac.aoc;

import java.util.ArrayList;

public class D02 {

    private static class P {
        char c;
        int min;
        int max;
        String word;

        public P(String[] s) {
            // 15-19 k: kkkkkkkkkkkkzkkkkkkk
            c = s[1].charAt(0);
            String[] minMax = s[0].split("-");
            min = Integer.parseInt(minMax[0]);
            max = Integer.parseInt(minMax[1]);
            word = s[2].trim();
        }

        public boolean isValid(){
            /*int count = 0;
            for(int i=0; i< word.length(); i++){
                if(word.charAt(i)==c){
                    count++;
                }
            }*/
            int count = word.length() - word.replace((CharSequence) (c+""), "").length();
            return count >= min && count <= max;
        }
        public boolean isValid2(){
            boolean t1 = word.charAt(min - 1)==c;
            boolean t2 = word.charAt(max - 1)==c;
            return t1 && !t2 || !t1 && t2;
        }
    }

    public static void main(String[] args) {
        Util.tStart(0);
        String[] lines = Util.readFileLineByLine("d02.txt");
        ArrayList<P> p = new ArrayList<>(lines.length);
        for (String line: lines){
            String[] s = line.split(" ");
            p.add(new P(s));
        }
        v1(p);
        v2(p);
        Util.tEnd(0);
    }

    public static void v1(ArrayList<P> pwds) {
        int validCount =0;
        for(P i: pwds){
            if(i.isValid()){
                validCount++;
            }
        }
        System.out.println("res1: " +validCount);
    }

    public static void v2(ArrayList<P> pwds) {
        int validCount =0;
        for(P i: pwds){
            if(i.isValid2()){
                validCount++;
            }
        }
        System.out.println("res1: " +validCount);
    }
}
