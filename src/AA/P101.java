package AA;

import java.util.*;
import java.util.Comparator;

public class P101 {
    public static ArrayList<Integer> bestWay = new ArrayList<Integer>();
    public static Map<Integer, Node> nodes = new HashMap<>();;
    public static Comparator<Node> comparator = new NodeComparator();
    public static PriorityQueue<Node> priorityQueue = new PriorityQueue<>(comparator);
    public static int start, last;
    public static int best_velocity = -1;


    public static void main(String[] args){
        String[] data = {"1 3", "1 2 3", "2 3 1", "1 4 2", "4 3 3", "2 4 3"};
        System.out.println(bestSolution(data));
    }

    public static ArrayList<Integer> bestSolution(String[] data){
        String[] splited = data[0].split("\\s+");

        if(splited[0] == splited[1])
            return bestWay;
        else{
            start = Integer.parseInt(splited[0]);
            last = Integer.parseInt(splited[1]);
            initialize(data);
        }

        //Inicio backtracking
        Node initial = nodes.get(start);
        if (initial.isFeasible(initial.alReadyChecked)) {
            initial.max_vel = Integer.MAX_VALUE;
            initial.alReadyChecked.add(initial.id);
            expand(initial);
        }

        Node n;

        while (!priorityQueue.isEmpty()) {
            n = priorityQueue.poll();
            if (n.isFeasible(n.alReadyChecked)){
                n.max_vel = Math.min(n.parents.get(n.ancestor), n.max_vel);
                n.alReadyChecked.add(n.id);
                if(n.id == last && n.max_vel >= best_velocity) {
                    best_velocity = n.max_vel;
                    bestWay = n.alReadyChecked;
                }else
                    expand(n);
            }


        }
        System.out.println();
        System.out.println("Velocity: " + best_velocity);
        return bestWay;
    }
    private static void expand(Node n){
        for (int i = 0; i < n.neighbours.size(); i++) {
            Node no = nodes.get(n.neighbours.get(i).neighB);
            no.alReadyChecked = new ArrayList<>(n.alReadyChecked);
            no.ancestor = n.id;
            no.max_vel = n.max_vel;
            no.parents.put(no.ancestor, n.neighbours.get(i).velocity);
            priorityQueue.add(no);
        }
    }
    private static void createNodesWithoutNeighbour(int newNode){
        Node n = new Node();
        n.id = newNode;
        n.initNeighbours();
        nodes.put(newNode, n);
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
                n.id = actual;
                n.initNeighbours();
                neighbour = new Neighbour();
                n.initNeighbours();
                neighbour.neighB = Integer.parseInt(splited[1]);
                neighbour.velocity = Integer.parseInt(splited[2]);
                n.neighbours.add(neighbour);
                nodes.put(actual, n);
            }
            else{
                neighbour = new Neighbour();
                neighbour.neighB = Integer.parseInt(splited[1]);
                neighbour.velocity = Integer.parseInt(splited[2]);

                for (Iterator<Node> i1 = nodes.values().iterator(); i1.hasNext();) {
                    Node toAdd = i1.next();
                    if (toAdd.id == actual){
                        toAdd.neighbours.add(neighbour);
                    }
                }
            }
        }

        mergeNeighbours();
        printNodes();
    }

    private static void mergeNeighbours() {
        Neighbour neighbour;

        for (Iterator<Node> i1 = nodes.values().iterator(); i1.hasNext(); ) {
            Node f1 = i1.next();
            for (Iterator<Node> i2 = nodes.values().iterator(); i2.hasNext(); ) {
                Node f2 = i2.next();
                if(!(f1.id == f2.id)) {
                    int k;
                    if((k=findNeighbour(f2.neighbours, f1.id))!=-1 && findNeighbour(f1.neighbours, f2.id)==-1) {
                        neighbour = new Neighbour();
                        neighbour.neighB = f2.id;
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
        for (Iterator<Node> it = nodes.values().iterator(); it.hasNext(); ) {
            Node f = it.next();
            System.out.print(f.id + " neighbours: ");
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

        for (Iterator<Integer> i = nodes.keySet().iterator(); i.hasNext();) {
            Node f = nodes.get(i.next());
            if(f.id == actual)
                ok = true;
        }

        return ok;
    }

    public static class Node {
        private int id;
        private int max_vel = 0;
        private int ancestor;
        private ArrayList<Integer> alReadyChecked = new ArrayList<>();
        private ArrayList<Neighbour> neighbours;
        private Map<Integer,Integer> parents = new HashMap<>();

        public void initNeighbours(){
            neighbours = new ArrayList<Neighbour>();
        }

        public boolean isFeasible(ArrayList<Integer> a){
            for (int element:a)
                if(element == id)
                    return false;
            return true;
        }

    }

    public static class Neighbour {
        private int neighB;
        private int velocity;
    }

    public static class NodeComparator implements Comparator<Node>{
        @Override
        public int compare(Node x, Node y){
            return x.max_vel == y.max_vel ? 0: x.max_vel<y.max_vel ? 1 : -1;
        }
    }
}