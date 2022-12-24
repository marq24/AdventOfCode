package de.emac.aoc;

import java.util.*;

public class D24 {
    public static int maxX;
    public static int maxY;
    public static int minX;
    public static int minY;
    public static Pos start;
    public static Pos finish;

    public static void main(String[] args) {
        String[] lines = INPUT.split("\n");
        minY = 1;
        minX = 1;
        maxY = lines.length - 2;
        maxX = lines[0].length() - 2;
        start = new Pos(1, 0);
        finish = new Pos(maxX, maxY + 1);
        Util.tStart(0);
        Util.tEnd(0, "Res1: "+ v1(readMapData(lines))+ " (269)");
        Util.tStart(0);
        Util.tEnd(0, "Res2: "+ v2(readMapData(lines))+ " (825)");
    }

    public static ArrayList<MapAndBliz> readMapData(String[] lines) {
        HashSet<Pos> map = new HashSet();
        ArrayList<Bliz> bliz = new ArrayList<>();
        for (int y = minY; y <= maxY; y++) {
            char[] chars = lines[y].toCharArray();
            for (int x = minX; x <= maxX; x++) {
                if (chars[x] != '.') {
                    Bliz aBliz = new Bliz(new Pos(x, y), chars[x]);
                    map.add(aBliz.pos);
                    bliz.add(aBliz);
                }
            }
        }
        ArrayList<MapAndBliz> mapList = new ArrayList<>();
        mapList.add(new MapAndBliz(map, bliz));
        return mapList;
    }

    private static int v1(ArrayList<MapAndBliz> mapList) {
        return solveIt(mapList, new MapPosState(0, start), finish).minute;
    }

    private static int v2(ArrayList<MapAndBliz> mapList) {
        MapPosState startState = new MapPosState(0, start);
        MapPosState out = solveIt(mapList, startState, finish);
        MapPosState back = solveIt(mapList, out, start);
        MapPosState end = solveIt(mapList, back, finish);
        return end.minute;
    }

    private static MapPosState solveIt(ArrayList<MapAndBliz> mapList, MapPosState initial, Pos finish) {
        Queue<MapPosState> workList = new ArrayDeque<>();
        HashSet<MapPosState> visited = new HashSet<>();
        workList.add(initial);
        visited.add(initial);

        while (!workList.isEmpty()) {
            MapPosState curMapState = workList.poll();
            // in order to avoid to calculate the nextMapState in each Minute of time...
            MapAndBliz aBlizMapAtCurrMinute = getBlizAndMapAtMinute(curMapState.minute, mapList);
            if (aBlizMapAtCurrMinute.canIMoveToPos(curMapState.pos)) {
                if (finish.equals(curMapState.pos)) {
                    // finally we have reached the goal...
                    return curMapState;
                } else {
                    MapPosState nextMinuteCurPosState = new MapPosState(curMapState.minute + 1, curMapState.pos);
                    if (!visited.contains(nextMinuteCurPosState)) {
                        visited.add(nextMinuteCurPosState);
                        workList.add(nextMinuteCurPosState);
                    }
                    for(Pos aPos: curMapState.pos.getNeighbours()){
                        MapPosState nextMinuteNeighbourPosState = new MapPosState(curMapState.minute + 1, aPos);
                        if (!visited.contains(nextMinuteNeighbourPosState)) {
                            visited.add(nextMinuteNeighbourPosState);
                            workList.add(nextMinuteNeighbourPosState);
                        }
                    }
                }
            }
        }
        return null;
    }

    private static MapAndBliz getBlizAndMapAtMinute(int minute, ArrayList<MapAndBliz> blizAndMaps) {
        int len = blizAndMaps.size();
        if (minute < len) {
            return blizAndMaps.get(minute);
        } else {
            MapAndBliz lastBlizAndMap = blizAndMaps.get(len - 1);
            while (blizAndMaps.size() <= minute) {
                blizAndMaps.add(lastBlizAndMap.calcNextMap());
            }
            return blizAndMaps.get(minute);
        }
    }

    private record MapPosState(int minute, Pos pos) {
    }

    private record MapAndBliz(HashSet map, ArrayList<Bliz> bliz) {

        MapAndBliz calcNextMap() {
            ArrayList<Bliz> newBliz = new ArrayList<>(bliz.size());
            for(Bliz aBliz: bliz){
                newBliz.add( aBliz.getNextPos() );
            }
            HashSet<Pos> newMap = new HashSet<>();
            for(Bliz aNewBliz : newBliz){
                newMap.add(aNewBliz.pos);
            }
            return new MapAndBliz(newMap, newBliz);
        }

