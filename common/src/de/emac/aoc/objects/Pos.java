package de.emac.aoc.objects;

public record Pos (int x, int y) implements Comparable{

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }

    @Override
    public String toString() {
        return x+"|"+y;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Pos){
            Pos p = (Pos) o;
            if(p.x == x){
                return new Integer(y).compareTo(new Integer(p.y));
            }else{
                return new Integer(x).compareTo(new Integer(p.x));
            }
        }
        return 0;
    }
}
