package de.emac.aoc;

import java.util.*;

public class D16 {

    public static void main(String[] args) {
        Util.tStart(0);
        String[] lines = Util.readFileLineByLine("d16.txt");
        HashMap<String, Valve> data = new HashMap<>(lines.length);
        for(int l=0; l< lines.length; l++){
            //"Valve IF has flow rate=7; tunnels lead to valves AR, JL, HK, PE, XI"
            String line = lines[l];
            String[] a = line.split(";");
            String[] b = a[1].split(", ");
            b[0] = b[0].substring(b[0].lastIndexOf(' ')+1);
            String key = a[0].substring(6, 8);
            data.put(key, new Valve(key, Integer.parseInt(a[0].substring(a[0].indexOf('=')+1)), new ArrayList<String>(Arrays.asList(b))) );
        }
        v1(data);
        v2(data);
        Util.tEnd(0);
    }

    private static void v1(Map<String, Valve> valveMap) {
        Queue<Step> steps = new ArrayDeque<>();
        steps.add(new Step("AA", 0, 0, 0, new HashSet<>(), Set.of("AA")));
        int maxReleased = Integer.MIN_VALUE;

        Step prevStep = null;
        while (!steps.isEmpty()) {
            Step current = steps.poll();
            if (current.minuteOfVisit < 30) {
                if (!current.openValves.contains(current.valveId) && valveMap.get(current.valveId).rate > 0) {
                    steps.add(new Step(current.valveId,
                            current.minuteOfVisit + 1,
                            current.releases + valveMap.get(current.valveId).rate,
                            current.released + current.releases,
                            current.openValve(current.valveId),
                            current.visitedValves));
                }
                // adding all possible target's to our TODO List...
                for(String valveKey: valveMap.get(current.valveId).targets){
                    if(!current.visitedValves.contains(valveKey)){
                        steps.add(new Step(valveKey,
                                current.minuteOfVisit + 1,
                                current.releases,
                                current.released + current.releases,
                                current.openValves,
                                current.visitValve(valveKey)));
                    }
                }
            }
            if (maxReleased < current.released) {
                maxReleased = current.released;
                prevStep = current;
            }
            if(steps.size() % 1_000_000 == 0) {
                System.out.println("remaining steps to calc: " + steps.size() + "|" + current.minuteOfVisit);
            }
        }
        System.out.println("Res1: "+maxReleased);
    }

    private record Step(String valveId, int minuteOfVisit, int releases, int released, Set<String> openValves, Set<String> visitedValves) {
        HashSet<String> openValve(String v) {
            HashSet<String> copy = new HashSet<>(openValves);
            copy.add(v);
            return copy;
        }

        HashSet<String> visitValve(String v) {
            HashSet<String> copy = new HashSet<>(visitedValves);
            copy.add(v);
            return copy;
        }
    }

    private static void v2(Map<String, Valve> valveMap) {
        Map<FromToTuple, Integer> prices = calcCosts(valveMap);

        ArrayList<String> valvesWithEffect = new ArrayList<>();
        for (Valve v: valveMap.values()) {
            if(v.rate > 0){
                valvesWithEffect.add(v.id);
            }
        }

        Stack<StepV2> steps = new Stack<>();// new ArrayDeque<>();
        steps.add(new StepV2(List.of("AA"), List.of("AA"), new HashSet<>()));
        int maxReleased = Integer.MIN_VALUE;

        StepV2 step = null;
        long c = 0L;
        while (!steps.isEmpty()) {
            ++c;
            StepV2 current = steps.pop();
            if (current.minutes(prices) < 26 || current.altMinutes(prices) < 26) {
                String start = current.route.get(0);
                String altStart = current.altRoute.get(0);

                for(String key: valvesWithEffect){
                    if( !key.equals(start) &&
                        !key.equals(altStart) &&
                        !current.openValves.contains(key)
                    ){
                        for(String oKey: valvesWithEffect){
                            if( !oKey.equals(key) &&
                                !oKey.equals(start) &&
                                !oKey.equals(altStart) &&
                                !current.openValves.contains(oKey)
                            ){
                                steps.add(new StepV2(   current.visitValve(key),
                                                        current.altVisitValve(oKey),
                                                        current.openValve(key, oKey)));
                            }
                        }
                    }
                }
            }
            if(maxReleased < current.released(prices, valveMap)) {
                maxReleased = current.released(prices, valveMap);
                step = current;
                //System.out.println(maxReleased);
                //System.out.println(previousElephantStep);
            }

            if (c % 5_000_000 == 0) {
                System.out.println("step.size: " + steps.size() + "\t" + current.minutes(prices) + "\t" + current.altMinutes(prices)+"\t"+ c);
            }
        }
        System.out.println("Res2: "+maxReleased);
    }

