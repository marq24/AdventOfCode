package de.emac.aoc;

import java.util.ArrayList;

public class D08 {

    public static void main(String[] args) {
        Util.tStart(0);
        String[] lines = Util.readFileLineByLine("d08.txt");
        ArrayList<ArrayList<Integer>> rows = new ArrayList<>();
        ArrayList<ArrayList<Integer>> cols = new ArrayList<>();
        for(int i=0; i<lines[0].length(); i++){
            cols.add(new ArrayList<>());
        }
        for(int l=0; l< lines.length; l++){
            ArrayList<Integer> aRow = new ArrayList<>(lines[l].length());
            rows.add(aRow);
            char[] digits = lines[l].toCharArray();
            for(int i=0; i < digits.length; i++){
                Integer aVal = Integer.parseInt(digits[i]+"");
                aRow.add(aVal);
                cols.get(i).add(aVal);
            }
        }
        v1(rows, cols);
        v2(rows, cols);
        Util.tEnd(0);
    }

    private static boolean checkUP(ArrayList<ArrayList<Integer>> cols, int curCol, int curRow, int treeToCheck) {
        ArrayList<Integer> aCol = cols.get(curCol);
        for(int i=curRow-1; i>=0; i--){
            if(aCol.get(i) >= treeToCheck){
                return false;
            }
        }
        return true;
    }
    private static boolean checkDOWN(ArrayList<ArrayList<Integer>> cols, int curCol, int curRow, int treeToCheck) {
        ArrayList<Integer> aCol = cols.get(curCol);
        for(int i=curRow+1; i<cols.size(); i++){
            if(aCol.get(i) >= treeToCheck){
                return false;
            }
        }
        return true;
    }
    private static boolean checkLEFT(ArrayList<ArrayList<Integer>> rows, int curRow, int curCol, int treeToCheck) {
        ArrayList<Integer> aRow = rows.get(curRow);
        for(int i=curCol-1; i>=0; i--){
            if(aRow.get(i) >= treeToCheck){
                return false;
            }
        }
        return true;
    }
    private static boolean checkRIGHT(ArrayList<ArrayList<Integer>> rows, int curRow, int curCol, int treeToCheck) {
        ArrayList<Integer> aRow = rows.get(curRow);
        for(int i=curCol+1; i<rows.size(); i++){
            if(aRow.get(i) >= treeToCheck){
                return false;
            }
        }
        return true;
    }

    public static void v1(ArrayList<ArrayList<Integer>> rows, ArrayList<ArrayList<Integer>> cols) {
        int rowsNum = rows.size();
        int colsNum = cols.size();
        int count = 2 * rowsNum + 2 * colsNum - 4;

        for(int curRow=1; curRow< rowsNum-1; curRow++){
            for(int curCol=1; curCol< colsNum-1; curCol++){
                int treeToCheck = rows.get(curRow).get(curCol);
                //System.out.println("check row:"+curRow+" col:"+curCol+" tree:"+treeToCheck);
                boolean isVisible = checkUP(cols, curCol, curRow, treeToCheck);
                //System.out.println("t "+isVisible);
                if(!isVisible){
                    isVisible = checkDOWN(cols, curCol, curRow,treeToCheck);
                    //System.out.println("b "+isVisible);
                    if(!isVisible){
                        isVisible = checkLEFT(rows, curRow, curCol, treeToCheck);
                        //System.out.println("l "+isVisible);
                        if(!isVisible){
                            isVisible = checkRIGHT(rows, curRow, curCol, treeToCheck);
                            //System.out.println("r "+isVisible);
                            if(isVisible){
                                count++;
                            }
                        } else {
                            count++;
                        }
                    } else {
                        count++;
                    }
                } else {
                    count++;
                }
            }
        }
        System.out.println("Res1: "+count);
    }

    public static void v2(ArrayList<ArrayList<Integer>> rows, ArrayList<ArrayList<Integer>> cols) {
        long result = 0;
        int rowsNum = rows.size();
        int colsNum = cols.size();

        for(int curRow=1; curRow< rowsNum-1; curRow++){
            for(int curCol=1; curCol< colsNum-1; curCol++){
                int treeToCheck = rows.get(curRow).get(curCol);
                int up = countUP(cols, curCol, curRow, treeToCheck);
                if(!checkUP(cols, curCol, curRow, treeToCheck)){
                    up = up+1;
                }
                int down = countDOWN(cols, curCol, curRow,treeToCheck);
                if(!checkDOWN(cols, curCol, curRow, treeToCheck)){
                    down = down+1;
                }

                int left = countLEFT(rows, curRow, curCol, treeToCheck);
                if(!checkLEFT(rows, curRow, curCol, treeToCheck)){
                    left = left+1;
                }
                int right = countRIGHT(rows, curRow, curCol, treeToCheck);
                if(!checkRIGHT(rows, curRow, curCol, treeToCheck)){
                    right = right+1;
                }
                long score = ((long) up) * ((long) down) * ((long) left) * ((long) right);
                result = Math.max(result, score);
            }
        }

        System.out.println("res2: "+result);
    }

    private static int countUP(ArrayList<ArrayList<Integer>> cols, int curCol, int curRow, int treeToCheck) {
        int count = 0;
        ArrayList<Integer> aCol = cols.get(curCol);
        for(int i=curRow-1; i>=0; i--){
            if(aCol.get(i) >= treeToCheck){
                return count;
            }else{
                count++;
            }
        }
        return count;
    }
    private static int countDOWN(ArrayList<ArrayList<Integer>> cols, int curCol, int curRow, int treeToCheck) {
        int count = 0;
        ArrayList<Integer> aCol = cols.get(curCol);
        for(int i=curRow+1; i<cols.size(); i++){
            if(aCol.get(i) >= treeToCheck){
                return count;
            }else{
                count++;
            }
        }
        return count;
    }
    private static int countLEFT(ArrayList<ArrayList<Integer>> rows, int curRow, int curCol, int treeToCheck) {
        int count = 0;
        ArrayList<Integer> aRow = rows.get(curRow);
        for(int i=curCol-1; i>=0; i--){
            if(aRow.get(i) >= treeToCheck){
                return count;
            }else{
                count++;
            }
        }
        return count;
    }
    private static int countRIGHT(ArrayList<ArrayList<Integer>> rows, int curRow, int curCol, int treeToCheck) {
        int count = 0;
        ArrayList<Integer> aRow = rows.get(curRow);
        for(int i=curCol+1; i<rows.size(); i++){
            if(aRow.get(i) >= treeToCheck){
                return count;
            }else{
                count++;
            }
        }
        return count;
    }
}
