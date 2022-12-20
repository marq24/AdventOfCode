package de.emac.aoc.objects;

public class Pair {

    public int a, b;

    @Override
    public boolean equals(Object o) {
        if(o instanceof  Pair){
            return a == ((Pair) o).a && b == ((Pair) o).b;
        }
        return super.equals(a);
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(a).hashCode() + Integer.valueOf(b).hashCode();
    }
}
