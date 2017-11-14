package AA;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class P101 {
    public static ArrayList<Integer> bestWay;
    public static Set<Node> nodes;
    public static int start, last;

    public static void main(String[] args){
        String[] data = {"1 3", "1 2 3", "2 3 1", "1 4 2", "4 3 3"};

        bestSolution(data);
    }

    public static ArrayList<Integer> bestSolution(String[] data){
        bestWay = new ArrayList<Integer>();
        nodes = new HashSet<Node>();
        String[] splited = data[0].split("\\s+");

        if(splited[0] == splited[1]){
            return bestWay;
        }
        else{
            start = Integer.parseInt(splited[0]);
            last = Integer.parseInt(splited[1]);
            initialize(data);
        }

        return bestWay;
    }

    private static void backTracking(){

    }

    private static void createNodesWithoutNeighbour(int newNode){
        Node n = new Node();
        n.node = newNode;
        n.initNeighbours();
        nodes.add(n);
    }

    private static void initialize(String[] data){
        String[] splited;
        Node n;
        Neighbour neighbour;
        int i=1, actual;

        createNodesWithoutNeighbour(start);

        createNodesWithoutNeighbour(last);

        for (;i<data.length;i++){
            splited = data[i].split("\\s+");          //edge separated nA - nB - velocity
            actual = Integer.parseInt(splited[0]);

            if(!findNode(actual)){
                n = new Node();
                n.node = actual;
                n.initNeighbours();
                neighbour = new Neighbour();
                n.initNeighbours();
                neighbour.neighB = Integer.parseInt(splited[1]);
                neighbour.velocity = Integer.parseInt(splited[2]);
                n.neighbours.add(neighbour);
                nodes.add(n);
            }
            else{
                neighbour = new Neighbour();
                neighbour.neighB = Integer.parseInt(splited[1]);
                neighbour.velocity = Integer.parseInt(splited[2]);

                for (Node no : nodes) {
                    if (no.node==actual){
                        no.neighbours.add(neighbour);
                    }
                }
            }
        }

        mergeNeighbours();
        printNodes();
    }

    private static void mergeNeighbours() {
        Neighbour neighbour;

        for (Iterator<Node> i1 = nodes.iterator(); i1.hasNext(); ) {
            Node f1 = i1.next();
            for (Iterator<Node> i2 = nodes.iterator(); i2.hasNext(); ) {
                Node f2 = i2.next();
                if(!(f1.node == f2.node)) {
                    int k;
                    if((k=findNeighbour(f2.neighbours, f1.node))!=-1 && findNeighbour(f1.neighbours, f2.node)==-1) {
                        neighbour = new Neighbour();
                        neighbour.neighB = f2.node;
                        neighbour.velocity = f2.neighbours.get(k).velocity;
                        f1.neighbours.add(neighbour);
                    }
                }
            }
        }
    }

    private static int findNeighbour(ArrayList<Neighbour> neighbours, int node){
        int it = -1;
        for(int i=0;i<neighbours.size();i++) {
            if(neighbours.get(i).neighB == node){
                it = i;
            }
        }

        return it;
    }

    private static void printNodes(){
        for (Iterator<Node> it = nodes.iterator(); it.hasNext(); ) {
            Node f = it.next();
            System.out.print(f.node + " neighbours: ");
            for(int i=0;i<f.neighbours.size();i++){
                System.out.print(f.neighbours.get(i).neighB);
                System.out.print(" velocity[" + f.neighbours.get(i).velocity + "]");
                System.out.print('\t');
            }
            System.out.print('\n');
        }
    }

    private static Boolean findNode(int actual){
        Boolean ok = false;

        for (Iterator<Node> it = nodes.iterator(); it.hasNext(); ) {
            Node f = it.next();

            if(f.node == actual){
                ok = true;
            }
        }

        return ok;
    }

    public static class Node {
        private int node;
        private ArrayList<Neighbour> neighbours;

        public void initNeighbours(){
            neighbours = new ArrayList<Neighbour>();
        }
    }

    public static class Neighbour {
        private int neighB;
        private int velocity;
    }
}
