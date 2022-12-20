package de.emac.aoc;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

public class D16v3 {
    public static void main(String[] args) {
        Map<String, Valve> valveMap = new HashMap<>();
        INPUT.lines().map(Valve::parse).forEach(v -> {
            valveMap.put(v.id, v);
        });
        // part1(valveMap);
        part2_2(valveMap);
    }

    private static void part1(Map<String, Valve> valveMap) {
        Queue<Step> steps = new ArrayDeque<>();
        steps.add(new Step("AA", 0, 0, 0, new HashSet<String>(), Set.of("AA")));
        int maxReleased = Integer.MIN_VALUE;
        Step last = null;
        while (!steps.isEmpty()) {
            Step current = steps.poll();
            if (current.minute() < 30) {
                if (!current.open().contains(current.at()) && valveMap.get(current.at).flowRate() > 0) {
                    steps.add(new Step(current.at(), current.minute() + 1,
                            current.releases() + valveMap.get(current.at).flowRate(),
                            current.released() + current.releases(), current.open(current.at()), current.visited()));
                }
                var availables = valveMap.get(current.at()).available();
                availables.stream().filter(s -> !current.visited.contains(s)).forEach(id -> {
                    steps.add(new Step(id, current.minute() + 1, current.releases(),
                            current.released() + current.releases(), current.open(), current.visit(id)));
                });
            }
            if (maxReleased < current.released()) {
                maxReleased = current.released();
                last = current;
            }
            System.out.println("step.size: " + steps.size() + "\t" + current.minute());
        }
        System.out.println(maxReleased);
    }

//    private static void part2(Map<String, Valve> valveMap) {
//        Stack<ElephantStep> steps = new Stack<>();// new ArrayDeque<>();
//        steps.add(new ElephantStep("AA", "AA", 0, 0, 0, new HashSet<String>(), Set.of("AA"), Set.of("AA")));
//        int maxReleased = Integer.MIN_VALUE;
//        ElephantStep last = null;
//        long c = 0L;
//        while (!steps.isEmpty()) {
//            ++c;
//            var current = steps.pop();
//            if (current.minute() < 26) {
//                var iShouldOpen = !current.open().contains(current.at()) && valveMap.get(current.at).flowRate() > 0;
//                var eShouldOpen = !current.open().contains(current.eAt()) && valveMap.get(current.eAt).flowRate() > 0;
//                if (iShouldOpen && eShouldOpen && !current.at().equals(current.eAt())) {
//                    steps.add(new ElephantStep(current.at(), current.eAt(), current.minute() + 1,
//                            current.releases() + valveMap.get(current.at()).flowRate()
//                                    + valveMap.get(current.eAt()).flowRate(),
//                            current.released() + current.releases(), current.open(current.at(), current.eAt()),
//                            current.visited(), current.eVisited()));
//                } else if (iShouldOpen) {
//                    valveMap.get(current.eAt()).available().stream()
//                            /* .filter(s->!current.eVisited().contains(s)) */.forEach(id -> {
//                                steps.add(new ElephantStep(current.at(), id, current.minute() + 1,
//                                        current.releases() + valveMap.get(current.at()).flowRate(),
//                                        current.released() + current.releases(), current.open(current.at(), null),
//                                        current.visited(), current.eVisit(id)));
//                            });
//                } else if (eShouldOpen) {
//                    valveMap.get(current.at()).available().stream()
//                            /* .filter(s->!current.visited().contains(s)) */.forEach(id -> {
//                                steps.add(new ElephantStep(id, current.eAt(), current.minute() + 1,
//                                        current.releases() + valveMap.get(current.eAt()).flowRate(),
//                                        current.released() + current.releases(), current.open(null, current.eAt()),
//                                        current.visit(id), current.eVisited()));
//                            });
//                } else {
//                    valveMap.get(current.at()).available().stream()
//                            /* .filter(s->!current.visited().contains(s)) */.forEach(id -> {
//                                valveMap.get(current.eAt()).available().stream()
//                                        /* .filter(s->!current.eVisited().contains(s)) */.forEach(eId -> {
//                                            steps.add(new ElephantStep(id, eId, current.minute() + 1,
//                                                    current.releases(), current.released() + current.releases(),
//                                                    current.open(), current.visit(id), current.eVisit(eId)));
//                                        });
//                            });
//                }
//            }
//            if (maxReleased < current.released()) {
//                maxReleased = current.released();
//                last = current;
//            }
//            if (c % 1_000_000 == 0) {
//                System.out.println("step.size: " + steps.size() + "\t" + current.minute() + "\t" + c);
//            }
//        }
//        System.out.println(maxReleased);
//    }

