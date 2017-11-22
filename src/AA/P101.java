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
    public static int contador = 0;  public static long tiempoInicializacion; public static PrintWriter writer;


    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        /*String[] data = {"1 5", "1 4 3", "1 18 3", "1 37 2", "1 10 1", "2 19 3", "2 37 3", "2 13 1", "2 29 4", "3 30 2", "3 21 3", "3 32 6", "3 23 2",
                "4 16 4", "4 27 6", "4 1 4", "4 21 6", "5 29 2", "5 17 4", "5 10 3", "5 18 3", "6 7 6", "6 24 1", "6 14 6", "6 37 6", "7 3 3",
                "7 36 3", "7 26 5", "7 23 1", "8 22 6", "8 10 5", "8 5 5", "8 7 5", "9 3 4", "9 2 5", "9 24 6", "9 29 4", "10 26 5", "10 17 3",
                "10 16 5", "10 34 6", "11 13 1", "11 5 3", "11 21 5", "11 16 1", "12 24 1", "12 30 4", "12 31 1", "12 34 5", "13 17 2", "13 14 1", "13 2 3",
                "13 16 2", "14 11 1", "14 27 6", "14 38 2", "14 18 4", "15 8 3", "15 36 3", "15 2 3", "15 27 4", "16 26 2", "16 27 2", "16 22 4", "16 5 1",
                "17 24 4", "17 19 2", "17 29 1", "17 38 2", "18 40 4", "18 11 4", "18 36 1", "18 20 2", "19 6 1", "19 18 3", "19 32 4", "19 16 4", "20 34 2",
                "20 36 2", "20 28 5", "20 38 3", "21 30 5", "21 11 3", "21 32 1", "21 27 6", "22 37 1", "22 6 3", "22 2 1", "22 5 5", "23 5 4", "23 38 2",
                "23 30 2", "23 18 5", "24 13 6", "24 29 6", "24 12 1", "24 39 3", "25 1 3", "25 10 2", "25 4 5", "25 11 4", "26 32 4", "26 2 3", "26 24 5",
                "26 39 5", "27 11 5", "27 22 3", "27 33 6", "27 23 5", "28 22 6", "28 10 4", "28 15 6", "28 17 5", "29 36 6", "29 10 1", "29 8 1", "29 37 1",
                "30 16 6", "30 26 2", "30 5 6", "30 27 5", "31 7 3", "31 19 1", "31 13 3", "31 11 1", "32 12 4", "32 10 1", "32 7 6", "32 14 1", "33 19 2",
                "33 21 3", "33 34 3", "33 8 4", "34 17 1", "34 8 6", "34 29 4", "34 2 6", "35 13 4", "35 34 6", "35 19 2", "35 24 1", "36 15 4", "36 27 6",
                "36 33 1", "36 24 6", "37 23 3", "37 26 4", "37 15 3", "37 17 5", "38 33 1", "38 11 6", "38 3 4", "38 23 1", "39 21 2", "39 22 6", "39 5 5",
                "39 15 4", "40 25 2", "40 7 5", "40 19 5", "40 31 4"};*/
        String[] data = NodeGenerator(1,5,40, 39);
        writer = new PrintWriter("paraCopiar.txt", "UTF-8");

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

        writer = new PrintWriter("ultimoGrafo.txt", "UTF-8");
        writer.println("Tiempo inicialización: "+tiempoInicializacion);
        writer.println("Tiempo algoritmo: "+(duration-tiempoInicializacion));
        writer.println("Nodos explorados: "+contador);
        writer.println("Velocidad: "+best_velocity);
        writer.println("Camino: "+bestWay);
        writer.println("Nodo inicial: "+data[0].split("\\s+")[0]);
        writer.println("Nodo final: "+data[0].split("\\s+")[1]);
        writer.print("Grafo: ");
        for(int i=1;i<data.length;i++) {
            if(Math.floorMod(i, 13)==0)
                writer.println();
            writer.print("[" + data[i] + "] ");
        }
        writer.close();

    }

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

            writer.println("Para volver a probar: ");

            for(int i=0;i<data.length;i++) {
                if(Math.floorMod(i, 13)==0)
                    writer.println();
                if (i == data.length-1)
                    writer.print("\"" + data[i] + "\"");
                else
                    writer.print("\"" + data[i] + "\", ");
            }

            writer.println();
            writer.println();
            writer.close();

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
                System.out.println(contador);
                priorityQueue.add(no);
            }
        }
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