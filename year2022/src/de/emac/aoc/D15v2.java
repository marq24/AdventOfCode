package de.emac.aoc;

import java.util.*;

public class D15v2 {

    static long minX = Long.MAX_VALUE;
    static long maxX = Long.MIN_VALUE;
    static long maxY = Long.MIN_VALUE;
    static long minY = Long.MAX_VALUE;

    public static void main(String[] args) {
        Util.tStart(0);
        String[] lines = Util.readFileLineByLine("d15a.txt");

        HashMap<Pos, Character> cave = new HashMap();

        ArrayList<Pos> sensors = new ArrayList<>(lines.length);
        ArrayList<Pos> beacons = new ArrayList<>(lines.length);
        for(int l=0; l< lines.length; l++){
            String line = lines[l];
            String[] p = line.split(":");
            Pos s = getPos(p[0]);
            Pos b = getPos(p[1]);
            //cave.put(s, 'S');
            //cave.put(b, 'B');
            long xDelta = Math.abs(s.x() - b.x());
            long yDelta = Math.abs(s.y() - b.y());
            long max = xDelta+yDelta;
            fill(cave, s, max);
        }

        for(Pos aCavePos: cave.keySet()){
            updateMinMax(aCavePos.x(), aCavePos.y());
        }

        int count = 0;
        int y = 10;
        for(long i=minX; i<=maxX; i++){
            if(cave.containsKey(new Pos(i, y))){
                count++;
            }
        }
        System.out.println("count "+count);
        Util.tEnd(0);
    }

    private static void fill(Map<Pos, Character> cave, Pos s, long count) {
        /*
        Pos(s.x - count, s.y);

        Pos(s.x - count + 1 , s.y);
        Pos(s.x - count + 1, s.y + 1);
        Pos(s.x - count + 1, s.y - 1);

        Pos(s.x - count + 2, s.y);
        Pos(s.x - count + 2, s.y + 1);
        Pos(s.x - count + 2, s.y - 1);
        Pos(s.x - count + 2, s.y + 2);
        Pos(s.x - count + 2, s.y - 2);

        Pos(s.x - count + 3);
        Pos(s.x - count + 3, s.y + 1);
        Pos(s.x - count + 3, s.y - 1);
        Pos(s.x - count + 3, s.y + 2);
        Pos(s.x - count + 3, s.y - 2);
        Pos(s.x - count + 3, s.y + 3);
        Pos(s.x - count + 3, s.y - 3);
        */

        /*int count2 = 0;
        for(int i=0; i<count; i++){
            cave.put( new Pos(s.x() - count + i, s.y()), '#');
            for(int j=1; j<=count2; j++){
                cave.put( new Pos(s.x() - count + i, s.y()+j), '#');
                cave.put( new Pos(s.x() - count + i, s.y()-j), '#');
            }
            count2++;
        }
        count2++;
        for(int i=0; i<=count; i++){
            cave.put( new Pos(s.x() + count + i, s.y()), '#');
            for(int j=1; j<=count2; j++){
                cave.put( new Pos(s.x() + count + i, s.y()+j), '#');
                cave.put( new Pos(s.x() + count + i, s.y()-j), '#');
            }
            count2--;
        }*/
        Set<Pos> result = new HashSet<>();
        Queue<Pos> queue = new ArrayDeque<>();
        queue.add(s);
        result.add(s);

        for(int i=0; i<count; ++i) {
            ArrayList<Pos> newValues = new ArrayList<>();
            queue.forEach(c->{
                c.getSourounding().forEach(nc->{
                    if(result.add(nc)) {
                        newValues.add(nc);
                    }
                });
            });
            queue = new ArrayDeque<>(newValues);
        }

        for (Pos aPos: result){
            cave.put(aPos, '#');
        }
    }

    private static final record Pos(long x, long y) {
        List<Pos> getSourounding() {
            return List.of(new Pos(x, y + 1), new Pos(x + 1, y), new Pos(x, y - 1), new Pos(x - 1, y));
        }

        long distance(Pos other) {
            return Math.abs(x-other.x) + Math.abs(y-other.y);
        }
    }

    private static void updateMinMax(long x,long y){
        if (x < minX) {
            minX = x;
        }
        if (x > maxX) {
            maxX = x;
        }
        if (y > maxY) {
            maxY = y;
        }
        if (y < minY) {
            minY = y;
        }
    }

    private static Pos getPos(String s) {
        String[] parts  = s.split("=");
        return new Pos(Integer.parseInt(parts[1].substring(0, parts[1].indexOf(',')), 10), Integer.parseInt(parts[2],10));
    }

    public static void v1(String[] lines) {
        int result = 0;
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


