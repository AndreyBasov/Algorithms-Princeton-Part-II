import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieST;
import java.util.BitSet;

public class BoggleSolver {
    private BoggleBoard board;
    private final TrieST<Integer> trie; // solve using a trie
    private SET<String> words;

    // Initializes the data structure using the given array of strings as the dictionary.
    public BoggleSolver(String[] dictionary) {
        trie = new TrieST<>();
        for (int i = 0; i < dictionary.length; i++) {
            for (int j = 0; j < dictionary[i].length() - 1; j++) {
                if (!trie.contains(dictionary[i].substring(0, j + 1))) {
                    trie.put(dictionary[i].substring(0, j + 1), 0);
                }
            }
            int val;
            switch (dictionary[i].length()) {
                case 0: case 1: case 2:
                    val = 0;
                    break;
                case 3: case 4:
                    val = 1;
                    break;
                case 5:  val = 2;
                    break;
                case 6:  val = 3;
                    break;
                case 7:  val = 5;
                    break;
                default: val = 11;
                    break;
            }
            if (val > 0) {
                trie.put(dictionary[i], val);
            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board1) {
        words = new SET<>();
        this.board = board1;
        BitSet set = new BitSet(board.rows() * board.cols()); // a[i][j] == 0 => haven't been to [i][j]
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                recWord("", set, i, j);
            }
        }
        return words;
    }

    // recursively go through the word to check if it's valid
    private void recWord(String word, BitSet oldSet, int i, int j) {  // i is a row, j is a col
        if (!(board.getLetter(i, j) == 'Q')) {  // case of QU
            word += board.getLetter(i, j);
        } else {
            word += board.getLetter(i, j) + "U";
        }
        int key;
        try {
            key = trie.get(word);
        } catch (NullPointerException e) {
            return;
        }
        if (key != 0) {
            words.add(word);
        }
        BitSet set = (BitSet) oldSet.clone();
        set.set(i * board.cols() + j);
        if (i !=  0) {
            if (!set.get((i - 1) * board.cols() + j)) {
                recWord(word, set, i - 1, j);
            }
        }
        if (i !=  board.rows() - 1) {
            if (!set.get((i + 1) * board.cols() + j)) {
                recWord(word, set, i + 1, j);
            }
        }
        if (j !=  0) {
            if (!set.get(i * board.cols() + j - 1)) {
                recWord(word, set, i, j - 1);
            }
        }
        if (j !=  board.cols() - 1) {
            if (!set.get(i * board.cols() + j + 1)) {
                recWord(word, set, i, j + 1);
            }
        }
        if (i != 0 && j != 0) {
            if (!set.get((i - 1) * board.cols() + j - 1)) {
                recWord(word, set, i - 1, j - 1);
            }
        }
        if (i != board.rows() - 1 && j != board.cols() - 1) {
            if (!set.get((i + 1) * board.cols() + j + 1)) {
                recWord(word, set, i + 1, j + 1);
            }
        }
        if (i != 0 && j != board.cols() - 1) {
            if (!set.get((i - 1) * board.cols() + j + 1)) {
                recWord(word, set, i - 1, j + 1);
            }
        }
        if (i != board.rows() - 1 && j != 0) {
            if (!set.get((i + 1) * board.cols() + j - 1)) {
                recWord(word, set, i + 1, j - 1);
            }
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    public int scoreOf(String word) {
        int key;
        try {
            key = trie.get(word);
        } catch (NullPointerException e) {
            return 0;
        }
        if (key != 0) {
            return key;
        }
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
