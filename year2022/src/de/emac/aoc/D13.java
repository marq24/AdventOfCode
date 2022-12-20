package de.emac.aoc;

import java.util.*;

public class D13 {

    public static void main(String[] args) {
        String[] lines = Util.readFileLineByLine("d13.txt");
        ArrayList<Tuple> pairs = new ArrayList<>();
        for (int l = 0; l < lines.length; l++) {
            String line = lines[l];
            if(line.length() > 0){
                pairs.add(new Tuple(new Pack(line), new Pack(lines[l+1])));
                l++;
            }
        }
        Util.tStart(0);
        System.out.println("res1: "+ v1(pairs));
        System.out.println("res2: "+ v2(pairs));
        Util.tEnd(0);

    }

    private static int v1(List<Tuple> pairs) {
        int sum = 0;
        for (int i = 0; i < pairs.size(); ++i) {
            if (pairs.get(i).isValid()) {
                sum += (i + 1);
            }
        }
        return sum;
    }

    private static int v2(List<Tuple> pairs) {
        ArrayList<Pack> packets = new ArrayList();
        for(Tuple p: pairs){
            packets.add(p.p1);
            packets.add(p.p2);
        }
        packets.add(new Pack("[[6]]"));
        packets.add(new Pack("[[2]]"));
        Comparator<Pack> cp = (left, right) -> {
            Tuple p = new Tuple(left, right);
            Boolean b = p.isValid();
            if (b == null) {
                return 0;
            } else if (b) {
                return -1;
            } else {
                return 1;
            }
        };
        Collections.sort(packets, cp);
        return findPack(packets, "[[2]]") * findPack(packets, "[[6]]");
    }

    private static final int findPack(List<Pack> packs, String packSrcToFind) {
        for (int i = 0; i < packs.size(); ++i) {
            if (packs.get(i).srcString.equals(packSrcToFind)) {
                return i + 1;
            }
        }
        return Integer.MIN_VALUE;
    }

    private record Tuple(Pack p1, Pack p2) {
        Boolean isValid() {
            return compareLists(p1.list, p2.list);
        }

        private Boolean compareLists(List<Object> list1, List<Object> list2) {
            //int i = 0;
            for (int i=0; i < list1.size() && i < list2.size(); ++i) {
                Object obj1 = list1.get(i);
                Object obj2 = list2.get(i);
                if (obj1 instanceof Integer && obj2 instanceof Integer) {
                    Integer int1 = (Integer) obj1;
                    Integer int2 = (Integer) obj2;
                    if (int2.intValue() < int1.intValue()) {
                        return false;
                    } else if (int1.intValue() < int2.intValue()) {
                        return true;
                    }
                } else {
                    List<Object> nextList1 = ensureListType(obj1);
                    List<Object> nextList2 = ensureListType(obj2);
                    Boolean b = compareLists(nextList1, nextList2);
                    if (b != null) {
                        return b;
                    }
                }
            }
            if (list1.size() < list2.size()) {
                return true;
            } else if (list2.size() < list1.size()) {
                return false;
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        private List<Object> ensureListType(Object v) {
            if (v instanceof List<?>) {
                return (List<Object>) v;
            }
            return List.of(v);
        }
    }

    private static final class Pack {
        final String srcString;
        final List<Object> list;

        Pack(String line) {
            this.srcString = line;
            list = parseLineToLists(line, 1, line.length() - 1).list;
        }

        private ListWithFinalPosIndex parseLineToLists(String line, int start, int end) {
            List<Object> parsedContent = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            int i = start;
            while (i < end) {
                if (line.charAt(i) == '[') {
                    ListWithFinalPosIndex t = parseLineToLists(line, i + 1, end);
                    parsedContent.add(t.list);
                    // we have skipped/read a certain amount of chars...
                    i = t.newLineIndex;
                } else if (line.charAt(i) == ']') {
                    if (sb.length() > 0) {
                        parsedContent.add(Integer.parseInt(sb.toString()));
                    }
                    return new ListWithFinalPosIndex(i + 1, parsedContent);
                } else if (line.charAt(i) == ',') {
                    if (sb.length() > 0) {
                        parsedContent.add(Integer.parseInt(sb.toString()));
                        sb = new StringBuilder();
                    }
                    ++i;
                } else {
                    sb.append(line.charAt(i));
                    ++i;
                }
            }
            if (sb.length() > 0) {
                parsedContent.add(Integer.parseInt(sb.toString()));
            }
            return new ListWithFinalPosIndex(i + 1, parsedContent);
        }
    }

    private static class ListWithFinalPosIndex {
        int newLineIndex;
        List<Object> list;
        ListWithFinalPosIndex(int idx, List<Object> list){
            this.newLineIndex = idx;
            this.list = list;
        }
    }
}

