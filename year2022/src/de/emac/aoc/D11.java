package de.emac.aoc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.TreeMap;

public class D11 {

    private static class Monkey {
        ArrayList<BigInteger> items = new ArrayList();
        String opperation;
        BigInteger test;

        BigInteger op2Value;
        int trueTarget;
        int falseTarget;

        int inspectLevel = 0;

        public Monkey(String[] lines, int pos) {
            String[] nums = lines[pos].substring(lines[pos].indexOf(":") + 1).split(",");
            for (String aNum : nums) {
                items.add(BigInteger.valueOf(Integer.parseInt(aNum.trim(), 10)));
            }
            String[] op = lines[pos + 1].substring(lines[pos + 1].indexOf(" = ") + 3).trim().split(" ");
            opperation = op[1];
            if(!op[2].equals("old")){
                op2Value = BigInteger.valueOf(Integer.parseInt(op[2], 10));
            }else{
                op2Value = null;
            }
            test = BigInteger.valueOf(Integer.parseInt(lines[pos + 2].substring(lines[pos + 2].indexOf("by ") + 3), 10));
            trueTarget = Integer.parseInt(lines[pos + 3].substring(lines[pos + 3].indexOf("monkey ") + 7), 10);
            falseTarget = Integer.parseInt(lines[pos + 4].substring(lines[pos + 4].indexOf("monkey ") + 7), 10);
        }

        public void doMonkeyBusiness(BigInteger divider, TreeMap<Integer, Monkey> data) {
            for (BigInteger value : items) {
                BigInteger newValue;
                if (divider == null) {
                    newValue = calcNewValue(value).divide(BigInteger.valueOf(3l));
                } else {
                    //newValue = calcNewValue(val).divideAndRemainder(divider)[1];
                    newValue = calcNewValue(value);
                }
                if (newValue.divideAndRemainder(test)[1].equals(BigInteger.ZERO)) {
                    data.get(trueTarget).items.add(newValue);
                } else {
                    data.get(falseTarget).items.add(newValue);
                }
                inspectLevel++;
            }
            items = new ArrayList<>();
        }

        private BigInteger calcNewValue(BigInteger value) {
            BigInteger opValue = op2Value == null? value:op2Value;
            switch (opperation) {
                case "+":
                    value = value.add(opValue);
                    break;

                case "-":
                    value = value.subtract(opValue);
                    break;

                case "*":
                    value = value.multiply(opValue);
                    break;
            }
            return value;
        }

        private BigInteger getOpValue(String s, BigInteger value) {
            if (s.equals("old")) {
                return value;
            } else {
                return op2Value;
            }
        }
    }

    public static void main(String[] args) {
        Util.tStart(0);
        String[] lines = Util.readFileLineByLine("d11.txt");
        v1(genData(lines));
        v2(genData(lines));
        Util.tEnd(0);
    }

    private static TreeMap<Integer, Monkey> genData(String[] lines) {
        int monkeyNum = 0;
        TreeMap<Integer, Monkey> data = new TreeMap<>();
        for (int l = 0; l < lines.length; l++) {
            String line = lines[l];
            if (line.startsWith("Monkey ")) {
                Monkey m = new Monkey(lines, l + 1);
                l = l + 5;
                data.put(monkeyNum++, m);
            }
        }
        return data;
    }

    public static void v1(TreeMap<Integer, Monkey> data) {
        int rounds = 20;
        for (int i = 0; i < rounds; i++) {
            for (Integer key : data.keySet()) {
                data.get(key).doMonkeyBusiness(null, data);
            }
            System.out.println("Round " + (i + 1));
            dump(data);
        }
        System.out.println("Res 1: " + calcRes(data));
    }

    public static void v2(TreeMap<Integer, Monkey> data) {
        Util.tStart(1);
        BigInteger magicDivisor = BigInteger.ONE;
        for (Integer key : data.keySet()) {
            magicDivisor = magicDivisor.multiply(data.get(key).test);
        }
        int rounds = 10000;
        for (int i = 0; i < rounds; i++) {
            for (Integer key : data.keySet()) {
                data.get(key).doMonkeyBusiness(magicDivisor, data);
            }
            //if ((i + 1) % 10 == 0) {
                Util.tEnd(1, "Round " + (i + 1));
                //dump2(data);
                Util.tStart(1);
            //}
        }
        System.out.println("Res 2: " + calcRes(data));
    }

    private static long calcRes(TreeMap<Integer, Monkey> data) {
        TreeMap<Long, Monkey> ordered = new TreeMap<>();
        for (Integer key : data.keySet()) {
            Monkey m = data.get(key);
            ordered.put((long) m.inspectLevel, m);
        }
        long top = ordered.lastKey();
        ordered.remove(top);
        long sec = ordered.lastKey();
        return top * sec;
    }

    private static void dump(TreeMap<Integer, Monkey> data) {
        for (Integer key : data.keySet()) {
            Monkey m = data.get(key);
            System.out.println(key + ": " + m.inspectLevel + " " + m.items);
        }
    }

    private static void dump2(TreeMap<Integer, Monkey> data) {
        for (Integer key : data.keySet()) {
            Monkey m = data.get(key);
            System.out.println(key + ": " + m.inspectLevel);
        }
    }
}

