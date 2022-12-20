package de.emac.aoc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.TreeMap;

public class D01 {

    public static void main(String[] args) {
        Util.tStart(0);
        v1();
        Util.tEnd(0);
    }

    public static void v1() {
        String[] lines = Util.readFileLineByLine("d01.txt");
        TreeMap<String, ArrayList<Integer>> res = new TreeMap<>();
        int count = 0;
        BigInteger curCals = BigInteger.valueOf(0l);
        String equalCalsKey = null;
        for (String line : lines) {
            if(line.length() > 0) {
                curCals = curCals.add(BigInteger.valueOf(Integer.parseInt(line)));
            } else {
                String key = curCals.toString();
                ArrayList<Integer> list;
                if(res.containsKey(key)) {
                    list = res.get(key);
                    equalCalsKey = key;
                }else{
                    list = new ArrayList<>();
                }
                list.add(count);
                res.put(key, list);

                curCals = BigInteger.valueOf(0l);
                count++;
            }
        }

        String top1 = res.lastKey();
        System.out.println("TOP1: "+top1);

        String top2 = res.floorKey(Integer.parseInt(top1)-1+"");
        String top3 = res.floorKey(Integer.parseInt(top2)-1+"");
        System.out.println("TOP1-3: "+BigInteger.valueOf(Long.parseLong(top1)).add(BigInteger.valueOf(Long.parseLong(top2))).add(BigInteger.valueOf(Long.parseLong(top3))));

    }
}