    private record Valve(String id, int rate, List<String> targets) { }

    private record StepV2(List<String> route, List<String> altRoute, Set<String> openValves) {
        Set<String> openValve(String vaultKey, String altVaultKey) {
            HashSet copy = new HashSet<>(openValves);
            if (vaultKey != null) {
                copy.add(vaultKey);
            }
            if (altVaultKey != null) {
                copy.add(altVaultKey);
            }
            return copy;
        }

        List<String> visitValve(String v) {
            LinkedList copy = new LinkedList<>(route);
            copy.addFirst(v);
            return copy;
        }

        List<String> altVisitValve(String v) {
            LinkedList copy = new LinkedList<>(altRoute);
            copy.addFirst(v);
            return copy;
        }

        int minutes(Map<FromToTuple, Integer> costs) {
            return minutes(route, costs);
        }

        int altMinutes(Map<FromToTuple, Integer> costs) {
            return minutes(altRoute, costs);
        }

        private int minutes(List<String> r, Map<FromToTuple, Integer> costs) {
            if (r.size() < 2) {
                return 0;
            }
            int sum = 0;
            String prev = r.get(r.size()-1);
            for (int i = r.size()-2; i >= 0; i--) {
                sum++;
                String next = r.get(i);
                sum += costs.get(new FromToTuple(prev, next));
                prev = next;
            }
            return sum;
        }

        int released(Map<FromToTuple, Integer> prices, Map<String, Valve> valves) {
            return released(route, prices, valves) + released(altRoute, prices, valves);
        }

        private int released(List<String> route, Map<FromToTuple, Integer> prices, Map<String, Valve> valves) {
            int res = 0;
            int rate = 0;
            int left = 26;
            String prev = route.get(route.size()-1);
            for (int i = route.size()-2; i >= 0 && left > 0; --i) {
                String next = route.get(i);
                Integer price = prices.get(new FromToTuple(prev, next));
                if(left - price > 1) {
                    left -= price;
                    res += (price+1) * rate;
                    --left;
                    rate += valves.get(next).rate();
                } else {
                    res += left * rate;
                    left = 0;
                }
                prev = next;
            }
            return res;
        }
    }
    private static Map<FromToTuple, Integer> calcCosts(Map<String, Valve> valveMap) {
        ArrayList<Valve> valvesWithEffect = new ArrayList<>();
        for(Valve v: valveMap.values()) {
            if (v.rate > 0) {
                valvesWithEffect.add(v);
            }
        }
        valvesWithEffect.add(valveMap.get("AA"));

        HashMap<FromToTuple, Integer> result = new HashMap();
        for(Valve fromValve: valvesWithEffect){
            for(Valve toValve: valvesWithEffect){
                if (fromValve != toValve && !toValve.id.equals("AA")) {
                    int price = calcCost(fromValve.id, toValve.id, valveMap);
                    result.put(new FromToTuple(fromValve.id, toValve.id), price);
                }
            }
        }
        return result;
    }
    private static int calcCost(String fromKey, String toKey, Map<String, Valve> valveMap) {
        Queue<List<String>> go = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        visited.add(fromKey);
        go.add(List.of(fromKey));
        while (!go.isEmpty()) {
            List<String> n = go.poll();
            if (n.get(0).equals(toKey)) {
                return n.size() - 1;
            }
            valveMap.get(n.get(0)).targets().stream().filter(s -> !visited.contains(s)).forEach(nn -> {
                var nnn = new LinkedList<>(n);
                nnn.addFirst(nn);
                go.add(nnn);
                visited.add(nn);
            });
        }
        throw new IllegalArgumentException();
    }
    private record FromToTuple(String from, String to) { }
}

