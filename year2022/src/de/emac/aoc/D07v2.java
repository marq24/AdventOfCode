package de.emac.aoc;

import java.util.TreeSet;
import java.util.function.LongConsumer;

public class D07v2 {

    private static final int STACK_SIZE = 20;
    private static final int SMALL_DIR_THRESHOLD = 100_000;
    private static final long TOTAL_SPACE = 70_000_000L;
    private static final long REQUIRED_SPACE = 30_000_000L;

    public static void main(String[] args) {
        String[] lines = Util.readFileLineByLine("d07.txt");
        Util.tStart(0);
        System.out.println( part1(lines) );
        System.out.println( part2(lines) );
        Util.tEnd(0);
    }

    public static Long part1(String[] reader) {
        final long[] sum = new long[]{0};
        visitDirectoryTree(
                reader,
                __ -> {},
                dirSize -> {
                    if (dirSize <= SMALL_DIR_THRESHOLD) {
                        sum[0] += dirSize;
                    }
                }
        );
        return sum[0];
    }

    public static Long part2(String[] lines) {
        final long[] required = new long[] { REQUIRED_SPACE - TOTAL_SPACE };
        final TreeSet<Long> potential = new TreeSet<>();

        visitDirectoryTree(
                lines,
                fileSize -> required[0] += fileSize,
                dirSize -> {
                    if (dirSize >= required[0]) {
                        potential.add(dirSize);
                    }
                }
        );

        return potential.ceiling(required[0]);
    }

    private static void visitDirectoryTree(String[] input, LongConsumer onFileVisit, LongConsumer onDirectoryVisit) {

        long[] stack = new long[STACK_SIZE];
        int stackPointer = stack.length / 2;

        for (int i = input.length - 1; i >= 0; i--) {
            String line = input[i];
            char firstChar = line.charAt(0);
            char thirdChar = line.charAt(2);

            if (firstChar >= '0' && firstChar <= '9') {
                long fileSize = Long.parseLong(line, 0, line.indexOf(" "), 10);
                stack[stackPointer] += fileSize;
                onFileVisit.accept(fileSize);
            } else if (firstChar == '$' && thirdChar == 'c') {
                if (line.equals("$ cd ..")) {
                    stack[++stackPointer] = 0;
                } else {
                    long dirSizeTotal = stack[stackPointer--];
                    stack[stackPointer] += dirSizeTotal;
                    onDirectoryVisit.accept(dirSizeTotal);
                }
            }
        }
    }
}

