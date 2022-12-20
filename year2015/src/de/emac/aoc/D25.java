package de.emac.aoc;

public class D25 {

    public static final long SECRET = 20201227l;

    public static void main(String[] args) {
        Util.tStart(0);

        //int cardPubKey = 5764801;
        //int doorPubKey = 17807724;

        int cardPubKey = 18499292;
        int doorPubKey = 8790390;

        long cardLoopCount = loop(cardPubKey,7, Long.MAX_VALUE);
        long doorLoopCount  = loop(doorPubKey,7, Long.MAX_VALUE);

        long v1 = loop(-1, doorPubKey, cardLoopCount);
        long v2 = loop(-1, cardPubKey, doorLoopCount);

        System.out.println("c:"+cardLoopCount+" d:"+doorLoopCount+" "+v1+" "+v2+" "+(v1-v2));
        Util.tEnd(0);
    }

    private static long loop(long keyToFind, int subject, long loopCount) {
        Util.tStart(1);
        long value = 1;
        for(long i=0; i < loopCount; i++){
            value = value * subject;
            if(value > SECRET){
                int divisor = (int) (value / SECRET);
                value = value - (divisor * SECRET);
            }
            // ok we are in the "find loop count mode" ...
            if(keyToFind>-1 && keyToFind == value){
                Util.tEnd(1);
                return i + 1;
            }
        }
        Util.tEnd(1);
        return value;
    }
}

