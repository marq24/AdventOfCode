package de.emac.aoc.objects;

import java.util.HashMap;

public class KeyValCol {

    protected HashMap<String, String> map = new HashMap<>();

    public KeyValCol() {
    }

    public void clear() {
        map = new HashMap<>();
    }

    public KeyValCol copy(KeyValCol t){
        t.map.putAll(map);
        return t;
    }

    public void parse(String line) {
        String[] parts = line.split(" ");
        for (String s : parts) {
            String[] kv = s.split(":");
            map.put(kv[0], kv[1]);
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}