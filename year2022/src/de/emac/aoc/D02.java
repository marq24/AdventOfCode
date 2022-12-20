package de.emac.aoc;

import java.util.HashMap;

public class D02 {

    public static void main(String[] args) {
        Util.tStart(0);
        v1();
        Util.tEnd(0);
    }

    public static void v1() {
        String[] lines = Util.readFileLineByLine("d02.txt");
        // A X 1 Stein
        // B Y 2 Papier
        // C Z 3 Schere
        HashMap<String, Integer> scoresV1 = new HashMap();
        HashMap<String, String> scoresV2 = new HashMap();

        scoresV1.put("A X", 3+1); // D
        scoresV1.put("A Y", 6+2); // W
        scoresV1.put("A Z", 0+3); // L

        scoresV2.put("A X", "A Z"); // X->L
        scoresV2.put("A Y", "A X"); // Y->D
        scoresV2.put("A Z", "A Y"); // Z->W


        scoresV1.put("B X", 0+1); // L
        scoresV1.put("B Y", 3+2); // D
        scoresV1.put("B Z", 6+3); // W

        scoresV2.put("B X", "B X"); // X->L
        scoresV2.put("B Y", "B Y"); // Y->D
        scoresV2.put("B Z", "B Z"); // Z->W


        scoresV1.put("C X", 6+1); // W
        scoresV1.put("C Y", 0+2); // L
        scoresV1.put("C Z", 3+3); // D

        scoresV2.put("C X", "C Y"); // X->L
        scoresV2.put("C Y", "C Z"); // Y->D
        scoresV2.put("C Z", "C X"); // Z->W

        int score1 = 0;
        int score2 = 0;
        for (String line : lines) {
            String l = line.trim();
            score1 = score1 + scoresV1.get(l);
            score2 = score2 + scoresV1.get(scoresV2.get(l));
        }
        //Score1: 13005
        System.out.println("Score1: "+score1);
        //Score2: 11373
        System.out.println("Score2: "+score2);
    }
}

