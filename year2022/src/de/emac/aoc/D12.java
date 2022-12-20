package de.emac.aoc;

import de.emac.aoc.objects.Pos;

import java.util.*;

public class D12 {

    public static void main(String[] args) {
        String[] lines = Util.readFileLineByLine("d12.txt");
        int[][] heights = new int[lines.length][];
        Pos start = null;
        Pos end = null;
        ArrayList<Pos> v2Starts = new ArrayList<>();
        for(int y=0; y < lines.length; y++){
            String aLine = lines[y];
            heights[y] = new int[aLine.length()];
            char[] chars = aLine.toCharArray();
            for(int x=0; x<chars.length; x++){
                if(chars[x] == 'S') {
                    start = new Pos(x, y);
                    heights[y][x] = 0;
                } else if(chars[x] == 'E') {
                    end = new Pos(x, y);
                    heights[y][x] = 'z' - 'a';
                } else {
                    heights[y][x] = chars[x] - 'a';
                }

                // collecting v2 points as well..
                if(heights[y][x]==0){
                    v2Starts.add(new Pos(x,y));
                }
            }
        }
        MyMap map = new MyMap(heights, end);

        Util.tStart(0);
        v1(map, start);
        Util.tEnd(0);

        Util.tStart(0);
        v2(map, v2Starts);
        Util.tEnd(0);
    }

    public static void v1(MyMap map, Pos start) {
        final var result = map.getPathLength(start);
        System.out.println("Res1: " + result);
    }

    public static void v2(MyMap map, ArrayList<Pos> starts) {
        System.out.println("Checking: "+starts.size()+" START points...");
        TreeMap<Integer, ArrayList<Pos>> res = new TreeMap<>();
        for(Pos aStart : starts) {
            int len = map.getPathLength(aStart);
            ArrayList<Pos> aList = res.get(len);
            if(aList == null){
                aList = new ArrayList<>();
                res.put(len, aList);
            }
            aList.add(aStart);

        }
        System.out.println("Res2: " + res.firstKey());
    }

    private static class MyMap {

        private final int[][] heights;
        private final Pos end;

        public MyMap(int[][] data, Pos end) {
            this.heights = data;
            this.end = end;
        }

        public int getPathLength(Pos start) {
            HashMap<Pos, Integer> cheapestCostToPosition = new HashMap();
            cheapestCostToPosition.put(start, 0);

            final HashMap<Pos, Integer> distanceToTarget = new HashMap();
            distanceToTarget.put(start, getPosDistance(start, end));

            PriorityQueue positionsToCheckOrderedByDistanceToTarget = new PriorityQueue<Pos>((p1, p2) -> {
                Integer pos1Score = distanceToTarget.getOrDefault(p1, Integer.MAX_VALUE);
                Integer pos2Score = distanceToTarget.getOrDefault(p2, Integer.MAX_VALUE);
                return pos1Score.compareTo(pos2Score);
            });

            positionsToCheckOrderedByDistanceToTarget.add(start);
            while (!positionsToCheckOrderedByDistanceToTarget.isEmpty()) {
                final Pos posToCheck = (Pos) positionsToCheckOrderedByDistanceToTarget.remove();
                if (posToCheck.equals(end)) {
                    // we have found finally our END...
                    return cheapestCostToPosition.get(posToCheck);
                }

                final int nextMoveCosts = cheapestCostToPosition.get(posToCheck) + 1;
                for (Pos possibleMove : getPossibleMoves(posToCheck)) {
                    if (nextMoveCosts < cheapestCostToPosition.getOrDefault(possibleMove, Integer.MAX_VALUE)) {

                        cheapestCostToPosition.put(possibleMove, nextMoveCosts);
                        distanceToTarget.put(possibleMove, nextMoveCosts + getPosDistance(possibleMove, end));

                        if (!positionsToCheckOrderedByDistanceToTarget.contains(possibleMove)) {
                            positionsToCheckOrderedByDistanceToTarget.add(possibleMove);
                        }
                    }
                }
            }
            return Integer.MAX_VALUE;
        }

        private ArrayList<Pos> getPossibleMoves(final Pos pos) {
            ArrayList<Pos> list = new ArrayList(4);
            int curHeightPlusOne = heights[pos.y()][pos.x()] + 1;

            if (pos.x() > 0) {
                Pos left = new Pos(pos.x() - 1, pos.y());
                if (curHeightPlusOne >= heights[left.y()][left.x()]) {
                    list.add(left);
                }
            }
            if (pos.x() < heights[pos.y()].length - 1) {
                Pos right = new Pos(pos.x() + 1, pos.y());
                if (curHeightPlusOne >= heights[right.y()][right.x()]) {
                    list.add(right);
                }
            }
            if (pos.y() > 0) {
                Pos up = new Pos(pos.x(), pos.y() - 1);
                if (curHeightPlusOne >= heights[up.y()][up.x()]) {
                    list.add(up);
                }
            }

            if (pos.y() < heights.length - 1) {
                Pos down = new Pos(pos.x(), pos.y() + 1);
                if (curHeightPlusOne >= heights[down.y()][down.x()]) {
                    list.add(down);
                }
            }
            return list;
        }

        private int getPosDistance(final Pos p1, final Pos p2) {
            return (int) Math.sqrt(Math.pow(p1.x() - p2.x(), 2d) + Math.pow(p1.y() - p2.y(), 2d));
        }
    }
}