        boolean canIMoveToPos(Pos pos) {
            return  pos.equals(start) || pos.equals(finish) ||
                    !map.contains(pos) && pos.x <= maxX && pos.y <= maxY && pos.x >= minX && pos.y >= minY;
        }
    }

    private record Pos(int x, int y) {
        List<Pos> getNeighbours() {
            return List.of(new Pos(x - 1, y), new Pos(x + 1, y), new Pos(x, y + 1), new Pos(x, y - 1));
        }
    }

    private record Bliz(Pos pos, char course) {
        public Bliz getNextPos() {
           switch (course) {
                case 'v':
                    return checkIfPosIsValidOrRespawn(new Pos(pos.x, pos.y + 1));
                case '^':
                    return checkIfPosIsValidOrRespawn(new Pos(pos.x, pos.y - 1));
                case '>':
                    return checkIfPosIsValidOrRespawn(new Pos(pos.x + 1, pos.y));
                case '<':
                    return checkIfPosIsValidOrRespawn(new Pos(pos.x - 1, pos.y));
            }
            return null;
        }

        private Bliz checkIfPosIsValidOrRespawn(Pos next) {
            if (minX <= next.x && maxX >= next.x && minY <= next.y && maxY >= next.y
            ) {
                return new Bliz(next, course);
            } else {
                switch (course) {
                    case 'v':
                        return new Bliz(new Pos(pos.x, minY), course);
                    case '^':
                        return new Bliz(new Pos(pos.x, maxY), course);
                    case '>':
                        return new Bliz(new Pos(minX, pos.y), course);
                    case '<':
                        return new Bliz(new Pos(maxX, pos.y), course);
                }
            }
            return null;
        }
    }

    private static final String INTEST = """
            #.######
            #>>.<^<#
            #.<..<<#
            #>v.><>#
            #<^v^^>#
            ######.#""";

