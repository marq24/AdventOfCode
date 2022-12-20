package de.emac.aoc;

import java.util.ArrayList;

public class D04 {

    public static void main(String[] args) {
        Util.tStart(0);
        String[] lines = Util.readFileLineByLine("d04.txt");
        ArrayList<ArrayList<Integer>[]> data = new ArrayList<>();
        for(int i=0; i< lines.length; i++){
            String[] p = lines[i].split(",");
            ArrayList<Integer> p1 = parseSection(p[0]);
            ArrayList<Integer> p2 = parseSection(p[1]);
            data.add(new ArrayList[]{p1, p2});
        }

        v1(data);
        v2(data);
        Util.tEnd(0);
    }

    private static ArrayList<Integer> parseSection(String s) {
        ArrayList<Integer> ret = new ArrayList<>();
        String[] minMax = s.split("-");
        int min = Integer.parseInt(minMax[0]);
        int max = Integer.parseInt(minMax[1]);
        for(int i=min; i<=max; i++){
            ret.add(i);
        }
        return  ret;
    }

    public static void v1(ArrayList<ArrayList<Integer>[]> lines) {
        int score = 0;
        for(int i=0; i < lines.size(); i++){
            ArrayList<Integer> p1 = lines.get(i)[0];
            ArrayList<Integer> p2 = lines.get(i)[1];
            if(p1.containsAll(p2) || p2.containsAll(p1)){
                score++;
            }
        }
        System.out.println("res1: "+score);
    }

    public static void v2(ArrayList<ArrayList<Integer>[]> lines) {
        int score = 0;
        for(int i=0; i < lines.size(); i++){
            ArrayList<Integer> p1 = lines.get(i)[0];
            ArrayList<Integer> p2 = lines.get(i)[1];
            for(Integer j: p1){
                if(p2.contains(j)){
                    score++;
                    break;
                }
            }
        }
        System.out.println("res2: "+score);
    }

}

