package de.emac.aoc;

import java.util.ArrayList;

public class D01 {

    public static void main(String[] args) {
        Util.tStart(0);
        String[] lines = Util.readFileLineByLine("d01.txt");
        ArrayList<Integer> nums = new ArrayList<>(lines.length);
        for (String line: lines){
            nums.add(Integer.parseInt(line));
        }
        v1(nums);
        v2(nums);
        Util.tEnd(0);
    }

    public static void v1(ArrayList<Integer> nums) {
        for(int i: nums){
            int numToFind = 2020 - i;
            if(nums.contains(numToFind)){
                System.out.println("res1: "+ (i * numToFind));
                break;
            }
        }
    }

    public static void v2(ArrayList<Integer> nums) {
        for(int i: nums){
            int sumToFind = 2020 - i;
            for(int j: nums) {
                int numToFind = sumToFind - j;
                if (nums.contains(numToFind)) {
                    System.out.println("res2: " + (j * i * numToFind));
                    break;
                }
            }
        }
    }
}

