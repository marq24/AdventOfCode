package de.emac.aoc;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class D04 {
    private static MessageDigest md;
    static {
        /* Static getInstance method is called with hashing MD5 */
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String s = "ckczppom";//"bgvyzdsv";
        long i = find(s, 0l, 5);
        i = find(s, i, 6);
        i = find(s, i, 7);
        i = find(s, i, 8);
    }
    public static long find(String s, long i, int len){
        Util.tStart(0);
        for(i=0; i < Long.MAX_VALUE; i++){
            if(md5(s + i , len)){
                System.out.println("res("+len+"): " + i);
                Util.tEnd(0);
                return i;
            }
        }
        Util.tEnd(0);
        return -1;
    }
    public static boolean md5(String input, int num){
        String md5 = new BigInteger(1, md.digest(input.getBytes())).toString(16);
        if(32 - num == md5.length()){
            return true;
        }
        return false;
    }
}
