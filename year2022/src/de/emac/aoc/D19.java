package de.emac.aoc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D19 {

    public static void main(String[] args) {
        //String[] lines = INTST.split("\n");

        ArrayList<Blueprint> list = new ArrayList<>();
        String[] lines = INPUT.split("\n");
        for (int l = 0; l < lines.length; l++) {
            list.add(Blueprint.parse(lines[l]));
        }
        //v1(lines);

        Util.tStart(0);
        v1(list);
        v2(list);
        Util.tEnd(0);
    }

    private static void v1(List<Blueprint> blueprints) {
        int sum = 0;
        for (int i = 0; i < blueprints.size(); ++i) {
            Blueprint bp = blueprints.get(i);
            Util.tStart(1);
            int max = maximumGeodeV1(bp, 24);
            Util.tEnd(1, bp.id+ ": " + max);
            sum += bp.id * max;
        }
        System.out.println("res1: " + sum +" (1127)");
    }

    private static void v2(List<Blueprint> blueprints) {
        long sum = 1;
        for (int i = 0; i < 3; ++i) {
            Blueprint bp = blueprints.get(i);
            Util.tStart(1);
            int max = maximumGeodeV1(bp, 32);
            Util.tEnd(1, bp.id+ ": " + max);
            sum *= max;
        }
        System.out.println("res2: " + sum+" (21546)");

        /*sum = 1;
        for (int i = 0; i < 3; ++i) {
            Blueprint bp = blueprints.get(i);
            Util.tStart(1);
            int max = maximumGeodeV2(bp, 32);
            Util.tEnd(1, bp.id+ ": " + max);
            sum *= max;
        }
        System.out.println("res2: " + sum+" (21546)");
        */
    }

    private static int maximumGeodeV1(Blueprint bp, int maxStep) {
        Queue<State> states = new LinkedList<>();
        states.add(new State(0, 0, 1, 0, 0, 0, 0, 0, 0));

        Set<Integer> visited = new HashSet<>();
        int maxGeod = Integer.MIN_VALUE;

        while (!states.isEmpty()) {

            State curState = states.poll();
            if (curState.minute <= maxStep) {
                if (curState.geodStore > maxGeod) {
                    maxGeod = curState.geodStore;
                }

                if (canBuildGeodRobot(bp, curState)) {
                    State s = new State(curState.minute + 1,
                            curState.oreStore + curState.oreRobot - bp.bpd.geodOreCost, curState.oreRobot,
                            curState.clayStore + curState.clayRobot, curState.clayRobot,
                            curState.obisidianStore + curState.obsidianRobot - bp.bpd.geodeObisidanCost, curState.obsidianRobot,
                            curState.geodStore + curState.geodRobot, curState.geodRobot + 1);
                    if (!visited.contains(s.hashCode())) {
                        states.add(s);
                        visited.add(s.hashCode());
                    }
                } else {
                    if (canBuildObsidianRobot(bp, curState)
                            && curState.obsidianRobot < bp.bpd.geodeObisidanCost
                    ) {
                        State s = new State(curState.minute + 1,
                                curState.oreStore + curState.oreRobot - bp.bpd.obisidanOreCost, curState.oreRobot,
                                curState.clayStore + curState.clayRobot - bp.bpd.obisidanClayCost, curState.clayRobot,
                                curState.obisidianStore + curState.obsidianRobot, curState.obsidianRobot + 1,
                                curState.geodStore + curState.geodRobot, curState.geodRobot);
                        if (!visited.contains(s.hashCode())) {
                            states.add(s);
                            visited.add(s.hashCode());
                        }
                    }

                    if (canBuildClayRobot(bp, curState)
                            && curState.clayRobot < bp.bpd.obisidanClayCost
                    ) {
                        State s = new State(curState.minute + 1,
                                curState.oreStore + curState.oreRobot - bp.bpd.clayCost, curState.oreRobot,
                                curState.clayStore + curState.clayRobot, curState.clayRobot + 1,
                                curState.obisidianStore + curState.obsidianRobot, curState.obsidianRobot,
                                curState.geodStore + curState.geodRobot, curState.geodRobot);
                        if (!visited.contains(s.hashCode())) {
                            states.add(s);
                            visited.add(s.hashCode());
                        }
                    }

                    if (canBuildOreRobot(bp, curState)
                            && curState.oreRobot < bp.bpd.oreMAX
                    ) {
                        State s = new State(curState.minute + 1,
                                curState.oreStore + curState.oreRobot - bp.bpd.oreCost, curState.oreRobot + 1,
                                curState.clayStore + curState.clayRobot, curState.clayRobot,
                                curState.obisidianStore + curState.obsidianRobot, curState.obsidianRobot,
                                curState.geodStore + curState.geodRobot, curState.geodRobot);
                        if (!visited.contains(s.hashCode())) {
                            states.add(s);
                            visited.add(s.hashCode());
                        }
                    }

                    State s = new State(curState.minute + 1,
                            //curState.oreStore + curState.oreRobot, curState.oreRobot,
                            Math.min(curState.oreStore + curState.oreRobot, bp.bpd.oreMAX + bp.bpd.oreMIN), curState.oreRobot,
                            curState.clayStore + curState.clayRobot, curState.clayRobot,
                            curState.obisidianStore + curState.obsidianRobot, curState.obsidianRobot,
                            curState.geodStore + curState.geodRobot, curState.geodRobot);

                    if (!visited.contains(s.hashCode())) {
                        states.add(s);
                        visited.add(s.hashCode());
                    }
                }
            }
        }
        return maxGeod;
    }

    private static int maximumGeodeV2(Blueprint bp, int maxStep) {
        Queue<State> states = new LinkedList<>();
        states.add(new State(0, 0, 1, 0, 0, 0, 0, 0, 0));

        Set<Integer> visited = new HashSet<>();
        int maxGeod = Integer.MIN_VALUE;

        while (!states.isEmpty()) {

            State curState = states.poll();
            if (curState.minute <= maxStep) {
                if (curState.geodStore > maxGeod) {
                    maxGeod = curState.geodStore;
                }

                if (canBuildGeodRobot(bp, curState)) {
                    State s = new State(curState.minute + 1,
                            curState.oreStore + curState.oreRobot - bp.bpd.geodOreCost, curState.oreRobot,
                            curState.clayStore + curState.clayRobot, curState.clayRobot,
                            curState.obisidianStore + curState.obsidianRobot - bp.bpd.geodeObisidanCost, curState.obsidianRobot,
                            curState.geodStore + curState.geodRobot, curState.geodRobot + 1);
                    if (!visited.contains(s.hashCode())) {
                        states.add(s);
                        visited.add(s.hashCode());
                    }
                }// else {
                    if (canBuildObsidianRobot(bp, curState)
                            && curState.obsidianRobot < bp.bpd.geodeObisidanCost
                    ) {
                        State s = new State(curState.minute + 1,
                                curState.oreStore + curState.oreRobot - bp.bpd.obisidanOreCost, curState.oreRobot,
                                curState.clayStore + curState.clayRobot - bp.bpd.obisidanClayCost, curState.clayRobot,
                                curState.obisidianStore + curState.obsidianRobot, curState.obsidianRobot + 1,
                                curState.geodStore + curState.geodRobot, curState.geodRobot);
                        if (!visited.contains(s.hashCode())) {
                            states.add(s);
                            visited.add(s.hashCode());
                        }
                    }

                    if (canBuildClayRobot(bp, curState)
                            && curState.clayRobot < bp.bpd.obisidanClayCost
                    ) {
                        State s = new State(curState.minute + 1,
                                curState.oreStore + curState.oreRobot - bp.bpd.clayCost, curState.oreRobot,
                                curState.clayStore + curState.clayRobot, curState.clayRobot + 1,
                                curState.obisidianStore + curState.obsidianRobot, curState.obsidianRobot,
                                curState.geodStore + curState.geodRobot, curState.geodRobot);
                        if (!visited.contains(s.hashCode())) {
                            states.add(s);
                            visited.add(s.hashCode());
                        }
                    }

                    if (canBuildOreRobot(bp, curState)
                            && curState.oreRobot < bp.bpd.oreMAX
                    ) {
                        State s = new State(curState.minute + 1,
                                curState.oreStore + curState.oreRobot - bp.bpd.oreCost, curState.oreRobot + 1,
                                curState.clayStore + curState.clayRobot, curState.clayRobot,
                                curState.obisidianStore + curState.obsidianRobot, curState.obsidianRobot,
                                curState.geodStore + curState.geodRobot, curState.geodRobot);
                        if (!visited.contains(s.hashCode())) {
                            states.add(s);
                            visited.add(s.hashCode());
                        }
                    }

                    State s = new State(curState.minute + 1,
                            //curState.oreStore + curState.oreRobot, curState.oreRobot,
                            Math.min(curState.oreStore + curState.oreRobot, bp.bpd.oreMAX + bp.bpd.oreMIN), curState.oreRobot,
                            curState.clayStore + curState.clayRobot, curState.clayRobot,
                            curState.obisidianStore + curState.obsidianRobot, curState.obsidianRobot,
                            curState.geodStore + curState.geodRobot, curState.geodRobot);

                    if (!visited.contains(s.hashCode())) {
                        states.add(s);
                        visited.add(s.hashCode());
                    }
                }
            //}
        }
        return maxGeod;
    }

    private static final boolean canBuildOreRobot(Blueprint bp, State state) {
        return state.oreStore >= bp.bpd.oreCost;
    }

    private static final boolean canBuildClayRobot(Blueprint bp, State state) {
        return state.oreStore >= bp.bpd.clayCost;
    }

    private static final boolean canBuildObsidianRobot(Blueprint bp, State state) {
        return state.oreStore >= bp.bpd.obisidanOreCost && state.clayStore >= bp.bpd.obisidanClayCost;
    }

    private static final boolean canBuildGeodRobot(Blueprint bp, State state) {
        return state.oreStore >= bp.bpd.geodOreCost && state.obisidianStore >= bp.bpd.geodeObisidanCost;
    }

    /*private static final boolean wothBuildingObsidinaRobot(Blueprint bp, State state) {
        int left = 24 - state.minute;
        State future = new State(state.minute + left, state.oreStore + left * state.oreRobot - bp.bpd.obisidanOreCost,
                state.oreRobot, state.clayStore + left * state.clayRobot - bp.bpd.obisidanClayCost, state.clayRobot,
                state.obisidianStore + left * (state.obsidianRobot + 1), state.obsidianRobot + 1, state.geodStore,
                state.geodRobot);
        return canBuildGeodRobot(bp, future);
    }

    private static final boolean wothBuildingClayRobot(Blueprint bp, State state) {
        int left = 24 - state.minute;
        State future = state;
        for (int i = 0; i < left; ++i) {
            future = new State(future.minute + 1, future.oreStore + future.oreRobot, future.oreRobot,
                    future.clayStore + future.clayRobot, future.clayRobot + 1,
                    future.obisidianStore + future.obsidianRobot, future.obsidianRobot,
                    future.geodStore + future.geodRobot, future.geodRobot);
            if (canBuildObsidianRobot(bp, future) && wothBuildingObsidinaRobot(bp, future)) {
                return true;
            }
        }
        return false;
    }*/

    /*private static final record State(int minute,
                                      int oreStore, int oreRobot,
                                      int clayStore, int clayRobot,
                                      int obisidianStore, int obsidianRobot,
                                      int geodStore, int geodRobot) {
    }*/

    private static final record State(byte minute,
                                      byte oreStore, byte oreRobot,
                                      byte clayStore, byte clayRobot,
                                      byte obisidianStore, byte obsidianRobot,
                                      byte geodStore, byte geodRobot) {
        State(int minute, int oreStore, int oreRobot, int clayStore, int clayRobot, int obisidianStore,
              int obsidianRobot, int geodStore, int geodRobot) {
            this((byte) minute, (byte) oreStore, (byte) oreRobot, (byte) clayStore, (byte) clayRobot,
                    (byte) obisidianStore, (byte) obsidianRobot, (byte) geodStore, (byte) geodRobot);
        }
    }

    public record Blueprint(int id, BluePrintCosts bpd) {
        public static Blueprint parse(String line) {
            Matcher m = Pattern.compile("-?\\d+").matcher(line);
            List<Integer> integerList = new ArrayList<>();
            while (m.find()) {
                integerList.add(Integer.parseInt(m.group(), 10));
            }
            int[] bp = new int[integerList.size() - 1];
            for (int i = 0; i < bp.length; i++) {
                bp[i] = integerList.get(i + 1);
            }
            return new Blueprint(integerList.get(0), BluePrintCosts.generate(bp));
        }
    }

    public static class BluePrintCosts {
        int oreCost;
        int clayCost;
        int obisidanOreCost;
        int obisidanClayCost;
        int geodOreCost;
        int geodeObisidanCost;
        int oreMAX;
        int oreMIN;

        public BluePrintCosts(int oreCost, int clayCost, int obisidanOreCost, int obisidanClayCost, int geodOreCost, int geodeObisidanCost) {
            this.oreCost = oreCost;

            this.clayCost = clayCost;

            this.obisidanOreCost = obisidanOreCost;
            this.obisidanClayCost = obisidanClayCost;

            this.geodOreCost = geodOreCost;
            this.geodeObisidanCost = geodeObisidanCost;

            this.oreMAX = Math.max(Math.max(Math.max(oreCost, clayCost), obisidanOreCost), geodOreCost);
            this.oreMIN = Math.min(Math.min(Math.min(oreCost, clayCost), obisidanOreCost), geodOreCost);
        }

        public static BluePrintCosts generate(int[] bp) {
            return new BluePrintCosts(bp[0], bp[1], bp[2], bp[3], bp[4], bp[5]);
        }
    }

    private static final String INTST = """
            Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
            Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian""";
    private static final String INPUT = """
            Blueprint 1: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 20 clay. Each geode robot costs 2 ore and 12 obsidian.
            Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 3 ore and 19 obsidian.
            Blueprint 3: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 11 clay. Each geode robot costs 2 ore and 7 obsidian.
            Blueprint 4: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 8 clay. Each geode robot costs 4 ore and 14 obsidian.
            Blueprint 5: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 9 clay. Each geode robot costs 2 ore and 9 obsidian.
            Blueprint 6: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 17 clay. Each geode robot costs 3 ore and 11 obsidian.
            Blueprint 7: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 4 ore and 18 clay. Each geode robot costs 3 ore and 13 obsidian.
            Blueprint 8: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 15 clay. Each geode robot costs 2 ore and 8 obsidian.
            Blueprint 9: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 4 ore and 19 clay. Each geode robot costs 4 ore and 7 obsidian.
            Blueprint 10: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 18 clay. Each geode robot costs 3 ore and 8 obsidian.
            Blueprint 11: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 6 clay. Each geode robot costs 2 ore and 14 obsidian.
            Blueprint 12: Each ore robot costs 2 ore. Each clay robot costs 2 ore. Each obsidian robot costs 2 ore and 17 clay. Each geode robot costs 2 ore and 10 obsidian.
            Blueprint 13: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 20 clay. Each geode robot costs 2 ore and 16 obsidian.
            Blueprint 14: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 9 clay. Each geode robot costs 3 ore and 9 obsidian.
            Blueprint 15: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 13 clay. Each geode robot costs 3 ore and 19 obsidian.
            Blueprint 16: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 19 clay. Each geode robot costs 3 ore and 8 obsidian.
            Blueprint 17: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 6 clay. Each geode robot costs 3 ore and 16 obsidian.
            Blueprint 18: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 20 clay. Each geode robot costs 3 ore and 14 obsidian.
            Blueprint 19: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 8 clay. Each geode robot costs 2 ore and 15 obsidian.
            Blueprint 20: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 18 clay. Each geode robot costs 4 ore and 16 obsidian.
            Blueprint 21: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 17 clay. Each geode robot costs 3 ore and 19 obsidian.
            Blueprint 22: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 17 clay. Each geode robot costs 3 ore and 11 obsidian.
            Blueprint 23: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 5 clay. Each geode robot costs 3 ore and 7 obsidian.
            Blueprint 24: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 11 clay. Each geode robot costs 4 ore and 12 obsidian.
            Blueprint 25: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 11 clay. Each geode robot costs 2 ore and 19 obsidian.
            Blueprint 26: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 5 clay. Each geode robot costs 3 ore and 18 obsidian.
            Blueprint 27: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 17 clay. Each geode robot costs 2 ore and 13 obsidian.
            Blueprint 28: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 9 clay. Each geode robot costs 3 ore and 19 obsidian.
            Blueprint 29: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 20 clay. Each geode robot costs 3 ore and 14 obsidian.
            Blueprint 30: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 16 clay. Each geode robot costs 4 ore and 16 obsidian.""";
}