    private static void part2_2(Map<String, Valve> valveMap) {
        var prices = calcPrices(valveMap);
        var valuables = valveMap.values().stream().filter(it -> it.flowRate() > 0).map(Valve::id).toList();
        Stack<ElephantStep> steps = new Stack<>();// new ArrayDeque<>();
        steps.add(new ElephantStep(List.of("AA"), List.of("AA"), new HashSet<String>()));
        int maxReleased = Integer.MIN_VALUE;
        ElephantStep last = null;
        long c = 0L;
        while (!steps.isEmpty()) {
            ++c;
            var current = steps.pop();
            if (current.minutes(prices) < 26 || current.eMinutes(prices) < 26) {
                String start = current.route().get(0);
                String eStart = current.eRoute().get(0);
                valuables.stream().filter(s -> !s.equals(start) && !s.equals(eStart) && !current.open().contains(s)).forEach(v -> {
                    valuables.stream().filter(eS -> !eS.equals(start) && !eS.equals(eStart) && !current.open().contains(eS) && !eS.equals(v))
                            .forEach(eV -> {
                                steps.add(new ElephantStep(current.visit(v), current.eVisit(eV), current.open(v, eV)));
                            });
                });
            }
            if(maxReleased < current.released(prices, valveMap)) {
                maxReleased = current.released(prices, valveMap);
                last = current;
                System.out.println(maxReleased);
                System.out.println(last);
            }
            if (c % 1_000_000 == 0) {
                System.out.println("step.size: " + steps.size() + "\t" + current.minutes(prices) + "\t" + current.eMinutes(prices)+"\t"+ c);
            }
        }
        System.out.println(maxReleased);
    }

    private static final record Valve(String id, int flowRate, List<String> available) {

        private static final Pattern PATTERN = Pattern
                .compile("Valve (\\w+) has flow rate=(\\d+); \\w+ \\w+ to \\w+ (.+)");
        static Valve parse(String line) {
            var matcher = PATTERN.matcher(line);
            if (matcher.matches()) {
                String id = matcher.group(1);
                int rate = Integer.parseInt(matcher.group(2));
                List<String> next = Arrays.asList(matcher.group(3).trim().split(", "));
                return new Valve(id, rate, next);
            }
            throw new IllegalArgumentException("does not match: " + line);
        }
    }

    private static final record Step(String at, int minute, int releases, int released, Set<String> open,
                                     Set<String> visited) {
        Set<String> open(String v) {
            var copy = new HashSet<>(open);
            copy.add(v);
            return copy;
        }

        Set<String> visit(String v) {
            var copy = new HashSet<>(visited);
            copy.add(v);
            return copy;
        }

        int calcMaxRelease() {
            return released + (30 - minute) * releases;
        }
    }

    private static final record ElephantStep(List<String> route, List<String> eRoute, Set<String> open) {
        Set<String> open(String v, String eV) {
            var copy = new HashSet<>(open);
            if (v != null) {
                copy.add(v);
            }
            if (eV != null) {
                copy.add(eV);
            }
            return copy;
        }

        List<String> visit(String v) {
            var copy = new LinkedList<>(route);
            copy.addFirst(v);
            return copy;
        }

        List<String> eVisit(String v) {
            var copy = new LinkedList<>(eRoute);
            copy.addFirst(v);
            return copy;
        }

        int minutes(Map<IdPair, Integer> prices) {
            return minutes(route, prices);
        }

        int eMinutes(Map<IdPair, Integer> prices) {
            return minutes(eRoute, prices);
        }

        private int minutes(List<String> r, Map<IdPair, Integer> prices) {
            if (r.size() < 2) {
                return 0;
            }
            int sum = 0;
            String prev = r.get(r.size()-1);
            for (int i = r.size()-2; i >= 0; --i) {
                ++sum;
                String next = r.get(i);
                sum += prices.get(new IdPair(prev, next));
                prev = next;
            }
            return sum;
        }

        int released(Map<IdPair, Integer> prices, Map<String, Valve> valves) {
            return released(route, prices, valves) + released(eRoute, prices, valves);
        }

        private int released(List<String> r, Map<IdPair, Integer> prices, Map<String, Valve> valves) {
            int res = 0;
            int rate = 0;
            int left = 26;
            //while(left>0) {
            String prev = r.get(r.size()-1);
            for (int i = r.size()-2; i >= 0 && left > 0; --i) {
                String next = r.get(i);
                var price = prices.get(new IdPair(prev, next));
                if(left-price>1) {
                    left -= price;
                    res += (price+1) * rate;
                    --left;
                    rate += valves.get(next).flowRate();
                } else {
                    res += left * rate;
                    left = 0;
                }
                prev = next;
            }
            //}
            return res;
        }
    }

    private static Map<IdPair, Integer> calcPrices(Map<String, Valve> valveMap) {
        var valuables = new ArrayList<>(valveMap.values().stream().filter(v -> v.flowRate() > 0).toList());
        valuables.add(valveMap.get("AA"));
        var result = new HashMap<IdPair, Integer>();
        valuables.forEach(from -> {
            valuables.forEach(to -> {
                if (from != to && !to.id.equals("AA")) {
                    int price = calcPrice(from.id(), to.id(), valveMap);
                    result.put(new IdPair(from.id(), to.id()), price);
                }
            });
        });
        return result;
    }

