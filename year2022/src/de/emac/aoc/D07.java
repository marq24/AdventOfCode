package de.emac.aoc;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.function.LongConsumer;

public class D07 {
    private static class EfSFile implements Comparable{
        private String name;
        long size = 0l;
        EfSFile parent;
        private boolean isDir = true;

        public EfSFile(String name, EfSFile parent) {
            this.name = name;
            this.parent = parent;
        }

        public EfSFile(long size, String name, EfSFile parent) {
            isDir = false;
            this.size = size;
            this.name = name;
            this.parent = parent;
            this.parent.increaseParentSize(size);
        }

        private void increaseParentSize(long childSize) {
            size = size + childSize;
            if(parent != null){
                parent.increaseParentSize(childSize);
            }
        }

        @Override
        public String toString() {
            String p = "";
            if(parent != null){
                p = parent.toString();
            }
            if(isDir){
                if(name.length()>0){
                    return p + name+"/";
                } else {
                    return "/";
                }
            } else {
                return "#" + p + name + " ["+size+"]";
            }
        }

        @Override
        public int compareTo(Object o) {
            // it's essential to compare the complete path!
            return toString().compareTo(o.toString());
            /*if(o instanceof File){
                File f = (File) o;
                return f.name.compareTo(name);
            }
            return 0;*/
        }

        /*@Override
        public int hashCode() {
            return toString().hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof File) {
                File f = ((File) o);
                return f.size == size && f.name.equalsIgnoreCase(name) &&  f.parent.equals(parent);
            }else{
                return false;
            }
        }

        public long getFileSize(TreeMap<File, ArrayList<File>> tree) {
            if(!isDir){
                return size;
            } else if( tree.containsKey(this) ){
                if(size == 0){
                    System.out.println("Size of dir: "+this+ " was 0?!");
                    for (File child: tree.get(this)){
                        size = size + child.getFileSize(tree);
                    }
                }
                return size;
            }
            throw new RuntimeException("Sucker!");
        }*/
    }

    static final EfSFile root = new EfSFile("", null);

    public static void main(String[] args) {
        String[] lines = Util.readFileLineByLine("d07.txt");

        Util.tStart(0);

        TreeMap<EfSFile, ArrayList<EfSFile>> tree = new TreeMap<>();
        EfSFile activePath = root;
        ArrayList<EfSFile> activeDirContent = new ArrayList<>();
        tree.put(activePath, activeDirContent);

        for(int l=1; l< lines.length; l++){
            String line = lines[l];
            if(line.startsWith("$ ls")){
                // ignore...
            } else if(line.startsWith("$ cd")){
                String dirName = line.substring(5);
                if(dirName.equals("..")){
                    activePath = activePath.parent;
                    activeDirContent = tree.get(activePath);
                } else {
                    activePath = new EfSFile(dirName, activePath);
                    activeDirContent = new ArrayList<>();
                    tree.put(activePath, activeDirContent);
                }
            } else if(line.startsWith("dir ")){
                activeDirContent.add(new EfSFile(line.substring(4), activePath));
            } else {
                String[] parts = line.split(" ");
                //activePath.increaseParentSize(Long.parseLong(parts[0]));
                activeDirContent.add(new EfSFile(Long.parseLong(parts[0]), parts[1], activePath));
            }
        }

        TreeMap<Long, EfSFile> dirsBySize = new TreeMap<>();
        long res1 = 0;
        for (EfSFile dir :tree.keySet()){
            long size = dir.size;
            dirsBySize.put(size, dir);
            if(size < 100000){
                res1 = res1 + size;
            }
        }
        System.out.println("res1: "+res1);
        System.out.println("totSize: "+root.size);
        long freeSpaceAvailable = 70000000l - root.size;
        long freeSpaceRequired = 30000000l - freeSpaceAvailable;

        long dirToDeleteSize = dirsBySize.ceilingKey(freeSpaceRequired);
        System.out.println("res2: -> Req: "+freeSpaceRequired+" Best to delete: "+dirToDeleteSize+" "+dirsBySize.get(dirToDeleteSize));

        Util.tEnd(0);
    }
}

