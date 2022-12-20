package de.emac.aoc;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Util {

    public static String[] readFileLineByLine(Path inputFilePath) {
        Charset charset = Charset.defaultCharset();
        List<String> stringList = null;
        try {
            stringList = Files.readAllLines(inputFilePath, charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringList.toArray(new String[]{});
    }

    public static String[] readFileLineByLine(String res) {
        InputStream in = Util.class.getClassLoader().getResourceAsStream(res);
        if (in != null) {
            BufferedReader lr = new BufferedReader(new InputStreamReader(in));
            return lr.lines().toArray(String[]::new);
        }
        return null;
    }

    private static final ArrayList<Long> timers = new ArrayList<>();
    public static void tStart(int i){
        if(timers.size() <= i){
            timers.add( System.nanoTime() );
        }
    }

    public static void tEnd(int i){
        tEnd(i, null);
    }

    public static void tEnd(int i, String addon){
        if(timers.size() > i){
            long dur = (System.nanoTime() - timers.remove(i)) / 1000000;
            StringBuffer buff = new StringBuffer();
            if (addon !=null){
                buff.append(addon);
                buff.append(' ');
            }
            if(dur < 4000) {
                buff.append("time: " + dur + "ms");
            }else if(dur < 60000) {
                buff.append("time: " + (((double) dur) / 1000d) + "sec");
            }else{
                buff.append("time: " + (((double) dur) / 60000d) + "min");
            }
            System.out.println(buff);
        }
    }

    public static HashSet<Character> asCharSet(String s){
        char[] a = s.toCharArray();
        HashSet<Character> ret = new HashSet<>(a.length);
        for(char c: a){
            ret.add(c);
        }
        return ret;
    }

    public static ArrayList<Character> asCharList(String s){
        char[] a = s.toCharArray();
        ArrayList<Character> ret = new ArrayList<>(a.length);
        for(char c: a){
            ret.add(c);
        }
        return ret;
    }
}
