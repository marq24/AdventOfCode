package de.emac.aoc;

import java.util.*;

public class D14 {

    static int minX = Integer.MAX_VALUE;
    static int maxX = Integer.MIN_VALUE;
    static int maxY = Integer.MIN_VALUE;
    static int minY = Integer.MAX_VALUE;

    public static void main(String[] args) {
        HashMap<Location, Character> cave = new HashMap();
        String[] lines = Util.readFileLineByLine("d14.txt");
        for(int l=0; l< lines.length; l++){
            ArrayList<Location> rockParts = new ArrayList<>();
            for(String aRockSection: lines[l].split(" -> ")){
                String[] xy = aRockSection.split(",");
                rockParts.add(new Location(Integer.parseInt(xy[0]), Integer.parseInt(xy[1])));
            }
            Location prev = rockParts.get(0);
            for (var i = 1; i < rockParts.size(); i++) {
                Location next = rockParts.get(i);
                if (prev.x == next.x) {
                    int x = prev.x;
                    for (int y = Math.min(prev.y, next.y); y <= Math.max(prev.y, next.y); y++) {
                        updateMinMax(x,y);
                        cave.put(new Location(x, y), '#');
                    }
                } else {
                    int y = prev.y;
                    for (int x = Math.min(prev.x, next.x); x <= Math.max(prev.x, next.x); x++) {
                        updateMinMax(x,y);
                        cave.put(new Location(x, y), '#');
                    }
                }
                prev = next;
            }
        }

        Util.tStart(0);
        v1(cave);
        Util.tEnd(0);

        Util.tStart(0);
        v2(cave);
        Util.tEnd(0);
    }

    private static void updateMinMax(int x,int y){
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

    private static void v1(Map<Location, Character> cave) {
        boolean isInCaveBounds = true;
        while (isInCaveBounds) {
            Location curSandPos = new Location(500, 0);
            while (!cave.containsKey(curSandPos) && isInCaveBounds) {
                Location nextSandPos = curSandPos.getDown();
                if (cave.containsKey(nextSandPos)) {
                    nextSandPos = curSandPos.getLeft();
                    if (cave.containsKey(nextSandPos)) {
                        nextSandPos = curSandPos.getRight();
                    }
                }
                if (cave.containsKey(nextSandPos)) {
                    // sand can not move further (all next pos are blocked) - let's take new
                    // sand...
                    cave.put(curSandPos, 'o');
                } else {
                    curSandPos = nextSandPos;
                    if (curSandPos.x < minX || curSandPos.x > maxX || curSandPos.y > maxY) {
                        isInCaveBounds = false;
                    }
                }
            }
        }

        System.out.println("res1: "+countSand(cave));
    }

    private static void v2(Map<Location, Character> cave) {
        int lastFloor = maxY + 1;
        Location source = new Location(500, 0);

        while (!cave.containsKey(source)) {
            Location curSandPos = new Location(500, 0);
            while (!cave.containsKey(curSandPos)) {
                Location nextSandPos = curSandPos.getDown();
                if (cave.containsKey(nextSandPos)) {
                    nextSandPos = curSandPos.getLeft();
                    if (cave.containsKey(nextSandPos)) {
                        nextSandPos = curSandPos.getRight();
                    }
                }
                if (cave.containsKey(nextSandPos)) {
                    cave.put(curSandPos, 'o');
                } else {
                    curSandPos = nextSandPos;
                    if (curSandPos.y == lastFloor) {
                        cave.put(curSandPos, 'o');
                    }
                }
            }
        }
        System.out.println("res2: "+countSand(cave));
    }

    private static int countSand(Map<Location, Character> cave){
        int result = 0;
        for(Character c: cave.values()){
            if(c == 'o'){
                result++;
            }
        }
        return result;
    }

    private record Location(int x, int y) {
        Location getDown() {
            return new Location(x, y + 1);
        }

        Location getLeft() {
            return new Location(x - 1, y + 1);
        }

        Location getRight() {
            return new Location(x + 1, y + 1);
        }
    }
}