    private static int calcPrice(String from, String to, Map<String, Valve> valveMap) {
        Queue<List<String>> go = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        visited.add(from);
        go.add(List.of(from));
        while (!go.isEmpty()) {
            var n = go.poll();
            if (n.get(0).equals(to)) {
                return n.size() - 1;
            }
            valveMap.get(n.get(0)).available().stream().filter(s -> !visited.contains(s)).forEach(nn -> {
                var nnn = new LinkedList<>(n);
                nnn.addFirst(nn);
                go.add(nnn);
                visited.add(nn);
            });
        }
        throw new IllegalArgumentException();
    }

    private static final record IdPair(String from, String to) {

    }

    private static final String INPUT = """
            Valve NA has flow rate=0; tunnels lead to valves MU, PH
            Valve NW has flow rate=0; tunnels lead to valves KB, MH
            Valve MR has flow rate=0; tunnels lead to valves GC, FI
            Valve XD has flow rate=0; tunnels lead to valves UN, CN
            Valve HK has flow rate=0; tunnels lead to valves AA, IF
            Valve JL has flow rate=0; tunnels lead to valves IF, WB
            Valve RQ has flow rate=13; tunnels lead to valves BL, DJ
            Valve AB has flow rate=0; tunnels lead to valves BO, RU
            Valve PE has flow rate=0; tunnels lead to valves AZ, IF
            Valve QF has flow rate=0; tunnels lead to valves TD, AZ
            Valve BA has flow rate=0; tunnels lead to valves RF, GU
            Valve SY has flow rate=0; tunnels lead to valves MH, MU
            Valve NT has flow rate=0; tunnels lead to valves DJ, UN
            Valve GU has flow rate=21; tunnels lead to valves VJ, BA, YP
            Valve AZ has flow rate=12; tunnels lead to valves QF, PI, AS, PE
            Valve WQ has flow rate=23; tunnels lead to valves VJ, UM, CN
            Valve DR has flow rate=0; tunnels lead to valves GA, CQ
            Valve UM has flow rate=0; tunnels lead to valves IE, WQ
            Valve XI has flow rate=0; tunnels lead to valves IE, IF
            Valve SS has flow rate=0; tunnels lead to valves CQ, MH
            Valve IE has flow rate=22; tunnels lead to valves YP, UM, XI, XA
            Valve BT has flow rate=24; tunnels lead to valves KB, BL, GA
            Valve GA has flow rate=0; tunnels lead to valves DR, BT
            Valve AR has flow rate=0; tunnels lead to valves IF, FI
            Valve DJ has flow rate=0; tunnels lead to valves RQ, NT
            Valve PI has flow rate=0; tunnels lead to valves FI, AZ
            Valve WB has flow rate=0; tunnels lead to valves TD, JL
            Valve OQ has flow rate=0; tunnels lead to valves ME, TD
            Valve RU has flow rate=19; tunnel leads to valve AB
            Valve IF has flow rate=7; tunnels lead to valves AR, JL, HK, PE, XI
            Valve BO has flow rate=0; tunnels lead to valves ME, AB
            Valve CN has flow rate=0; tunnels lead to valves WQ, XD
            Valve HH has flow rate=0; tunnels lead to valves AA, FS
            Valve AS has flow rate=0; tunnels lead to valves AA, AZ
            Valve FS has flow rate=0; tunnels lead to valves HH, MH
            Valve PQ has flow rate=0; tunnels lead to valves TD, AA
            Valve AA has flow rate=0; tunnels lead to valves HH, CO, AS, HK, PQ
            Valve ME has flow rate=18; tunnels lead to valves OQ, BO, PH
            Valve RF has flow rate=0; tunnels lead to valves UN, BA
            Valve MH has flow rate=8; tunnels lead to valves FS, NW, SS, SY
            Valve YP has flow rate=0; tunnels lead to valves IE, GU
            Valve FI has flow rate=11; tunnels lead to valves PI, MR, AR, CO, DI
            Valve UU has flow rate=0; tunnels lead to valves CQ, MU
            Valve CO has flow rate=0; tunnels lead to valves AA, FI
            Valve TD has flow rate=16; tunnels lead to valves QF, GC, OQ, WB, PQ
            Valve MU has flow rate=15; tunnels lead to valves SY, UU, NA
            Valve BL has flow rate=0; tunnels lead to valves BT, RQ
            Valve PH has flow rate=0; tunnels lead to valves ME, NA
            Valve XA has flow rate=0; tunnels lead to valves IE, DI
            Valve GC has flow rate=0; tunnels lead to valves TD, MR
            Valve KB has flow rate=0; tunnels lead to valves BT, NW
            Valve DI has flow rate=0; tunnels lead to valves XA, FI
            Valve CQ has flow rate=9; tunnels lead to valves UU, DR, SS
            Valve VJ has flow rate=0; tunnels lead to valves WQ, GU
            Valve UN has flow rate=20; tunnels lead to valves NT, XD, RF""";
}
