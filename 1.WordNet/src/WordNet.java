import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.TreeMap;


public class WordNet {
    private final String Synsets;
    private final String Hypernyms;
    private final TreeMap<String, SET<Integer>> wordsSymTable; // найти по слову все вершины графа, в которых встречается это слово
    private final TreeMap<Integer, String> indToWord;
    private final SET<String> words; //просто набор слов
    private final Digraph digraph;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Illegal");
        }
        Synsets = synsets;
        Hypernyms = hypernyms;
        wordsSymTable = new TreeMap<>();
        indToWord = new TreeMap<>();
        words = new SET<>();
        In in = new In(Synsets);
        SET<Integer> set;
        String line, word = "";
        while (!in.isEmpty()) { //создаем соответствие и набор слов
            line = in.readLine();
            int beg = line.indexOf(',');
            int ind = Integer.parseInt(line.substring(0, beg));
            String synset = line.substring(beg + 1, line.indexOf(',', beg + 2));
            indToWord.put(ind, synset);
            for (int i = beg + 1; i < line.indexOf(',', beg + 2); i++) {
                if (line.charAt(i) != ' ') {
                    word += line.charAt(i);
                } else {
                    if (!words.contains(word)) {
                        set = new SET<Integer>();
                        set.add(ind);
                        wordsSymTable.put(word, set);
                    } else {
                        set = wordsSymTable.get(word);
                        set.add(ind);
                        wordsSymTable.replace(word, set);
                    }
                    words.add(word);
                    word = "";
                }
            }
            if (!words.contains(word)) {
                set = new SET<Integer>();
                set.add(ind);
                wordsSymTable.put(word, set);
            } else {
                set = wordsSymTable.get(word);
                set.add(ind);
                wordsSymTable.replace(word, set);
            }
            words.add(word);
            word = "";
        }

        In in2 = new In(Hypernyms);
        digraph = new Digraph(82192);
        while (!in2.isEmpty()) { //создаем граф
            line = in2.readLine();
            String arr[]= line.split(",");
            for (int i = 1; i < arr.length; i++) {
                digraph.addEdge(Integer.parseInt(arr[0]), Integer.parseInt(arr[i]));
            }
        }
        sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return words;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return words.contains(word);
    }

    // distance between nounA and nounB 
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Illegal");
        }
        return sap.length(wordsSymTable.get(nounA), wordsSymTable.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path 
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Illegal");
        }
        return indToWord.get(sap.ancestor(wordsSymTable.get(nounA), wordsSymTable.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String Synsets = "synsets.txt";
        String Hypernyms = "hypernyms.txt";
        WordNet net = new WordNet(Synsets, Hypernyms);
        String string1 = StdIn.readLine();
        String string2 = StdIn.readLine();
        StdOut.println(net.sap(string1, string2));
    }
}