    private static final String INPUT = """
            #.########################################################################################################################
            #.v^<>^^.<^^<v>v>>>.<^<<v.^>vv>>^<<<v>.v<^v^<v<<^<^^<^<^v><^vv.v^^.>^><vv><^<.<^v<>.v<v..^>><v>.vvv.v>^^<^>.<<^<^<^>v.^^<#
            #.^^<v>><<v><vv<>..<>v<^v><<>><>v.>v.^^<<.v.^<^<vv><v<<v><<^<>^v>v>><>v..v^<^.v><>^>><v<<>v^<<v<v>v.v<v>><<v>^v<v^...<<<>#
            #><vv>>v>v<^>^^^^vv^<v^<<v<>^<^.^^>^<^vv>^v>><<^^<.>^>v<^v<.v.^<^^v.<.>.<^>v^>.v>>^vv^v.>>>v<<<<v.^^v<^^<^><<<<.^>.>vvv>>#
            #<.vv>.><<.<^v.>v^v>><v>><<^vv>^v^>.><v>>><^><v<v^^vv>.v^^<v>vv<>v>^v.>>v^>..v<>^v.^>><.vv><^...<>^^>v>vvv>^<^<><v>v<^^^<#
            #>^.^>vvv>vv>^^vv>v^^v>vv>v^v>^<v>>vvvv^>.v^<v^><^>vvv<>vv<^v<.<^v^>^vv^<><>.^.^>><<^^>^<v^>>>>v><<v^^>^^>>v^v.<^vv^<^v<>#
            #><^>><vv^vvv.v><v<>^v>^<<<^^<><^<<v<>^<<v><<<v>><v<^^<vv<.>^v>^<v>.>v>>^^>^>><>.<<v<^><>>>^.<vv>.^^<.^^>>v.><<v<>><>v^><#
            #>>^.<<v.^v>^^v.>v.vv^<<.v<v<..<<v^^>^v.><>v<^^<<.>v>v>v>^v>.<>v<><<^v><^>><^v>v^v>^vvv>>>..>^>>.><v><>.^^v.><<vv<.v>^^<.#
            #>vv>^^.<>^v<^<<v^>v<^^v><>>>>v^vv>v<^v^><><^.<v^>.<^.v^>><vv>>vvv>^v<<<><vv<v^<<>v>>^v>^^^>^v><^^>^^^<<^^<v<<v<v>^^^<^>>#
            #>.v^<<.vv<^v>^>>v<>v^v^^v^v<^^<<..<<^v<<<<^<v>^>.<>v<v<v<.<.v>vv^>><^.<.v<^<^v><v<v>.v.><<^>>v^.^vv.vv^.>^^>vv<>^..v>^v<#
            #..v.>^><<>..<>.<^<<v>^^^^><<><<vv><^>v>^v<vv>v><vv.^<.<<<<v^v<>^^^vv>.>>>v^v<v<^.<v<<^.v<><<>>v<<<.>>v.^^>^^.>.<.>vv>^>>#
            #>^^vv<<><<v<^.v>>v>^^^v^<v.^><<<<<v^v>v<<^v..^<.vv.^v>vvv>^v<..v<><<^<<vv<>>>>^>^^<>^<^<^^.vv>>^>^>^v^>.<^.<<.>v^>vv<.^<#
            #<v^vv>^^v>vv>^.^>v>v^v>vv.>>.>v^v^>.^v<^.>^<<<v.>^>^^<>vv<.<^^<^v<^><v^v^.^<><v^^v^>.<vv^>v^v<v^><>^<vv>v<><^v<v.<<^.>^>#
            #<>^<^<v><^^.^^>..^^>v>>>>^v<.<>>.<^v^>vvvvv>^<<.><^<v<.v<^><^^>vv<^<^<>v>v<>^^v>>^^>>v<^^<v>v^^^vvv<<<<<>>>v>^v.<^^>^>^<#
            #>.<^v<.<^v.v>^.>>><^<<^^<<.<>><>>>^vvv<v<v<<^^vv<<<^v^v^vv<^v>>^^vv.^>^.v^^^>>v^>.<>.^^>v^^^^><^^>^>>.v<v^<^>^v^>^<<<v<<#
            #>><><.>v^<>^^>.^>^v>^.^<v><><>.<v<^v>>>>v.<>>.^vv>v.vv.><vv^v>^<>>v>vv.v>v>v<><>>>^..^v>^^><..^..^..^vvv>><>v<>v<^<<>v<<#
            #<vv^vvv><>v^v.<^v^^v<.<>v<>.^v^<<^<><.^vv<>^>>.v<<><vv<^^<>>v^.^v><v^^<>v.v<.>^^v^<<<^^<<v<^^^<<<<>^^v>><><<><>v>v<v.>^>#
            #.<.^>^>^>^v..v>^.<v<^<<<^vvv>vv><^^v^<.>>.^<.^^<<>>^><<>>^^.^<<^^><^v<.><>.<<<.vvv>>vv^v<^v<>vv<<^v^^<v.v^<<^>v^>v<><vv<#
            #.vvvv>>^>^<..^>>^>vv^.^>^<<v>^^v>v>^<>.>vv<v^>>>^.^<<^>>^>^>vvv><vv^^<^.^^.<^^v.<v<>^<<<v^>><<.^^^^>^>>>v<<>>v>v<>v<v><.#
            #<<vv.<v>^^v<.>>^>^<>^><.<v^><>>.^<v^v<<.v>>.<>.v>^>^v><><^<><<<v^<><<v^..^v^v>^.><>>>v^.^>vv<<^>v<><.^^v<>vv>v^v<><^>>><#
            #><>^v>v<>><vv>>v<>^<v>v^^>^v^>^^<v>v>^^v><<^v>>vv<^><>^>^^>>v^^<^<v>.^>v^vv^<.v>^v^.v^><><>>v.v^vv^<v>^>^><v>^>^>v>^v..<#
            #>v<^^<^>.<>v<v<.^vv^>>v><^^v<^^>>^>^<^^>>>^<v<^v<>v<^>v>^>v^v>^.<<v^>vv<^<.><^><<^>v><^<v>^<^v>v^>><^vvv^>>v>>vv>v^>^><<#
            #<vvv<><^.>v.^><v<^^<v><^<vvv.^..v^>...v^><<^^><^>^^vv>^v>v^<v<<>^>^^..<^<<v><<>.><<>^vvvv>v^><.^^>>v>>>^>>^<^<^<v^>vvv>>#
            #><<<^^.^<.^.^^<>^.vv^^^<v^<<<vv<<>^<v<^>>v^>>v<<v^^.v^<.vv<v<><<.v><<^>>v><.v><v>v^^v^v<^><vvvvv<^.<<>v^.<>^.^><>^v><>^<#
            #<.<>.>v^><<>^^<<vv>^>^v>>>v>v><^v><v.>>^<>>.<vv<<<v<v^<<vv^<.v^^>vv>>^<^<v<v<<>vv.><v..>>>.>^v<<vv><v^^^vv^^<>^>v<^^>v.>#
            #<>^^<>>^v>v<<>^.>>^^^<<v>^vv^<^<.<<<vv>>v>^^<^v><><>.<>.^><<^^^>^vv><>v.<>v<.<v>>><v.^>^>vv<v>^<v^<<v^v>v..>^^<^^.><>^><#
            ########################################################################################################################.#""";
}
