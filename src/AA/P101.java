package AA;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Comparator;

public class P101 {
    public static ArrayList<Integer> bestWay = new ArrayList<>();
    public static Map<Integer, Node> nodes = new HashMap<>();;
    public static Comparator<Node> comparator = new NodeComparator();
    public static PriorityQueue<Node> priorityQueue = new PriorityQueue<>(comparator);
    public static int start, last;
    public static int best_velocity = -1;
    public static int contador = 0;
    public static long tiempoInicializacion;


    public static void main(String[] args){
        /*String[] data = {"1 4", "1 8 3", "1 7 2", "3 7 3", "7 9 4", "3 5 3", "3 9 5", "9 5 5", "3 4 3", "4 5 4", "4 10 2", "4 6 3", "10 5 4", "6 10 4", "6 2 3", "2 8 4", "1 4 1", "11 6 4", "11 4 3", "5 11 5"
                , "12 2 5", "12 8 4", "12 7 4", "12 9 3", "13 5 4", "13 11 3", "14 13 3", "14 11 3", "14 6 3"};*/
        String[] data = NodeGenerator(1,5,40, 4);

        final long startTime = System.nanoTime();
        bestSolution(data);
        final long duration = System.nanoTime() - startTime;
        try {
            toFile(duration, data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void toFile(long duration, String[] data) throws FileNotFoundException, UnsupportedEncodingException {

        PrintWriter writer = new PrintWriter("ultimoGrafo.txt", "UTF-8");
        writer.println("Tiempo inicialización: "+tiempoInicializacion);
        writer.println("Tiempo algoritmo: "+(duration-tiempoInicializacion));
        writer.println("Nodos explorados: "+contador);
        writer.println("Velocidad: "+best_velocity);
        writer.println("Camino: "+bestWay);
        writer.println("Nodo inicial: "+data[0].split("\\s+")[0]);
        writer.println("Nodo final: "+data[0].split("\\s+")[1]);
        writer.print("Grafo: ");
        for(int i=1;i<data.length;i++)
            writer.print("["+data[i]+"] ");
        writer.println();
        writer.println();
        writer.println("Para volver a probar: ");
        for(int i=0;i<data.length;i++)
            writer.print("\""+data[i]+"\", ");
        writer.close();
    }
    //Pesimista: Camino aleatorio hacia el nodo final, sino encuentra 0, si encuentra x
    public static ArrayList<Integer> bestSolution(String[] data){
        String[] splited = data[0].split("\\s+");

        if(splited[0].equals(splited[1])) {
            return bestWay;
        }else{
            start = Integer.parseInt(splited[0]);
            last = Integer.parseInt(splited[1]);

            final long startTime1 = System.nanoTime();
            initialize(data);
            tiempoInicializacion = System.nanoTime() - startTime1;
        }
        System.out.println("Inicialización acabada.");
        //Inicio backtracking
        Node n = nodes.get(start);
        n.max_vel = Integer.MAX_VALUE;

        priorityQueue.add(n);
        while (!priorityQueue.isEmpty()) {
            n = priorityQueue.poll();
            if(n.max_vel > best_velocity) {
                if (n.id == last && n.max_vel > best_velocity) {
                    n.alReadyChecked.add(n.id);
                    best_velocity = n.max_vel;
                    bestWay = n.alReadyChecked;
                } else
                    if (n.isFeasible()) {
                        expand(n);
                }
            }
        }
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

                contador++;
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

    private static void createNodes(int newNode, int newNeighbour, int newVelocity){
        Node n = new Node();
        Neighbour neighbour = new Neighbour();

        n.id = newNode;
        n.initNeighbours();
        n.initNeighbours();
        neighbour.neighB = newNeighbour;
        neighbour.velocity = newVelocity;
        n.neighbours.add(neighbour);
        nodes.put(n.id, n);
    }

    private static void initialize(String[] data){
        String[] splited;
        Node n;
        Neighbour neighbour;
        int i=1, j=0, actual;

        for (;i<data.length;i++){
            splited = data[i].split("\\s+");         //edge separated nA - nB - velocity
            actual = Integer.parseInt(splited[0]);

            if (nodes.get(actual)==null) {
                createNodes(actual, Integer.parseInt(splited[1]), Integer.parseInt(splited[2]));
            } else {
                neighbour = new Neighbour();
                neighbour.neighB = Integer.parseInt(splited[1]);
                neighbour.velocity = Integer.parseInt(splited[2]);

                nodes.get(actual).neighbours.add(neighbour);
            }

            actual = Integer.parseInt(splited[1]);

            if (nodes.get(actual) == null) {
                createNodes(actual, Integer.parseInt(splited[0]), Integer.parseInt(splited[2]));
            } else {
                neighbour = new Neighbour();
                neighbour.neighB = Integer.parseInt(splited[0]);
                neighbour.velocity = Integer.parseInt(splited[2]);

                nodes.get(actual).neighbours.add(neighbour);
            }

        }

        //printNodes();
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

    public static String[] NodeGenerator(int initial, int last, int n, int v){
        ArrayList<String> toReturn = new ArrayList<>();
        toReturn.add(""+initial+" "+last);
        ArrayList<Integer> added = new ArrayList<>();
        Random r = new Random();
        int nodeC=1;
        int toAdd=0;
        while(nodeC<=n) {
            for (int i = 0; i < v; i++) {
                toAdd = (r.nextInt(n ) + 1);
                while(toAdd == nodeC){
                    toAdd = (r.nextInt(n ) + 1);
                }

                for(int j=0;j<added.size();j++)
                    if(added.get(j).equals(toAdd) || toAdd == nodeC){
                        toAdd = (r.nextInt(n) + 1);
                        j=-1;
                    }
                added.add(toAdd);
                toReturn.add(nodeC + " " + toAdd + " " + (r.nextInt(7 - 1) + 1));
            }
            added.clear();
            nodeC++;
        }
        String[] list = new String[toReturn.size()];
        list = toReturn.toArray(list);
        return list;
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
            alReadyChecked.add(id);
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
            return  x.neighbours.size() > y.neighbours.size() ? 1:-1;
        }
    }
}