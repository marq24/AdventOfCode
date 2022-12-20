package de.emac.aoc;

import java.util.ArrayList;
import java.util.HashSet;

public class D09 {

    public static void main(String[] args) {
        Util.tStart(0);
        String[] lines = Util.readFileLineByLine("d09.txt");
        solve(lines, genRope(2));
        solve(lines, genRope(10));
        Util.tEnd(0);
    }

    private static ArrayList<int[]> genRope(int size) {
        ArrayList<int[]> rope = new ArrayList<>();
        for(int i=0; i<size; i++){
            rope.add(new int[]{0,0});
        }
        return rope;
    }

    public static void solve(String[] lines, ArrayList<int[]> rope) {
        HashSet<String> visitedPositions = new HashSet<>();
        visitedPositions.add(rope.get(rope.size()-1)[0]+"/"+rope.get(rope.size()-1)[1]);
        for(int l=0; l< lines.length; l++){
            String line = lines[l];
            String[] parts = line.split(" ");
            int steps = Integer.parseInt(parts[1], 10);
            switch (parts[0].trim().toUpperCase()) {
                case "R":
                    adjustKnotPositions(steps, rope, +1, 0, visitedPositions);
                    break;
                case "L":
                    adjustKnotPositions(steps, rope, -1, 0, visitedPositions);
                    break;
                case "U":
                    adjustKnotPositions(steps, rope, 0, +1, visitedPositions);
                    break;
                case "D":
                    adjustKnotPositions(steps, rope, 0, -1, visitedPositions);
                    break;
            }
        }
        System.out.println("res for "+rope.size()+": "+visitedPositions.size());
    }

    private static void adjustKnotPositions(int steps, ArrayList<int[]> rope, int x, int y, HashSet<String> data) {
        for (int i=0; i < steps; i++){
            rope.get(0)[0] = rope.get(0)[0] + x;
            rope.get(0)[1] = rope.get(0)[1] + y;

            for(int p=0; p < rope.size()-1 ; p++){
                int leadingKnotPos[] = rope.get(p);
                int followingKnotPos[] = rope.get(p+1);

                boolean isSameXaxis = false;
                boolean movedOnXaxis = false;
                boolean isSameYaxis = false;
                boolean movedOnYaxis = false;

                // deal with X-Axis
                if (followingKnotPos[0] == leadingKnotPos[0]) {
                    // nothing to do on X-Axis
                    isSameXaxis = true;
                } else if (followingKnotPos[0] + 2 == leadingKnotPos[0]) {
                    // we are two left... need to move one RIGHT
                    followingKnotPos[0] = followingKnotPos[0] + 1;
                    movedOnXaxis = true;
                } else if (followingKnotPos[0] - 2 == leadingKnotPos[0]) {
                    // we are two right... need to move one LEFT
                    followingKnotPos[0] = followingKnotPos[0] - 1;
                    movedOnXaxis = true;
                }

                // deal with Y-Axis
                if (followingKnotPos[1] == leadingKnotPos[1]) {
                    // nothing to do on Y-Axis
                    isSameYaxis = true;
                } else if (followingKnotPos[1] + 2 == leadingKnotPos[1]) {
                    // we are two down... need to move one UP
                    followingKnotPos[1] = followingKnotPos[1] + 1;
                    movedOnYaxis = true;
                } else if (followingKnotPos[1] - 2 == leadingKnotPos[1]) {
                    // we are two up... need to move one DOWN
                    followingKnotPos[1] = followingKnotPos[1] - 1;
                    movedOnYaxis = true;
                }

                // check if we need to move diagonal...
                if(!isSameXaxis && !isSameYaxis) {
                    if(movedOnXaxis && !movedOnYaxis){
                        // need to jump up/down
                        followingKnotPos[1] = leadingKnotPos[1];
                    } else if(movedOnYaxis && !movedOnXaxis){
                        // need to jump left/right
                        followingKnotPos[0] = leadingKnotPos[0];
                    }
                }
            }
            data.add(rope.get(rope.size()-1)[0] + "/" + rope.get(rope.size()-1)[1]);
        }
    }
}

