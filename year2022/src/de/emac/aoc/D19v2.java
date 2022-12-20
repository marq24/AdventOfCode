package de.emac.aoc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D19v2 {

    public static void main(String[] args) {
        Util.tStart(0);
        //String[] lines = INTST.split("\n");
        String[] lines = INPUT.split("\n");
        v1(lines);
        Util.tEnd(0);
    }

    static HashSet<String> alreadyProcessed = new HashSet<>();
    static ArrayList<Integer> glob;

    public static void v1(String[] lines) {
        int result1 = 0;
        int result2 = 1;
        for (int l = 0; l < lines.length; l++) {
            Blueprint bp = Blueprint.parse(lines[l]);
            alreadyProcessed.clear();
            glob = new ArrayList<>();
            glob.add(0);
            int bpBestValue = check(24,
                    new MaterialRequired(1, 0, 0, 0),
                    new MaterialChange(-1, 0, 0, 0),
                    bp.bpd);
            System.out.println(bp.id + ": " + bpBestValue);
            result1 = result1 + bpBestValue * bp.id;

            if (l < 3) {
                result2 = result2 * check(32,
                        new MaterialRequired(1, 0, 0, 0),
                        new MaterialChange(-1, 0, 0, 0),
                        bp.bpd);
            }

        }
        System.out.println("res1: " + result1 + " res2: " + result2);
    }

    private static int check(int time, MaterialRequired usedMaterial, MaterialChange generatedMaterials, final BluePrintCosts buildCosts) {
        for (int i = 0; i < 4; i++) {
            int requirementForMaterialOfType = usedMaterial.get(i);
            if (requirementForMaterialOfType > buildCosts.MAX.get(i)) {
                return 0;
            }
            // Cap material when we produce more than we can purchase
            if (i < 3 && generatedMaterials.get(i) >= time * buildCosts.MAX.get(i) - requirementForMaterialOfType * (time - 1)) {
                generatedMaterials.set(i, time * buildCosts.MAX.get(i) - requirementForMaterialOfType * (time - 1));
            } else {
                generatedMaterials.set(i, generatedMaterials.get(i) + requirementForMaterialOfType);
            }
        }
        if (getMaximum(generatedMaterials.geo, usedMaterial.geo, time) <= glob.get(glob.size() - 1)) {
            return 0;
        }
        if (time == 0) {
            glob.add(generatedMaterials.geo);
            return generatedMaterials.geo;
        }
        String key = genKey(time, usedMaterial, generatedMaterials);
        if (alreadyProcessed.contains(key)) {
            return 0;
        }
        alreadyProcessed.add(key);
        int best = check(time - 1, usedMaterial.clone(), generatedMaterials.clone(), buildCosts);

        // geode
        if (generatedMaterials.ore >= buildCosts.oreRobot4geo && generatedMaterials.obs >= buildCosts.obsRobot4geo) {
            MaterialRequired nextMatAvail = usedMaterial.clone();
            MaterialChange nextMatChance = generatedMaterials.clone();
            nextMatAvail.geo += 1;

            nextMatChance.ore -= buildCosts.oreRobot4geo;
            nextMatChance.obs -= buildCosts.obsRobot4geo;
            nextMatChance.geo -= 1;
            best = Math.max(best, check(time - 1, nextMatAvail, nextMatChance, buildCosts));
        }
        // obsidian
        if (generatedMaterials.ore >= buildCosts.oreRobot3obs && generatedMaterials.clay >= buildCosts.clayRobot3obs) {
            MaterialRequired nextMatAvail = usedMaterial.clone();
            MaterialChange nextMatChance = generatedMaterials.clone();
            nextMatAvail.obs += 1;

            nextMatChance.ore -= buildCosts.oreRobot3obs;
            nextMatChance.clay -= buildCosts.clayRobot3obs;
            nextMatChance.obs -= 1;
            best = Math.max(best, check(time - 1, nextMatAvail, nextMatChance, buildCosts));
        }
        // clay
        if (generatedMaterials.ore >= buildCosts.oreRobot2clay) {
            MaterialRequired nextMatAvail = usedMaterial.clone();
            MaterialChange nextMatChance = generatedMaterials.clone();
            nextMatAvail.clay += 1;

            nextMatChance.ore -= buildCosts.oreRobot2clay;
            nextMatChance.clay -= 1;
            best = Math.max(best, check(time - 1, nextMatAvail, nextMatChance, buildCosts));
        }
        // ore
        if (generatedMaterials.ore >= buildCosts.oreRobot1ore) {
            MaterialRequired nextMatAvail = usedMaterial.clone();
            MaterialChange nextMatChance = generatedMaterials.clone();
            nextMatAvail.ore += 1;

            nextMatChance.ore -= buildCosts.oreRobot1ore;
            nextMatChance.ore -= 1;
            best = Math.max(best, check(time - 1, nextMatAvail, nextMatChance, buildCosts));
        }
        return best;
    }

    private static String genKey(int time, MaterialRequired quantities, MaterialChange materials) {
        return "T:" + time + ":"+ quantities.toString() + ":" + materials.toString();
    }

    private static int getMaximum(int material, int quantity, int time) {
        return material + quantity * time + time * (time + 1) / 2;
        // return material + quantity * time + time * (time + 1);
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

    public static class MaterialChange {
        int ore; int clay; int obs; int geo;

        public MaterialChange(int ore, int clay, int obs, int geo){
            this.ore = ore;
            this.clay = clay;
            this.obs = obs;
            this.geo = geo;
        }

        @Override
        public String toString() {
            return "M:" + ore + ":" + clay + ":" + obs + ":" + geo;
        }

        public int get(int i) {
            switch (i) {
                default:
                    return Integer.MIN_VALUE;
                case 0:
                    return ore;
                case 1:
                    return clay;
                case 2:
                    return obs;
                case 3:
                    return geo;
            }
        }

        public void set(int i, int value) {
            switch (i) {
                default:
                    break;
                case 0:
                    ore = value;
                    break;
                case 1:
                    clay = value;
                    break;
                case 2:
                    obs = value;
                    break;
                case 3:
                    geo = value;
                    break;
            }
        }

        public MaterialChange clone(){
            return new MaterialChange(ore, clay, obs, geo);
        }
    }

    public static class MaterialRequired {
        int ore; int clay; int obs; int geo;

        public MaterialRequired(int ore, int clay, int obs, int geo){
            this.ore = ore;
            this.clay = clay;
            this.obs = obs;
            this.geo = geo;
        }

        @Override
        public String toString() {
            return "R:" + ore + ":" + clay + ":" + obs + ":" + geo;
        }
        public int get(int i) {
            switch (i) {
                default:
                    return Integer.MIN_VALUE;
                case 0:
                    return ore;
                case 1:
                    return clay;
                case 2:
                    return obs;
                case 3:
                    return geo;
            }
        }
        public MaterialRequired clone(){
            return new MaterialRequired(ore, clay, obs, geo);
        }
    }

    public static class BluePrintCosts {
        int oreRobot1ore;
        int oreRobot2clay;
        int oreRobot3obs;
        int clayRobot3obs;
        int oreRobot4geo;
        int obsRobot4geo;
        MaterialChange MAX;

        public BluePrintCosts(int oreRobot1ore, int oreRobot2clay, int oreRobot3obs, int clayRobot3obs, int oreRobot4geo, int obsRobot4geo) {
            this.oreRobot1ore = oreRobot1ore;

            this.oreRobot2clay = oreRobot2clay;

            this.oreRobot3obs = oreRobot3obs;
            this.clayRobot3obs = clayRobot3obs;

            this.oreRobot4geo = oreRobot4geo;
            this.obsRobot4geo = obsRobot4geo;

            this.MAX = new MaterialChange(
                    Math.max(Math.max(Math.max(oreRobot1ore, oreRobot2clay), oreRobot3obs), oreRobot4geo),
                    clayRobot3obs,
                    obsRobot4geo,
                    1000000000);
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
