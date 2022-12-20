package de.emac.aoc;

import java.util.ArrayList;
import java.util.List;

public class D15 {
    //static long xMin = Long.MAX_VALUE;
    //static long xMax = Long.MIN_VALUE;

    public static void main(String[] args) {
        //long searchLine = 10l;
        //String[] lines = Util.readFileLineByLine("d15a.txt");

        long searchLine = 2_000_000l;
        String[] lines = Util.readFileLineByLine("d15.txt");

        ArrayList<Pack> packs = new ArrayList<>(lines.length);
        for(int l=0; l< lines.length; l++) {
            String[] p = lines[l].split(":");
            Pos s = getPos(p[0]);
            Pos b = getPos(p[1]);
            packs.add(new Pack(s, b));
        }
        //System.out.println(xMin + "|" + xMax);
        Util.tStart(0);
        Util.tEnd(0, "Res1: " + v1(packs, searchLine));

        Util.tStart(0);
        Pos c = v2(packs, 0, 4_000_000);
        Util.tEnd(0, "Res2: " + ((4_000_000 * c.x) + c.y));
    }

    private static Pos getPos(String s) {
        String[] parts  = s.split("=");
        long x = Long.parseLong(parts[1].substring(0, parts[1].indexOf(',')), 10);
        //xMin = Math.min(xMin, x);
        //xMax = Math.max(xMax, x);
        return new Pos(x, Long.parseLong(parts[2],10));
    }

    private static int v1(List<Pack> packs, long searchLine){
        int result = 0;

        Pack startPack = null;
        for(Pack pair: packs){
            if(pair.beacon.y == searchLine){
                startPack = pair;
                break;
            }
        }

        long distance = 1;
        boolean found = true;

        while (found) {
            found = false;
            //if(xMin <= (startPack.beacon.x - distance) + startPack.scanRange()) {
            Pos left = new Pos(startPack.beacon.x - distance, searchLine);
            if (isPositionInRangeOfAnyScanner(left, packs)) {
                found = true;
                result++;
            }
            //}
            //if(xMax >= (startPack.beacon.x + distance) - startPack.scanRange()) {
            Pos right = new Pos(startPack.beacon.x + distance, searchLine);
            if (isPositionInRangeOfAnyScanner(right, packs)) {
                found = true;
                result++;
            }
            //}
            distance++;
        }
        return result;
    }

    private static Pos v2(List<Pack> packs, long minRange, long maxRange) {
        for (Pack pair : packs) {
            long outOfRange = pair.scanRange() + 1;
            Pos sensor = pair.sensor;
            for (int d = 0; d <= outOfRange; ++d) {
                Pos checkPos = new Pos(sensor.x - outOfRange + d, sensor.y + d);
                if(outOfRange(checkPos, minRange, maxRange, packs)){
                    return checkPos;
                }
                if(outOfRange(checkPos, minRange, maxRange, packs)){
                    return checkPos;
                }
                checkPos = new Pos(sensor.x - outOfRange + d, sensor.y - d);
                if(outOfRange(checkPos, minRange, maxRange, packs)){
                    return checkPos;
                }
                checkPos = new Pos(sensor.x + outOfRange - d, sensor.y + d);
                if(outOfRange(checkPos, minRange, maxRange, packs)){
                    return checkPos;
                }
                checkPos = new Pos(sensor.x + outOfRange - d, sensor.y - d);
                if(outOfRange(checkPos, minRange, maxRange, packs)){
                    return checkPos;
                }
            }
        }
        return null;
    }

    private static boolean outOfRange(Pos checkPos, long minRange, long maxRange, List<Pack> pairs) {
        return  minRange <= checkPos.x &&
                checkPos.x <= maxRange &&
                minRange <= checkPos.y &&
                checkPos.y <= maxRange &&
                !isPositionInRangeOfAnyScanner(checkPos, pairs);
    }

    private static boolean isPositionInRangeOfAnyScanner(Pos pos, List<Pack> packs) {
        for(Pack p: packs){
            if(p.sensor.dist(pos) <= p.scanRange()){
                return true;
            }
        }
        return false;
    }

    private record Pos(long x, long y) {

        long dist(Pos p) {
            return Math.abs(x - p.x) + Math.abs(y - p.y);
        }
    }

    private record Pack(Pos sensor, Pos beacon) {
        long scanRange() {
            return sensor.dist(beacon);
        }
    }
}
