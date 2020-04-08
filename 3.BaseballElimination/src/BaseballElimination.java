import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.TreeMap;


public class BaseballElimination {
    private FordFulkerson solution;  
    private int leftVertex;   // the pseudo vertex on the left
    private int rightVertex;  // the pseudo vertex on the right
    private int[] w;   // wins 
    private int[] l;   // losses 
    private int[] r;   // remaining 
    private int[][] g; // a table for games
    private ArrayList<String> teams;  // all teams
    private int N;   // number of teams
    private int max; // max wins
    private int maxI; // a maximum win 
    private TreeMap<String, Integer> map; // team-index map

    public BaseballElimination(String filename)    {   // from 0 to N - 1 - vertexes representing teams
        max = 0;
        maxI = 0;
        In in = new In(filename);
        N = in.readInt();
        w = new int[N];
        l = new int[N];
        r = new int[N];
        teams = new ArrayList<>(N);
        g = new int[N][N];
        map = new TreeMap <>();
        if (N != 1) {
            for (int i = 0; i < N; i++) {
                String s = in.readString();
                map.put(s, i);
                teams.add(s);
                w[i] = in.readInt();
                if (w[i] > max) {
                    max = w[i];
                    maxI = i;
                }
                l[i] = in.readInt();
                r[i] = in.readInt();
                for (int j = 0; j < N; j++) {
                    g[i][j] = in.readInt();
                }
            }
        }
    }

    // number of teams
    public int numberOfTeams()  {
        return N;
    }     
                 
    // all teams
    public Iterable<String> teams() {
        return teams;
    }                        
 
    // number of wins for given team
    public int wins(String team)  {
        int i = map.get(team);
        return w[i];
    }                  

    // number of losses for given team
    public int losses(String team)  {
        int i = map.get(team);
        return l[i];
    }                    

    // number of remaining games for given team
    public int remaining(String team)  {               
      int i = map.get(team);
      return r[i];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {   
        return g[map.get(team1)][map.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) { 
        int indexOfEliminated = map.get(team);
        if (w[indexOfEliminated] + r[indexOfEliminated] < max) {
            return true;
        }
        int totalVertex = N * (N - 1) / 2 + 2 + N; // total N*(N + 1)/2 vertexes and (N - 1) * (N - 2) / 2 * 3 edges
        leftVertex = totalVertex - 1;
        rightVertex = totalVertex - 2;
        FlowNetwork network = new FlowNetwork(totalVertex);
        for (int i = 0; i < N; i++) {
            if (i != indexOfEliminated) {
                network.addEdge(new FlowEdge(i, rightVertex, w[indexOfEliminated] + r[indexOfEliminated] - w[i]));
            }
        }
        int count = N ;
        for (int i = 0; i < N; i++) {
            for (int j = i; j < N; j++) {
                if (i != indexOfEliminated && j != indexOfEliminated && i != j) {
                    network.addEdge(new FlowEdge(leftVertex, count, g[i][j]));
                    network.addEdge(new FlowEdge(count, i, g[i][j]));
                    network.addEdge(new FlowEdge(count, j, g[i][j]));
                    count++;
                }
            }
        }
        solution = new FordFulkerson(network, leftVertex, rightVertex);
        for (FlowEdge e : network.adj(leftVertex)) {
            if (e.capacity() != e.flow()) {
                return true;
            }
        }
        return false;
    }            

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team)  {
        Boolean eliminated = isEliminated(team);
        int indexOfEliminated = map.get(team);
        Queue<String> bag = new Queue<>();
        if (eliminated == true) {
            if (w[indexOfEliminated] + r[indexOfEliminated] < max) {
                bag.enqueue(teams.get(maxI));
            } else  {
                for (int i = 0; i < N; i++) {
                    if (i != indexOfEliminated) {
                        if (solution.inCut(i)) {
                            bag.enqueue(teams.get(i));
                        }
                    }
                }
            }
        }
        return bag;
    } 
     
    //unit testing
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
