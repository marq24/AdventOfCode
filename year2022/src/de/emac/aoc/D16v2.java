package de.emac.aoc;

import java.util.*;

public class D16v2 {

    private static final int MAX_TIME = 30;

    private static final Map<String, Valve> valvesById = new HashMap<>();

    public static void main(String[] args) {
        Util.tStart(0);
        String[] lines = Util.readFileLineByLine("d16.txt");
        for (int l = 0; l < lines.length; l++) {
            //"Valve IF has flow rate=7; tunnels lead to valves AR, JL, HK, PE, XI"
            String line = lines[l];
            String[] a = line.split(";");
            String[] b = a[1].split(", ");
            b[0] = b[0].substring(b[0].lastIndexOf(' ') + 1);
            String key = a[0].substring(6, 8);
            Valve v = new Valve(key, Integer.parseInt(a[0].substring(a[0].indexOf('=') + 1)), new ArrayList<String>(Arrays.asList(b)));
            valvesById.put(key, v);
        }
        System.out.println("Res1:" + new D16v2().v1());
        Util.tEnd(0);
    }

    private int maxReleasedPressure = Integer.MIN_VALUE;
    private int openedValves = 0;
    private int openableValves = 0;

    public long v1() {
        for (Valve valve : valvesById.values()) {
            if (valve.rate > 0) {
                openableValves++;
            }
        }
        // Release maximum pressure
        maxPressure(valvesById.get("AA"), 0, 0, 0);
        return maxReleasedPressure;
    }

    private void maxPressure(Valve currentValve, int totalMinutes, int totalReleasedPressure, int totalFlow) {
        if (totalMinutes >= MAX_TIME) {
            // Case base - Max time reached
            // Check if solution is a better optimal solution
            if (totalReleasedPressure > maxReleasedPressure) {
                maxReleasedPressure = totalReleasedPressure;
            }

        } else if (openedValves == openableValves) {
            // Recursive case 1 - All valves are opened
            // Calculate total pressure for remaining minutes
            // Leads directly to case base
            int remainingMinutes = MAX_TIME - totalMinutes;
            int estimatedTotalReleasedPressure = totalReleasedPressure + (remainingMinutes * totalFlow);
            maxPressure(currentValve, MAX_TIME, estimatedTotalReleasedPressure, totalFlow);

        } else if (!currentValve.open && currentValve.rate > 0) {
            // Recursive case 2 - Open an openable valve that has flow greater than 0
            // Open valve
            currentValve.open = true;
            openedValves++;
            // You spend 1 minute opening the valve, update total minutes and total released pressure before calling recursive method
            int nextTotalMinutes = totalMinutes + 1;
            int nextTotalReleasedPressure = totalReleasedPressure + totalFlow;
            int nextTotalFlow = totalFlow + currentValve.rate;
            maxPressure(currentValve, nextTotalMinutes, nextTotalReleasedPressure, nextTotalFlow);

            // Backtrack - Close valve
            currentValve.open = false;
            openedValves--;
        } else {
            // Recursive case 3 - Move to other valve
            List<String> leadingValves = currentValve.getUnvisitedTargets();
            for (String valveName : leadingValves) {
                // Obtain next valve to evaluate
                Valve nextValve = valvesById.get(valveName);

                // Move to next valve
                currentValve.targets.put(valveName, true);
                maxPressure(nextValve, totalMinutes + 1, totalReleasedPressure + totalFlow, totalFlow);
                currentValve.targets.put(valveName, false);
            }
        }
    }

    public static class Valve {

        String id;
        int rate;
        boolean open;

        private Map<String, Boolean> targets = new HashMap<>();

        public Valve(String id, int rate, List<String> targets) {
            this.id = id;
            this.rate = rate;
            for (String leadingCave : targets) {
                this.targets.put(leadingCave, false);
            }
        }

        public List<String> getUnvisitedTargets() {
            ArrayList<String> ret = new ArrayList<>();
            for (Map.Entry<String, Boolean> e : targets.entrySet()) {
                if (!e.getValue()) {
                    ret.add(e.getKey());
                }
            }
            return ret;
        }
    }
}
