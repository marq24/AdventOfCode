package de.emac.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class D04 {

    public static void main(String[] args) {
        Util.tStart(0);
        String[] lines = INPUT.split("\n");
        v1(lines);
        v2(lines);
        Util.tEnd(0);
    }

    public static void v1(String[] lines) {
        long sum = 0;
        for (String line : lines) {
            sum += parse(line).score();
        }
        System.out.println("Res1: " + sum + " [23941]");
    }

    public static void v2(String[] lines) {
        long sum = 0;
        Game[] games = new Game[lines.length];
        int i=0;
        for (String line : lines) {
            games[i] = parse(line);
            i++;
        }

        for (Game g : games) {
            sum += g.copies;
            int currentIndex = g.index;
            for (String card : g.cards) {
                if (g.wins.contains(card)) {
                    games[currentIndex].copies += g.copies;
                    currentIndex += 1;
                }
            }
        }
        System.out.println("Res2: " + sum + " [5571760]");
    }

    private static Game parse(String line) {
        line = line.substring(4);
        int pos = line.indexOf(':');
        int num = Integer.parseInt(line.substring(0, pos).trim());
        line = line.substring(pos + 1).trim();
        line = line.replaceAll("  ", " ");
        String[] parts = line.split(" \\| ");
        String[] wins = parts[0].trim().split(" ");
        String[] cards = parts[1].trim().split(" ");
        return new Game(num, 1, new ArrayList<>(Arrays.stream(wins).toList()), cards);
    }

    private static final class Game {
        private final int index;
        private int copies;
        private final ArrayList<String> wins;
        private final String[] cards;

        private Game(int idx, int copies, ArrayList<String> wins, String[] cards) {
            this.index = idx;
            this.copies = copies;
            this.wins = wins;
            this.cards = cards;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Game) obj;
            return this.index == that.index;
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, copies, wins, cards);
        }

        @Override
        public String toString() {
            return "Game[" +
                    "idx=" + index + ", " +
                    "copies=" + copies + ", " +
                    "wins=" + wins + ", " +
                    "cards=" + Arrays.stream(cards).toList() + ']';
        }

        public int score() {
            Integer value = null;
            for (String card : cards) {
                if (wins.contains(card)) {
                    if (value == null) {
                        value = 1;
                    } else {
                        value = value * 2;
                    }
                }
            }
            if (value != null) {
                return value.intValue();
            }
            return 0;
        }
    }

    public static final String INTEST = """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11""";

    public static final String INPUT = """
            Card   1: 84 17 45 77 11 66 94 28 71 70 | 45 51 86 83 53 58 64 30 67 96 41 89  8 17 33 50 80 84  6  2 87 72 27 63 77
            Card   2: 18 17 59  8 78 79 34 35 48 73 | 61 49 59 99 77  8 79 64 36  6  3 67  4 90 83 22  9 82 39 78 92 42 33 70 17
            Card   3: 60 78 77 44 62 54 94 50 32 11 |  2  6 89 50 11 60 57 53 71 44 47 62 49 42 73 78 77 54 99 29 35 94 32 68 74
            Card   4: 74 19 54  9 79 24 21 88 53  7 | 21 30 53 62 74 79 54 19 45 67  7 80 16 24 89 38 71 41 88 47  5  9  4 73 97
            Card   5: 68 18 23 55  9 60 82 27 76 16 | 55 26 12 23 74  7 58 29 45 86  5  6 93 87 14  2 66 22 60 78 17  9 34 67 71
            Card   6: 53 11 94 57 45 24 20  1  8 92 | 29 66 16 45 48 98 61 49  8 56 94 28 19 67 24 20 97 57  1 75  5 85 35 82 12
            Card   7: 99 29 90 82 88 72 84 36 53 81 | 93 22  9 59 15 81 32 98 28 96 53  2 90 99 92 74 82 65 72 33 31  7  1 97 36
            Card   8: 71  3 88  1 35 32  2 59  8 92 | 33 59  2 87  1 80 93 76 71 98 10 17 32 89 85  8 35 16 92 19 46 49  3 54 88
            Card   9: 19 14 92 73 64 53 59 24 76 65 | 76 29 55 79 88 92 43 37 99  4 96 19 69 53  2 52 33  3 73 35 82  5 85 14 57
            Card  10: 39  3 43 61 53 40 89 16 60 12 | 47 44 63  1 67 26 55 12 20 68 78 92 56 32 74 76 22 42 71 88 33 11 94 18 89
            Card  11: 55 43 88 90 60 93 58  2 89  4 | 69 89 58 75 18 43 80 40 72 81 37 22 34 60 33 65 88 50 90 39 87  2  7  4 55
            Card  12: 60 41 82 51 83 87 19 88 24 76 | 43 22 64 88 30 14 19 83 99 81 51 82 25 45 35 65 53 13 86 34 60 58 84 15 11
            Card  13: 36 23 86 64  5 46 41 24 53 16 | 41 46 57 83 35 63 65 43 64 59 82 24 75 54 23 22 45 36 48 53 61 86  5 16 74
            Card  14: 95 49 89 23 59 77 85 16 20 25 | 28 65 51 90 81 44 86 60 78 82 19 37 18 22 93 50 94 13 48 30  7  9 49 43 89
            Card  15: 78 68 34 44 80 25 70 98 71 99 | 80 73 44 20 38 34 70 57 75 77 98 40 55 71 19 67 32 10 60 94 62 68 22 13 66
            Card  16: 19 47  8 42 85 14 35 24 95 61 | 51 33 98 48 14 25 62 40 37 77 91  8  6 35 63 20 11 97 27 89  1 72 57 31 78
            Card  17: 99 36 90 26 17 86  1 83 97 29 | 44 67 36 33 19 54 84 21 17 83 26 13 64 99  1  7 95 29 90 45 43 89 32 82 81
            Card  18: 44 18 77 17 73 41 36 33 98 96 |  7 71  3 63 26 55 12 20 86 51 31 50 49 23 85 62 79 61 22 80 99 27  8 30 83
            Card  19: 93 51 80 97 98 40 81 62 91 42 |  9 27 31 29 34 35 52 46 76 39 28 14 45 58 33 18 89 60 92 22 13 17 26 71 53
            Card  20: 43 52 12 79 87 31 63 69 70 39 | 18 51 75 37 64 53 36 13 77  9 93  4 19 43 86 52 42 31 90 41 16 23 98 85 20
            Card  21: 74 10 94 30 26 66 43 35 38 81 | 68 91 83 61  5 10 78 23 93 81 95 49 33 29 39 82 86 66 58 72  3 63 67  1 18
            Card  22: 76  5 94 78 96 31 70 19 77 48 | 23 74 36 90 82 57  4 38 21 20 92 67 51  8 33 68  3 25 99  2 10 78  6 29 44
            Card  23: 79 68 54 32 86 26 69 28 80 19 | 34 42  7 88 14 78 99 37 89 15  3 22 91 23 60  9 35 19 92 70 75 20 96 29 57
            Card  24: 75 92  1 74 31  7 40 58 66 13 | 18 93 33 39 34  3 50 53 98 29 44 92 59 38 83  5 23 52 37 72 80  8 24 26 46
            Card  25: 82 34 19 18 77 27 52 85 41  6 | 32 44  3 89 21 57 45 24 86 25 96  2 58 40 12 16 73 15 64 80 47 43 53 51 92
            Card  26: 71 43 90 23 40 59 93 99 56 97 | 84  7 99 90 16 97 70 54 40 59 36 71 23 81 33 62 77 93 56 87 39 43 24 42  1
            Card  27:  8 51 91 67  4 72 63 94 26 40 | 15 72 63  9 20 56 24 51 79 67 55  8 40  4 66 46 91 13 64 26 37 43 94 60 75
            Card  28: 68 46 58 60 62 37 52  1 51 55 | 97 82 95 52 51 74 60 70 62 48 22 20 98 33 45 55 68  1 56 58 46 91 36 37 89
            Card  29: 19 56 81 51 63 31 14 35 70 66 | 69 47 39  3 85 29 84 86 33  6 40 24 23 74 38  9 95 77  5 76 17 12 27 75 83
            Card  30: 25 76 38 75 63 17 29 69 55 18 | 75 99 21  2  9 13 82 86 44 51 33 29 60 55 59 96 25 56 49 18 46 91 64  3 38
            Card  31: 19 54 77 99 85 67 26 30 46 95 | 85 13 46 71 37 32 35 77 76 79  3 66 56 58 48 73 26 54 29 55 33 45 22 67 99
            Card  32: 10 31 83 87 47 89 99 46 58 77 | 76 81 64 53  3 83 30 34 91 48 85 29 17 47 89 75 26 18 40 62 49  4 87 20 36
            Card  33: 72 17 14 67 40 23  6 92 32 66 | 40 18 79 70 41 66 23 32 67 87 99 53  2 17 20 58 29 74 55  3 43 92 13  1 78
            Card  34: 53 70 50 43 64 46 99  6 28 69 |  6 26 47 71 86 69 30 99 72  5 70 92 53 48 81 43 46 50 64 76 10 28  8 89 14
            Card  35: 73 83 59  4 12  7 28 68 52 15 | 72 32 62 51 82  5 34 91 53 68 85  7 30 58 95 22 38 86 55 24 89 78 70 45 96
            Card  36: 95 28 59 24 98 92 65 25 57 79 | 83 18 79 76 22 57 59 99 20 21 32 69 51 25 30 26 62 65 95 61 82 12 56 35 88
            Card  37: 20 74 51 16 35 60  2 64 80 87 | 87 68 49 90 84 29  2 22 61 44 28 74 52 66 93  9 60 92 95 38 53 25 69 14 81
            Card  38: 11 82 13 81 29 43  7 84 57 93 |  8 97 27 91 24 41 54  1 38 95 26 49 14 30 70  9 80 63 59 69 65 68 18 98 33
            Card  39: 71 42 26 48 36 97 76 41  1 74 | 34 93 36 99 20 22 62 61 97 79  1 82 31 42 91 74 59 76 32 70 25 41 55 64 48
            Card  40: 75 14 44 80 31 95 23 21 84 35 |  3 30 54 80 83 84  7 75 74 14 23 95 89 35 53 90 36 43 18 11 88 25 59 73 41
            Card  41:  6 76 63 25 77 26 39 44 52 36 | 33 57 27 84 89 18 28 96 12 20  7 48 78 92 22  8 34 41 90 53 17 80 83 52 77
            Card  42: 23 35 45 21 31 72 20 37 80 47 | 32 59 16 84 31 29 89 62  6 97 92 51 43 20 34  7 42 52 61 37 65 73 17 75 38
            Card  43: 75 41 80  9 48 61 16 98 55 54 | 27 52 43 37 72 65 39 53 63 12 64 98 93 16 50 11 51 33 45 42  8 13 57  5 14
            Card  44: 36 80 89 60 54 95 72 83 63 15 | 10 67 58 47 49 86 16 42 82 68 71 12 53 66 50 57 43 34 74 56  5 64 37 21  9
            Card  45: 33 34 99 72 44 49 77 57 90 55 | 76 63 68 97 35 95 65 64 11 52 58 21 48  2 47 29 89 83  7 40 67 79 74 60  8
            Card  46: 35 31 36 91 45 25 14 74 40 56 | 65 88 18 28 20 76 51 95 27 79 50 64 70  5 30 78 10 46 32 41  9 16 60 69  3
            Card  47: 48 16 58 66 64 80 55 93  5 89 | 31 54 67 47 27 69 76 61 70 43 77 90  7 81 83 21 88 13 36 87 25 40 96  2  6
            Card  48: 34 51 39 29 61 97  5 55  6 31 | 19 55 70 83 29 31 60 35 97  8 82 73 61 16 59 47 50 39  6  2  5 34 62 46 51
            Card  49: 72 38 25 61 77 40 79 56 81 23 | 85 86 20 61 98 73 54 44 21 70 96 59 66 64 39 77 32 78 26 79  6  4 81 30 23
            Card  50: 71 94 82 16 91 67 63 81 29 87 | 67 63  8 16 93 40 23 28 20 87 68 79 33 71 85 84 11 88 37 41 13 58 49 19 44
            Card  51: 54 65 59 85 76 94 20 37 21 58 | 24 74 49 62 68 66 20 50 19 80 79 47 99 92 45 48 26 33 27 57 87 18 72 84  3
            Card  52: 45  3 97 88 72 95 18 31 27 74 | 27 78 66 72 43 80 14 65 62 88 31 98 36 81  6 45 63 53 40 76 97 16 38 48 56
            Card  53: 18 12 41 46 93 51 17 27 35 36 | 97 57 68 96 83 89 17 34 62 73 60 71 53 91 70 19 36 16 92  8 35 39 61 65 31
            Card  54: 59 38 12 95 31 15 37 10 51 24 | 26 77 10 92 85 28 11 83 59 87  9 88 37 19 21 80 75 63 24 51 69 71 64 35 61
            Card  55: 74 37 96 86 89 24  7 10 71 55 | 48 80 42 27 72  9 45 99 33 17  8  5 53 73 87 82 16 29 83 60 35 68 75 91  6
            Card  56: 52 34  5 51 99 12 21 43 66 70 | 67 78 31 28 34 32 98 51 26 79 61 36 21 20 16 75  9  1 47 95 25 91 70 88  7
            Card  57: 49 52 63 19 98 28 76 60 34 93 | 42 31 33 37 45  2 66 34 21 17 15 78 98 52 70 27 49 63  4 19 28 25 99 47 35
            Card  58: 14 34  6 75 41 78 22 82 94 37 | 19 73  9 79 35 50 28 72 95 43 59 25 96 88  1  7 66 83 49 12 76 15 69 22 77
            Card  59:  8 42 66 43  4 56 12 25 68 44 | 52 32 22 37 46 89 43 94 11 38 33 71 66 70 44  8 76 65 96 25 35 41 73  3 15
            Card  60: 25 88 30 47 95 86 74 73  8 71 | 48 75 63 21 41 46 20 84  6 73 80 92 10 65 76  8 58 61 12 93 14 86 31 25 44
            Card  61:  8 68 85 74  4 56 46 32 81 76 | 96 32 97 54 40 99 44  1 81 12 21 29 89 14 93  6 11 10 84 67 79 65 94 80 13
            Card  62: 89 23 99 79 74 21 56 95 88 15 | 46 71 66 34 38 11 17 78 36 97 81 32 83 99 20 70 87 54 76 57 39 12 58 48  7
            Card  63: 93 28 55 70 42  4 12 31 68 94 | 90 39  3 54 61 59 84 19 14 51 71 76 99 98 47 92 37 23 52 20 95 41 24 29 44
            Card  64: 47 40 81 29 24 75 17 93 65 87 | 84  7 70 62 37 55 78 58 75  2 15 63 92 10 42 36 98  8  4 64 79 95 30 76 40
            Card  65: 26 91 19 74 68 90 23 28 18 69 | 25 83 80 46 56 43 15 41 66 87 67 89 47 82 10 35 54 24 70 30 77 34 52 97 21
            Card  66: 22 94 30 78 32 62 41 48 63 38 | 67 86 53 68 87 57 17 80 93 44  4 76 11 28 40 84 88 39  1 58 73 98 54 55 37
            Card  67:  8 41 49 70 13 18 71 12 53 76 | 18 71 21 70 38 53 27 25 36 12 41  8 59 95 60 13 76  4 49 74 20 93 30 54 87
            Card  68: 40 27 63 87 17 18 23 33  9 95 |  9 40 44 63 43 57 46 95 33 32 87 70 23 72 71 67 52 60 18 17 27 84 88 19 99
            Card  69: 30 28 12 55 80 84 75 72 53 43 | 80 55 37 18 16 76 74 75 81 60 12 43 28 49 32 61 87 47 79 30 83 72 64 53 84
            Card  70: 44  2 87 68  6 34 93  4 66 22 | 59 34 64 65 13 55 73 50 85 47 38 86 93 27 66 77 74 31 22 87 40  4 72 71 81
            Card  71:  7 11 12 49 37 86 14 57 35  9 | 57  3  7 36 79 28 35 11 71 86 37 62 15 78 12 21  2  9 81 63 77 24 14 47 61
            Card  72: 49 68 24 95 40 97 28 30 41 73 | 90 15 56 48 85 88 82 75 78 40 66 93 21 91 95 84 41 97 36 70 96  8  5 34 87
            Card  73:  7 59 40 43 68 41 79 90 29 50 | 20 59 90 25 92 44 55 41  7 32  9  6  8 61 93 65 37 49 85 14 27 63 48 62  3
            Card  74: 56 43 88 26 96 63 19 23 44 59 | 95 93 88 96 21 34 75 63 56 57 87 83 59 26 16 51 47 44 32 40 27 43 42 23 19
            Card  75: 68  3 93 45 95 76 17  5 19 18 | 57  7  4 17 68  3 34 18 50 95 69 19 88  8 80 49 45 81  6  5 46 93 91 48 76
            Card  76: 86 94 77 92 71 97 96 38 15 37 | 81 37  6 70 96 38 97 15 98 47 39 55 92 90 28 83 94 77 86 45 71 64 99 46 87
            Card  77: 88 71 94 86 72  8 99 32 92 44 | 44 91 76 81 78 15 58 12 45 95 88 89 70 41 86 94  7 75  2 18 72 32 46  6 13
            Card  78: 53 44 42 87 67 59 85 49 15 29 | 83 80 44 86 50 85 81  1 14 92 95 73 71  8 54 77 20 42 13 10  9 47 89 76 49
            Card  79: 14 81 21  3 39 71 53 45 67 32 | 88 19 26 86 28  3 81 27 12 58  1 97 20 39 10 32 31 76  8 75 45 91 94 23 66
            Card  80: 36 29 58 81 65 55 42 82 38 50 | 13 91 71 75 44 16  8 62 36 58 32 78 65 12 53  2 61 27 17 95 11 48 98 81 34
            Card  81: 12 15 33 90 61 45 85 64  8 21 | 54 47 56 34 18 68 77 21  9 30 37 25 26 28 62 83 70 19 49 91 90 69 96 93  3
            Card  82: 21 88 22 24  7 89 12 72 67 18 | 85 47  9 10 28 51 20 14 11  2 12 30 24 19 31 22 81 72 84 23 29 46 75 52 69
            Card  83: 47 12 55 65  1 19 56 94 71 24 |  3 78 39 47 16  4 83 62 89 76 55 58 70 31 34 20 61 18 74 80 75 98 25 41 79
            Card  84: 87 75 63 51 68 14 25 69 19 94 | 41 65 34 31 56 71 11 98 78 22 15 50 80 82 72 48 44 47  4 28 95 84 54 23 92
            Card  85: 70  4  9 94 30 35 71 63 11 47 | 34 47 61 85 86 22 54 72 98 16 39 20 50 74 96 62 49 31 52  5 64 81 66 55 32
            Card  86: 48 74  6 58 71 95 55 84 54 22 | 69 79 87 34  1 52 11 85  6 98 86 29 40 10 42 41 50 78 93 90  4  9 18 19 31
            Card  87: 12  8 89 92 30 23 57 45 13 82 | 79 31 44 97 20 43 64 25 22 39 93  5 61 37 16 18 85 27 67 14 53  3 83 60 94
            Card  88: 26 74 88 33 31 38  6 34 28 93 | 83 68 76  2 33 53 62  6 30 46 71  7 34 51 67 80 91 38 42 78 94 74 25 50 31
            Card  89: 74 52 44 14 11 23 67 27 63 60 | 63 62 97 78  2 47 35 14 65 81 59 69 43 68 90 73 86 28  8 88 19 79 60 32 72
            Card  90: 42 72 51 58 59 69 83 99 75 46 | 27 69  4 30 58 94 49 59 35 97 83 51 43 99 46 75 72 13 14 47 17 42 63 87 93
            Card  91: 95 83 20 79 80 49 12 91 66 31 | 16 75 64 22 43 85 47 12 11 82 50 46 24 77 31 35 55 95 72 34 67 69 74 38 88
            Card  92: 54 33 19 68 90 13 49 43 50 32 | 59 30  1 93 23  9 64 14 67 63 13 31 70 60 24 62  6 56 43 72 35 22 26 73 61
            Card  93: 65 84 41 37 73 85 25 30 10 92 | 45 52 80  2 38 36 84 37 26 96 25 67 65 91 41 63 72 99 51 30 39 85 66 32 10
            Card  94: 20 93  9 21 82 68 39 98 78 79 | 94 53 60 82 67 34 71 21 29 15 69 98 92 87 19 91 56 17 81 78 30 16 90 72 20
            Card  95: 46 74 75  2 31 92 19  9 56 86 | 74 46  9 90 40 34 12 45 75 38 86 21 19 31  2 91 56 26 44  7 97 88 13 51 92
            Card  96: 19 45 71  9 74 26 49 80  7 72 | 88 22 94 76 95 10  5  1 72 68  2 30 42 57 20 38  6  8 65 59 12 61 64 52 69
            Card  97: 20 98 54 72 79 64 89 93 94 38 | 22 11 32 78  6 70 84 24 57 55 60 36 52 76 67 47 79 90 56 50 66 83 39 77 82
            Card  98: 31 48 72 30 73 13  8 86 19 96 | 30 55 48 13 95 19 42 99  2 72 59 86 31  8 28 98 10 63 73 96 81 58 40 38 39
            Card  99: 46 56 65 82 13 24 31 89 74 94 | 34 38 43 24 13 46 60 83 12 69 57 18 65 96 56 80 50 35 32 23 66 31 77 74 49
            Card 100: 93 90 23 71 16 47 15 49 73 21 | 54 14 92 44 20 87 48  3 15 88 83  2 12 39 70 49 64 21 94 27 45 89 99 98  8
            Card 101: 80 36 73 54 67 32 46 13 59 82 | 70 86 31 34 85 20 89 50 46 82 30 79 67 16 12 54 96 47  7 74 35 32 19 75 26
            Card 102: 21 42 89  6 12 13 43 24  3 71 | 38 14  9 56 49 43 42 61 86 95  6 72 98 99 66 50  1 63 55 10 37 46 97 36 59
            Card 103: 58 26 67  8  3 69 20 87 17 31 | 69 38 41 32 52  5 42 17 91  9 51 44 93 20 84 58 21 19 49 71 55 36 14 67  3
            Card 104: 65 15 42 44 25 84 48 80 86 37 | 52 75 26 65 33 32 48 11 41 64 62 13 31 74 89 54 85 34 42 27 98 68 46 39 19
            Card 105: 30 22 55 93 10 83 94 73 86 38 | 23 45 79 80 86  3 53 70 91 77 11 83 65 63 12 29 14 32 27 84 31 78 25  6 90
            Card 106:  3  4 83 71 10 23 75 50 17 53 | 57 54  9 50 33 51 10  7 14 82 46 13  3 34 52 87 67 48 61 28 64 30 78 92 38
            Card 107: 14 87 53  2 33  5 44 55 73 69 |  4 34 58  6 41 36 16 64 32 51 82 38 47 15 94 83 77  8  5 27 52 74 84 63 14
            Card 108: 11 18 31 60 50 95 88 15 63 94 | 80 72 74 34 26 52 70 62 61 12  8 81 19 44  4 21 43 51 79 47 25 17 91 83 24
            Card 109: 71 79 65 17 88 92 93 95 96 27 | 28 23 61 39 26 10 32 75 36 57 97 63 78 62 59  3  1 74 80 72  9 68  2 91 20
            Card 110: 96 79 43 89 47 91  9 64 66 23 | 78 60 70 50 62  9 34 66 56 26 43 74 79 19 51 17 31 89 47 91 64 23 69 42 96
            Card 111:  1 29 47 53 62 84 22 13 69 94 | 74 22 91 42  3 53 45 88 29 48 26 62 96 50 41 47 54 94 39 13 72 31 69 84  1
            Card 112: 50 29 75 86 56 24 99 35 85 34 | 81 90 80 17 32 50 65 24 35  5 92 53 56 86 13 20 31 34 46 85 63 99 33 41 29
            Card 113: 35 92  5 83 68 59 23  2 78 87 | 40  2  3  5 43 10 92 51 59  8 24 78 64 57 52 97 26 74 85 62 14 72 86 29  1
            Card 114: 70 62 96 47 77 11 57 75 65 37 | 51 75 16 58 42 14 77 31 26 21 86 12 36 78 76 73 54 41 49  4 30 52  7 38 74
            Card 115: 12  9 70 13 49 31 58 44 30  3 | 80 14 49 55 93  1 90 35 75 39 33 91 82 66  9 50 62 40 31 34 41 27 87 85 37
            Card 116: 49 32 55 59 71 64 73 44 34 41 | 44 41 95 12 34 13 65 73 27 80 81 43 86 40 58 31 64 68 49 74 92 14 26 17  1
            Card 117: 86 32 48 53 34 26 19 38 14  8 |  8 48 39 19 89 97 44 26 88 27 66 81 60 56 38 54 43 53 63 32 14 86 94 34 30
            Card 118: 33 13 53  8  5 65 62 36 69  6 | 30 41 28 69 18  2 29 92 68 39 61 24 97 33 84 34 76 71 53 45 20 31  5  8  6
            Card 119: 87 74 99 12 41 76  5 14 85  8 | 94 46 88  7 52 60 67 58 72 29 38 99 78 39 22 76 41 71 98  5 95 74 35 68 47
            Card 120: 47 69 25 35 82 71 89 98 23  4 | 44 61 55 23 22  7 83 35 30 81 49 72 47 67 57  5 12 50 71 42 60  1 26 69 36
            Card 121: 71  7  4 64 63 39 93 26 56 61 | 85 69 58 38 25 79 61 47 67 59 95 94 88 62 27 20 66 60 87 13 65 93 53 72 89
            Card 122: 28 73 39 35  7 97 58 16 52  6 | 62 42 21 49 72 70 13 47 69 76 96 19  1 11 92 78 85 64  7 95 29 22 36  9  2
            Card 123: 92 87 70 37 30 49 35  8 63 28 | 38 84 87 35 20 23  4 30 34 60 70 68 81 49 41  7 89 22 93 97 25 28 46 24 92
            Card 124: 32 79 16 89 78 95 70 28 84 71 | 60 12 91 67 68 15 62  7  8 47 64 85 44 83 19 76 71 92  9 98  6 80 27  5 38
            Card 125: 33 88 37 58 48 18 27 98 11 71 | 10 35 58 81 46 41 37 92 91 23 98 79  7  9 65 36 25 39 93 62 77  6 28 20 97
            Card 126: 86 92 60 52 71 44 80 77 51 75 | 36 55 29 87 24 59 80 92 71 38 61 47 37  3 52 57  7 51 13 74 39 35 44 27 75
            Card 127: 81 11 96 68 39 58 42 16 41 78 | 18 68 43 55 42 29  9 72 81 96 11  5 49 32 46 41 15 88 99  4 50 13 60  8 39
            Card 128: 63 48  8 70 21 46 47 90 69 43 | 60 43 93  6 49 98 87 42 25 85 81 39 78 57 95 36 20 64 47 22 77 16 24 26 30
            Card 129: 49  3 17 30 71 74 41 24 67 82 | 88 40 70 72 74 20 44 93 96 94 59 58 49 87 30 90 86 85 47 60  6  8  4 12 55
            Card 130: 29 21 10 94 70 69 45 59 56 36 | 86 91 19 46  1 85 55 34 96 38 73 39 87 71 12 40 47  5 64 78 95 27 33 28 88
            Card 131: 17  5 65 61 87  1 94 38 50 12 | 29 15 99 62 81  8 75 49 48 74 14 19 13 88 39 85 23 77  9 71 92 86 11 98 91
            Card 132:  7 74 28 29 67 69  4 80 87 61 | 11 84 52  5 51  3 66 81 37 83 40 25 32 23 53 19  6 97 22 35 41  8 90 20 59
            Card 133: 88 78 53 13 20 57 98 23 71 44 | 73 16 84 76 72 34 25 65  4 32 59 10 45 31 74 94 62  8 80 58 50 49 91 26 90
            Card 134: 62 93 37 84 29  6 63 81 11 60 | 58 25 71 48 52 28 44 21 18 27 82 96 51 38 78 90 75 36  5 15 24 88 42 97 49
            Card 135: 45 59 29 47 95 25 69 60 15 91 | 93 91 53 14 64 86 34 37 69 25 15 29  7 32 47 67 76 95 82 60 38 45 59 89 55
            Card 136: 19 13 68 41 57 90 73 82  1 64 | 16 64  7 19 75 73 94 82 51 26 13 90 23 68 45  1 56 30 24 41 72 66 78 57 18
            Card 137: 25 92 31 12 98 59 11 96 38 90 | 33 99 59  3 96 98 11 65 85 17 47 72 39 92 48 74 31 12 95 90 62 84 25 55 38
            Card 138: 82 49 58 18 86 67 89 13 20 83 | 58  2  9 93 18 67  4 53 83 64 13 89 65 82  6 87 15 46 76 61 20 12 44 49 86
            Card 139: 62 59 76 63 48 10  7 65 12 51 | 15  4 63 44 29 66 95 27 26 46 85 17 47 10 60 80 35 61 78 42 86 28 23 71 64
            Card 140: 81 56 73 71 76 59 44 27 60 79 | 44 34 84 37 47 71 87 98  1 73 27 62 33 76 59 24 95 52 79 60 48 96 54 25 81
            Card 141: 12 87 21 65 60  8 79 90 89 30 | 31 98 23 40  9 60 48 34 46 26 79 87 54 99 80 84 21 58 65 30 12 88  8 89 90
            Card 142: 75 39 34 23 61  3 68  1  7 87 |  4 68 75 39 82  7 10 67 71 38 34 96 49 26 63 14  1 29 61 54 98 87  3  9 23
            Card 143: 61 54 86 76 53 96 30 93 90 75 | 33 72 67 70 15 94 26 85 75 16 21 62  2 42 88 11 77  9 31 34 35 65 55 63  1
            Card 144: 28 45 76 99 49 80 58 46 65 97 | 85 90  2 47 12 10 87 23 92 64 37 79 70 18 14 38 81 33  6  8 25 30 69 15 56
            Card 145: 92 22 36 28 81 93  5 64 16 72 | 75 45 34 15 86 53 21 89 58 84 83 20 26 35 82 33 10 66 96 91 25 73 76 69 79
            Card 146: 55 16 86 22 85 24 49  1  6 88 | 88  1 21 41 58 24 22 61 82 16 30 54  6 49 56 46 67 42 91 80 85 86 55 87 94
            Card 147: 52 33 10 21 60 97 47 15 74 78 | 63 45 49  6 53 11 93 69 37 12 77 25 46 16 35 31  8 23 51 13 30 48 94 86 17
            Card 148:  8 90 28 18 85 60 70 49 88 84 | 19 16 89 62 40 45  9 65 35 21  1 50 37 17  4  7 29 70 95 31 97 14 53 59 46
            Card 149: 38 71 19 83 63 36 35  8 54 49 | 27 98 64 72 10 94 23  5 76 60 66 37 89 13 47 82 86 44 20 15  4 92 71 45  7
            Card 150: 95 13 30 16 70 44 37 14 97 20 | 14 17 43 45 84 74 97 62 93 56 44 23 83 22 26 50  2 69 60 55 99  8 68 47 96
            Card 151: 85 52 96 47 66 70 82 32 60 10 | 86 22 24 43 35 91 77 94 58  5 73  7 10  8 51 96 74 79 80 70 52 75 39 60 32
            Card 152: 11 66 37 47  6 56 42  7 35 87 | 56 39 33 61 93 26 76 47 32 86 60 25 69 62 68 42 67 37 82 35 72 15 75 31  5
            Card 153: 93 10 79 92 26  2 81  3 61 89 | 69 72 71 87 95 42 48 90 75 27 84 14 47 96 85 19 79 23  1 68 77 53 94 22 91
            Card 154:  5 89 99 64 93 95 34  8 84 77 | 16 45 88 39 66  9 90 83 36 46 40 58 75 32 80 87 23 61 30 69 74 42 33  7 34
            Card 155: 11 88 66 21 38  3 79 24 84 74 | 98 76 64  5 12 57 65 75  6 92 96  7 93 28  4 34  8  2 45 22 69 32 73 95 55
            Card 156: 62 61 83 13  8 44 91 69 78 93 | 51 96  1 58 26 46 10 35 23 40 82 11  2 76 84  6 17 16 29 32 90 20 85 42 94
            Card 157: 37 99 26 32 18  8 71  9 89 57 | 65 50 36 83 43 25 17  6 55 85 63 81 39 59 33 49 44 54 40 66 34 78 15  4 11
            Card 158: 20 11 16 10 78  6 36 84 27 90 | 36 88 16 38 20 78 92  3 72 89 12 48 74 94 85 64 84 87  6 10 76 19 11 90 27
            Card 159: 50 15 63 66 42 25 94 77 10 51 | 94 77 98 25 10 50 23 72  4 15 63 84 11 86 42 38  8 51 92 96 18 89 70 43 66
            Card 160:  4 98 12 61 31  1 99 41 45 15 | 95  8 68 92 27 15 53 33 16 86 72 12 40 98 46 52  4 64 36 48  1 31 45 25 54
            Card 161: 55 49  2 89 15 53 14 82 45 77 | 45 27 77 59  3 83 58 15  2 56 88 89 97 49 14  6 55  7 87  5 82 53 16 21 29
            Card 162: 87 49 63 27 93 78 57 13 88 32 | 94 56 10 86 63 88 60 90 57 85  9  2 93 27 98 32 99 78 13 71 49 64 87 40 34
            Card 163: 65 36 83  8 58 27 15  6 96 10 | 68 48 51 29 10 71 58 36 60 96 50  8 81 83 27 67  6 14 56 74 12 65 35  4 15
            Card 164: 91 31 52 92 36 22 68 71 13 18 | 44 77 63 95 74 33 85 19 25 75 41 71 52 48  3 36 30 88 22 17 86 80 47 42 58
            Card 165: 26 44 66 58 87 27 22 55  1 78 | 94 29 78 21 84  7 49 55 41 82 61 63 26 34 65 12 32 67 44 87 36 57 80 18 92
            Card 166:  9 80 14 37 32 95  8 90 89 97 |  1 17 48 12 63 68 62 50 10 71 83 84  3 44 16 58 38 97  9 53 91 25 14 79 37
            Card 167: 82 85 47 68 10 54  5 71 70 56 | 30 85 12  3 17 28 77 59 96 35 41 47 23  2 13 66 99 46 98 81  4  6 19 15 25
            Card 168: 13 34 12 25 30 60 78 24 31 95 | 84 33 90 89 81 44 69 57 62 79 32 19 59 87 40 21 72 11 42 15 77 53 75  9 88
            Card 169: 75  9 87 31  6  5 73 44 18 98 | 56 39  2 50 65 44 75 30 42 69  3 97  5 58 47 45 36 73 62 48 85 60 26 55 67
            Card 170:  9 53 80  5 14 77 18 48 24 13 | 47 90 98 67 91 95 71 96 19  2 34 69  5 97 11 92  1 80 33 10 81 24 45 48 29
            Card 171: 49 40  2 91 71 90 31 99 88 34 | 55 67 38 68 31 61 73 72 32 83  4 90 81  9 13 23 28 53  1 75 33 44 14 18  2
            Card 172: 83 38 28 78 88 85 34 82 49 96 | 44 69 51 36 54 94 15 22 41 58 14 63 77 82 47 97  8  9 78 24 30 16 96 39 53
            Card 173: 23 76 87 69 31 26 34 67 55 25 | 79 96  8  6  1 19 10 77  3 72 39 44 71 73  9 30 90 97 99 13 23 48 61 34 26
            Card 174: 85  2 18 80 24 41 11 52 46 16 | 22 40 55 84 25 83 23 65 36 50 57 26 48 71  4 15 82 10 33 74 46 73 47 17  9
            Card 175:  2 60 34 23 15 53 32 84 80 33 | 88 11 27 64 65 77 49 43  1 58 91 54 71  6 90 24 37 30 39 29 26 92 81  3 69
            Card 176: 39 71 66 75 67 11 17 80 93 10 | 86 57 76 16 50 88 45 80 58 43 47 18 33 31 54 61 29 40 14 72 34  2 81 94 41
            Card 177: 71 19 23 13  5 44 66 56 65 77 | 86 78 80 37  7 16 15 76  1 18 94 21 41 68 75 72 48 36 28 31 95 91 93 65 29
            Card 178: 60 57 73 17 72 23 96 33  4 75 | 82 70 38 25 67 27 31 97 34 95 87 21 88 76 30 68 51 53 91  9 83  8 55 84 18
            Card 179: 51  5 39 38 22 42 12 73 92 97 | 38  5 92 49 35 32 73 69 81 78 60 74 22 42 51 39 12 11 30 97 37 93 50 52 54
            Card 180:  3 25 43 85  1 94 45 10 19 60 |  6  3 60 43 82 63  2 68  1 61 25 19 96 98 10 85 75  4 45 78 58 62 99 94 12
            Card 181: 56 53  4 17 71 73 60 36 16  3 | 56 61 77 50 17  2 39  6 29 55 81 45 40  4 71 95 67 15 65 27  9 90 86 92 99
            Card 182: 61 71 97 13 62 91 77 41 27  9 | 13 89 25 33  8 41 65 16 18 43 59  6 75 57 48 34 38  3  9 15 45 54 74 88 42
            Card 183: 61 55 75  9 11 48 29 89 42 46 | 71 66 24 26 60 98 97 67 18 51 58 34 69 70 73 39 65 33 96 91 94 57  8 28 54
            Card 184: 34 50 88 24 12 96 58 46  3  9 | 14 48  9 21 85 64 82 57 91 81 28 45 15 26 80 94 88 13 43 98 33 38 17 27 10
            Card 185: 68 49 62 25 69 86  9 92 26 12 |  5 14 66 16 17 68 55 93 31 45 49  7 78 94 33 73 25 84 20 98 60 82 92 95 24
            Card 186: 23 62 36 87 37 34 31 90 82 25 | 47 62 11 42  3 55 90 23 49 50 31 78 34 37 16 25  7 27 36 72 82 13 98 53 87
            Card 187: 94  8 47 24 59 65 77  5 43 63 | 21 16 70 94 34 11  3  1  6  8 88 73 66 83 47 81 27 59 60 65 98 24 63  5 43
            Card 188: 75 13  8 61 69 59 52 29 21 76 | 75 93 66 43 69 58 11  8 92 70 85 41 55 28 62 76 97 80 20 29  3 51 52 72 87
            Card 189:  7 90 29 68 69 44 94 87 58 99 | 98 48 73 68 38 75 41 83 29 67 90 25 40 13 22 63 62 94 58 99 69 19 18 44 66
            Card 190:  7 87 63 80 57 68 30 86 58 64 | 63 11 20 29 49 52 41 60 76 96 85 84 72  6 81 66 47 65  8 48 69 37 36 58 87
            Card 191:  6 71 15 30 42 60 33 17 92 16 | 60 18 65 57 16 53 20 72 59 96 40 47  8 64 81 52 70 67 29 87 35 90 54 85 17
            Card 192: 61 57 38 71 29 32 56 10 87 26 | 59  4 77  2 54 10 40 71 26 97 64 61 45 98 44 12 41 95 50 28 92 11 51 37 36
            Card 193: 40 85 41 83 90 43 10 25  5 97 | 87 17 66 53 44  3 76 57  1 10 96 28 51 16 82 52 89 65 98 93 72 38 13 79 40
            Card 194: 56 51 10  9  4 30 75 60 97 79 | 16  5  1 43 11 66 95 48 25 62 78 14 18 49 72 55 70 69 80 42 52 82 96 12 46
            Card 195: 68 36 39 79 72 45 43 85 65 93 | 97 79 96 25 20 46 63 52 38 89 24 27 57 53 64  3 31 47 14 26 18 74 37 95 58
            Card 196: 97  4 99 92 36 29 22 98 26 32 | 57 25 93 63 40 27 91 37 41 76 19 92 59 78 64 82 84 20 24 52 28 13 48 31 50
            Card 197: 51  3 97 61 84 41 39 66 93 64 | 65 75 90 78 13 79 82 33 10 96 86 19 16 32 30 99 47 92 67 80 62 69 45 15 49
            Card 198: 55 67 89 34 69  7 60 26 90 31 | 66 61 32 54 97 36  2 28  4 77 38 71 19 41 37  6 70 21 93 65  3 73 45 59 13
            Card 199: 14 26 40 92 97 83 99 29 54 60 | 23 60 90 49 18 17 32 83 91 27 54 92 78  9 29 70 61 66 48 95  2 39 88 12 97
            Card 200:  4 69 13  3 98 91 11 81 79  1 | 58 87  3 91 13 48 79 24  6 18  4 65 98 56 12 34  1 81 69 41 14 11 45 64 43
            Card 201: 37 21 55 26 67 29 25 76 15 70 | 58 86 25 95 83 17 76 69  2 51 70 45 26 92  6 54  9 21 77 67 15 84 55 37 61
            Card 202: 28 14  6 73 24 74 68 19 13 58 | 35  5 49 70 46 10 68 38 83 80 92 32 86  2 88 41 15 78 47 95 42 55 96 30 69
            Card 203: 21 23 66 43 77 12 51 14 40 67 | 21 23 45 46 77 31 40 12 75 14 52 89 51 67  6 66 73 16 79  7 48 43 84 95 96
            Card 204: 20 95 43 24 25 52 49 10 83  9 | 17 24 83 88 61 43 49 66 81 95 45 64 31 74 92 52 10  8 25 36 30 94  9 20 89
            Card 205: 39 69 99 92 97 77 52 51 29 53 | 30 84 32 65 91 83 68  5 79 98 16 57  9 20 29 69 88 52 92  1 13 17 39 34 77
            Card 206:  3 80 14  9  5 49 38 22 64 21 | 43 95 34 44 91  1 64 46 29  6 33 16 57 24 86 79 89 73 65 15 56 63 83  3 55
            Card 207: 40 76 42 26 80 31 43  8 84 63 | 73 16 57 19 38 31 84 61 50 60 70 65 81 49 86 13 22 15 40 42 63 69 36 76 37
            Card 208: 16 60  1  5 97 90 86 88 19 82 | 90 14  8 38 43 19 94 40 97 60 88 28 39 99 16 46 76 24 86 84 75 74 53 72  5
            Card 209: 12 93 53 60 41 64 25 74 79 16 | 84 18 30 31  7 65 69 11 77 99 32 89 79 90 52 28 17 38 62 24 85 49 35 43 29
            Card 210: 23 67 11 42 28 59 98 48 26 79 | 14 27 38 82 91  4 95 81 88 44 53 67 74 45 28 99  9 47 68 62 20 54 41 42  8
            Card 211:  2 31 33 18 54  6 30 68 62 60 | 60 63 49 24 73 54 75 39 59 88 64 62 72 61  4  6 26 13 34 36 95  9 74 47 44
            Card 212: 10 80 94 19 76 15 40 64  6 99 | 67 71 65 14 42 32 75 27 60 24  8 43 68 85  4 54 83 44  1  2 57 34 63 22 11
            Card 213: 21 71 66 97 26 80 42 70 89 53 | 16 29 34 79 99 73  9 65 76 19 43 11 35 52 37 25 17 21 12 26 23 82 74 91 70
            Card 214: 85 22 65 54 15  6 78  3 11 94 | 27 10 59 37 40 75 95 29 30 79 57 48 44 76 15 62 89 53  2 32 36  4 63 67 39
            Card 215: 46 68 79 92 50 22 47 89 28 34 | 29 87  6 35 20 81 15 10 71 96 77 44 55 18 98 42 76 26 72 21 95  5 23 11 57
            Card 216: 43 38 30 79 14 47 64  5  8 50 | 70 26 63 98 86 20 59 74  3 41 34 49 78 28 55 67 89 68 60 39 54 94 33 44 80""";
}

