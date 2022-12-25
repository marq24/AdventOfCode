package de.emac.aoc;

import java.util.HashMap;

public class D25 {
    public static void main(String[] args) {
        Util.tStart(0);

        HashMap<Character, Integer> snafu = new HashMap<>();
        snafu.put('0', 0);
        snafu.put('1', 1);
        snafu.put('2', 2);
        snafu.put('-', -1);
        snafu.put('=', -2);

        String[] lines = INPUT.split("\n");
        long total = 0l;
        for (String line : lines) {
            total = total + decode(line.toCharArray(), snafu);
        }
        Util.tEnd(0, "Res1: " + toSnafu(total) + " (20=212=1-12=200=00-1)");
    }

    public static long decode(char[] line, HashMap<Character, Integer> snafu) {
        long number = 0l;
        long pow = 1l;
        for (int i = line.length - 1; i >= 0; i--) {
            number = number + snafu.get(line[i]) * pow;
            pow *= 5;
        }
        return number;
    }

    public static String toSnafu(long number) {
        HashMap<Integer, Character> map = new HashMap<>();
        map.put(0, '0');
        map.put(1, '1');
        map.put(2, '2');
        map.put(3, '=');
        map.put(4, '-');

        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            int remaining = (int) (number % 5);
            sb.append(map.get(remaining));
            if (remaining > 2) {
                number = number + 5l;
            }
            number = number / 5l;
        }
        return sb.reverse().toString();
    }

    private static final String INTEST = """
            1=-0-2
            12111
            2=0=
            21
            2=01
            111
            20012
            112
            1=-1=
            1-12
            12
            1=
            122""";

    private static final String INPUT = """
            1=12=1--2220--2=21
            2=20---12
            1=2=-=2-0=0=022
            1212=--0-20=1
            1202-1
            2=2-=1-
            1-22-12-1
            1-1-1120=1=120221
            10-02
            122-12=
            2-2
            20001--=20100
            1-=-00=00=2
            1=--0200=--0=2--1-
            1==--001
            1-10011
            1-1=2-==2=2
            2-=02=02222-=0
            1-121=
            2012=2==
            1--
            1212-2
            1--10-112=
            100--1011-=-12---
            2=
            120
            1=12-1-==0==2-02
            1==10
            12==2201112-1=-
            22201=
            202-022=1111-0
            1-=1-=--2
            111011---
            2-=1122101--1-2-11
            22100
            1=20-112
            21=--1
            1-000--21=110101=
            2-10=02-121=
            112
            121=0-2000=-=01=12=
            11=-121-=-1
            1-20==---=000-
            111221==2-122111
            1=122=22010120121
            2-11=-22=2
            1-
            101=0121121-2122
            1=2221-11-2-=
            212
            11=-==00--
            121-0--1===111
            11
            2==00-21222=-2
            2=-02-=22-2=1
            22==-1222=-1-12--
            1--22
            2110-=2-0-211-12=
            202222-1111
            2-=-=1222221=
            1=-=-==-1=
            1-1-0121
            12-1==0222
            11---100
            1--01020-2
            10=20
            12=1=-002-02-=1222
            1011-0--0121-
            101-2==
            2112021212==212
            1-==1-=110-021-22
            1==20111-2022-0
            1=1=2-=0=
            1-0==11-1--
            1--21-21-0-2-
            11-111=-=10==10-
            12-
            202-10221=
            1-1=-00=0
            2-=11
            200-
            21=1=12022-01-0--=
            1=---0=021-0==
            1-=00-1-0210=2
            1121=1=2=20-
            220100-02111=2211-
            1-0-00-0200-001-
            1002
            12-=0-012-111
            1=-0111-10=0
            21=10-2=1=00-120-
            201=
            21
            2000=0-=
            11==0--212=--12-121
            11-220-0=22-011==
            1-1-0=1-21-101==
            111=
            2--1=2=-
            1==0=0-
            1=021222=0---2-1=
            2-20=210212-0122
            1-2100-021=-2=010-11
            1-=-1-0-1-=-0
            20-122200202-12-0=
            2=-20
            1-1-=-
            11=2=1=1-
            1=11-=-20=20""";
}
