import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet net;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet)  {       
        net = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns)   {
        int max = -1;
        int dist, ans = -1;
        for (int i = 0; i < nouns.length; i++) {
            dist = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (i != j) {
                    dist += net.distance(nouns[i], nouns[j]);
                }
            }
            if (dist > max) {
                max = dist;
                ans = i;
            }
        }
        return nouns[ans];
    } 
    
    // unit testing
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
