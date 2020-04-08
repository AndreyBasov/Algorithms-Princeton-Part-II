import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph digraph;
    private final int len;
    private int distances[];
    private BreadthFirstDirectedPaths bfs1;
    private BreadthFirstDirectedPaths bfs2;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Illegal");
        }
        digraph = new Digraph(G);
        len = digraph.V();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= digraph.V())
            throw new IllegalArgumentException("vertex");
        if (w < 0 || w >= digraph.V())
            throw new IllegalArgumentException("vertex");
        bfs1 = new BreadthFirstDirectedPaths(digraph, v);
        bfs2 = new BreadthFirstDirectedPaths(digraph, w);
        distances = new int[len];
        for (int i = 0; i < len; i++) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
                distances[i] = bfs1.distTo(i) + bfs2.distTo(i);
            }
            else {
                distances[i] = -1;
            }
        }
        int min = -1;
        for (int i = 0; i < len; i++) {
            if (distances[i] != -1 && (distances[i] < min || min == -1)) {
                min = distances[i];
            }
        }
        return min;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= digraph.V())
            throw new IllegalArgumentException("vertex");
        if (w < 0 || w >= digraph.V())
            throw new IllegalArgumentException("vertex");
        if (v == w) {
            return v;
        }
        bfs1 = new BreadthFirstDirectedPaths(digraph, v);
        bfs2 = new BreadthFirstDirectedPaths(digraph, w);
        distances = new int[len];
        for (int i = 0; i < len; i++) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
                distances[i] = bfs1.distTo(i) + bfs2.distTo(i);
            }
            else {
                distances[i] = -1;
            }
        }
        int ans = -1;
        int min = -1;
        for (int i = 0; i < len; i++) {
            if (distances[i] != -1 && (distances[i] < min || min == -1)) {
                min = distances[i];
                ans = i;
            }
        }
        return ans;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int min = -1, len;
        for (Integer inV: v) {
            if (inV.equals(null))
                throw new IllegalArgumentException("vertex");
            for (Integer inW: w) {
                if (inW.equals(null))
                    throw new IllegalArgumentException("vertex");
                len = length(inV, inW);
                if (len < min || (min == -1)) {
                    min = len;
                }
            }
        }
        return min;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int min = -1, len, anc = -1;
        for (Integer inV: v) {
            if (inV.equals(null))
                throw new IllegalArgumentException("vertex");
            for (Integer inW: w) {
                if (inW.equals(null))
                    throw new IllegalArgumentException("vertex");
                len = length(inV, inW);
                if (len  < min || (min == -1)) {
                    min = len;
                    anc = ancestor(inV, inW);
                }
            }
        }
        return anc;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
