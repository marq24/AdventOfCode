package de.emac.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class D05 {
    public static void main(String[] args) {
        Util.tStart(0);
        String[] lines = Util.readFileLineByLine("d05.txt");
        ArrayList<ArrayList<Character>> stacks = new ArrayList<>();
        /*
            [D]
        [N] [C]
        [Z] [M] [P]
         1   2   3
        stacks.add(new ArrayList(Arrays.asList('n', 'z')));
        stacks.add(new ArrayList(Arrays.asList('d', 'c', 'm')));
        stacks.add(new ArrayList(Arrays.asList('p')));
         */
/*
    [P]                 [C] [C]
    [W]         [B]     [G] [V] [V]
    [V]         [T] [Z] [J] [T] [S]
    [D] [L]     [Q] [F] [Z] [W] [R]
    [C] [N] [R] [H] [L] [Q] [F] [G]
[F] [M] [Z] [H] [G] [W] [L] [R] [H]
[R] [H] [M] [C] [P] [C] [V] [N] [W]
[W] [T] [P] [J] [C] [G] [W] [P] [J]
 1   2   3   4   5   6   7   8   9
 */
stacks.add(new ArrayList(Arrays.asList('f', 'r', 'w')));
stacks.add(new ArrayList(Arrays.asList('p', 'w', 'v', 'd', 'c', 'm', 'h', 't')));
stacks.add(new ArrayList(Arrays.asList('l', 'n', 'z', 'm', 'p')));
stacks.add(new ArrayList(Arrays.asList('r', 'h', 'c', 'j')));
stacks.add(new ArrayList(Arrays.asList('b', 't', 'q', 'h', 'g', 'p', 'c')));
stacks.add(new ArrayList(Arrays.asList('z', 'f', 'l', 'w', 'c', 'g')));
stacks.add(new ArrayList(Arrays.asList('c', 'g', 'j', 'z', 'q', 'l', 'v', 'w')));
stacks.add(new ArrayList(Arrays.asList('c', 'v', 't', 'w', 'f', 'r', 'n', 'p')));
stacks.add(new ArrayList(Arrays.asList('v', 's', 'r', 'g', 'h', 'w', 'j')));
        for(int i=0; i< lines.length; i++){
            String line = lines[i].replaceAll("[a-z]", "").trim();
            String[] inst = line.split(" ");
            int amount = Integer.parseInt(inst[0]);
            int fromIdx = Integer.parseInt(inst[2]) -1 ;
            int toIdx = Integer.parseInt(inst[4]) -1 ;

            ArrayList<Character> src = stacks.get(fromIdx);
            ArrayList<Character> dest = stacks.get(toIdx);
            /*V1
            for(int j=0; j<amount; j++){
                // v1:
                //dest.add(0, src.remove(0) );
            }*/
            // V2
            for(int j=amount-1; j>=0; j--){
                dest.add(0, src.remove(j) );
            }
        }
        String result = "";
        for(List<Character> s: stacks){
            result = result + s.get(0);
        }
        System.out.println("res1: "+result.toUpperCase());

        Util.tEnd(0);
    }
}

