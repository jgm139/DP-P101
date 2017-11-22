package AA;

import java.util.*;
import java.util.Comparator;

public class P101 {
    public static ArrayList<Integer> bestWay = new ArrayList<>();
    public static Map<Integer, Node> nodes = new HashMap<>();;
    public static Comparator<Node> comparator = new NodeComparator();
    public static PriorityQueue<Node> priorityQueue = new PriorityQueue<>(comparator);
    public static int start, last;
    public static int best_velocity = -1;
    //public static int contador = 0;
    //public static long tiempoInicializacion;


    public static void main(String[] args){
        //final long startTime = System.nanoTime();
        String[] data = {"1 4", "1 8 3", "1 7 2", "3 7 3", "7 9 4", "3 5 3", "3 9 5", "9 5 5", "3 4 3", "4 5 4", "4 10 2", "4 6 3", "10 5 4", "6 10 4", "6 2 3", "2 8 4", "1 4 1", "11 6 4", "11 4 3", "5 11 5"
                , "12 2 5", "12 8 4", "12 7 4", "12 9 3", "13 5 4", "13 11 3", "14 13 3", "14 11 3", "14 6 3"};
        System.out.println(bestSolution(data));
        //final long duration = System.nanoTime() - startTime;
        //System.out.println("Tiempo algoritmo: "+(duration-tiempoInicializacion));
    }
    //Pesimista: Camino aleatorio hacia el nodo final, sino encuentra 0, si encuentra x
    public static ArrayList<Integer> bestSolution(String[] data){
        String[] splited = data[0].split("\\s+");

        if(splited[0].equals(splited[1])) {
            return bestWay;
        }else{
            start = Integer.parseInt(splited[0]);
            last = Integer.parseInt(splited[1]);
            //final long startTime1 = System.nanoTime();
            initialize(data);
            //tiempoInicializacion = System.nanoTime() - startTime1;
            //System.out.println("Tiempo inicialización: " + tiempoInicializacion);
        }
        //Inicio backtracking
        Node initial = nodes.get(start);
        if (initial.isFeasible()) {
            initial.max_vel = Integer.MAX_VALUE;
            initial.alReadyChecked.add(initial.id);
            expand(initial);
        }

        Node n;

        while (!priorityQueue.isEmpty()) {
            n = priorityQueue.poll();
            if(n.max_vel > best_velocity) {
                if (n.id == last) {
                    best_velocity = n.max_vel;
                    bestWay = n.alReadyChecked;
                } else if (n.isFeasible()) {
                    n.alReadyChecked.add(n.id);
                    expand(n);
                }
            }
        }
        //System.out.println(contador);
        //System.out.println("Velocity: " + best_velocity);
        return bestWay;
    }
    private static void expand(Node n){
        Neighbour neighbour;
        for (int i = 0; i < n.neighbours.size(); i++) {
            neighbour = n.neighbours.get(i);
            if(n.cotaOptimista(neighbour)) {
                Node no = new Node(nodes.get(neighbour.neighB));
                no.alReadyChecked = new ArrayList<>(n.alReadyChecked);
                no.max_vel = Math.min(neighbour.velocity, n.max_vel);

                //contador++;
                priorityQueue.add(no);
            }
        }
    }

    //No merece la pena
    private static int AlgoritmoVoraz(){
        ArrayList<Integer> checked = new ArrayList<>();
        Node initial = nodes.get(start);
        if (initial.isFeasible()) {
            initial.max_vel = Integer.MAX_VALUE;
            initial.alReadyChecked.add(initial.id);

        }
        return 0;
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
            splited = data[i].split("\\s+");         //edge separated nA - nB - velocity
            actual = Integer.parseInt(splited[0]);

            if (nodes.get(actual) == null) {
                n = new Node();
                n.id = actual;
                n.initNeighbours();
                neighbour = new Neighbour();
                n.initNeighbours();
                neighbour.neighB = Integer.parseInt(splited[1]);
                neighbour.velocity = Integer.parseInt(splited[2]);
                n.neighbours.add(neighbour);
                nodes.put(actual, n);
            } else {
                neighbour = new Neighbour();
                neighbour.neighB = Integer.parseInt(splited[1]);
                neighbour.velocity = Integer.parseInt(splited[2]);

                nodes.get(actual).neighbours.add(neighbour);
            }
            actual = Integer.parseInt(splited[1]);
            if (nodes.get(actual) == null) {
                n = new Node();
                n.id = actual;
                n.initNeighbours();
                neighbour = new Neighbour();
                n.initNeighbours();
                neighbour.neighB = Integer.parseInt(splited[0]);
                neighbour.velocity = Integer.parseInt(splited[2]);
                n.neighbours.add(neighbour);
                nodes.put(actual, n);
            } else {
                neighbour = new Neighbour();
                neighbour.neighB = Integer.parseInt(splited[0]);
                neighbour.velocity = Integer.parseInt(splited[2]);

                nodes.get(actual).neighbours.add(neighbour);
            }
        }
        printNodes();
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

    public static class Node {
        private int id;
        private int max_vel = 0;
        private Node ancestor;
        private ArrayList<Integer> alReadyChecked;
        private ArrayList<Neighbour> neighbours;

        public Node(){
            alReadyChecked = new ArrayList<>();
        }
        public Node(Node n){
            this.id = n.id;
            this.ancestor = n.ancestor;
            neighbours = n.neighbours;
        }
        public void initNeighbours(){
            neighbours = new ArrayList<>();
        }

        public boolean cotaOptimista(Neighbour neighbour){
            return neighbour.velocity > best_velocity;
        }

        public int cotaOptimista(){
            int toReturn = best_velocity;
            for(int i=0;i<neighbours.size();i++)
                toReturn = Math.max(toReturn, neighbours.get(i).velocity);
            return toReturn;
        }

        public boolean isFeasible(){
            for (int element: alReadyChecked)
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
            return x.alReadyChecked.size() == y.alReadyChecked.size() ? 1 : x.alReadyChecked.size() < y.alReadyChecked.size() ? 1:-1;
        }
    }
}