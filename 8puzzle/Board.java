/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {
    private int[][] tiles;

    public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
    {                                      // (where blocks[i][j] = block in row i, column j)
        tiles = deepCopy(blocks);
    }

    private int goal(int i, int j) {
        return i * tiles.length + (j + 1);
    }

    public int dimension()                 // board dimension n
    {
        return tiles.length;
    }

    public int hamming()                   // number of blocks out of place
    {
        int count = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                // ignore if we arrive at the last tile (0 in goal need not be evaluated; 0 in tiles[][] should be evaluated)
                if (tiles[i][j] != goal(i, j) && goal(i, j) != tiles.length * tiles.length) count++;
            }
        }
        return count;
    }

    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        int dist = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j]
                        != 0) // skip if we encounter 0 in tiles[][] (there is no correct location for blank tile)
                {
                    int i_target = (tiles[i][j] - 1) / tiles.length;
                    int j_target = (tiles[i][j] - 1) - i_target * tiles.length;
                    dist += ((i - i_target < 0 ? i_target - i : i - i_target)) + ((
                            j - j_target < 0 ?
                            j_target - j :
                            j - j_target));
                }
            }
        }
        return dist;
    }

    public boolean isGoal()                // is this board the goal board?
    {
        return hamming() == 0;
    }

    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        int[][] tiles_copy = deepCopy(tiles);
        Board twin;
        if (tiles[0][0] != 0) {
            int temp_int = tiles_copy[0][0];
            tiles_copy[0][0] = tiles_copy[0][1]; // switch two tiles in the horizontal axis
            tiles_copy[0][1] = temp_int;
        }
        else {
            int temp_int = tiles_copy[1][0]; // if first tile is 0, move down
            tiles_copy[1][0] = tiles_copy[1][1]; // switch two tiles in the horizontal axis
            tiles_copy[1][1] = temp_int;
        }
        twin = new Board(tiles_copy);
        return twin;
    }

    public boolean equals(Object y)        // does this board equal y?
    {
        if (y == null) return false;
        if (y == this) return true;
        if (y.getClass() != this.getClass()) return false;
        return this.toString().equals(y.toString());
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
        Stack<Board> neighbors = new Stack<Board>();

        int i = 0;
        int j = 0;

        for (int p = 0; p < tiles.length; p++) {
            for (int q = 0; q < tiles.length; q++) {
                if (tiles[p][q] == 0) {
                    i = p;
                    j = q;
                }
            }
        }

        // there are a maximum of 4 neighbors (up, down, left, right)
        int i_up = i - 1;
        int j_up = j;
        int i_dn = i + 1;
        int j_dn = j;
        int i_l = i;
        int j_l = j - 1;
        int i_r = i;
        int j_r = j + 1;

        int[][] neighbor_tiles;

        if (i_up >= 0) {
            neighbor_tiles = deepCopy(tiles);
            neighbor_tiles[i][j] = neighbor_tiles[i_up][j_up];
            neighbor_tiles[i_up][j_up] = 0;
            Board neighbor = new Board(neighbor_tiles);
            neighbors.push(neighbor);
        }

        if (i_dn <= tiles.length - 1) {
            neighbor_tiles = deepCopy(tiles);
            neighbor_tiles[i][j] = neighbor_tiles[i_dn][j_dn];
            neighbor_tiles[i_dn][j_dn] = 0;
            Board neighbor = new Board(neighbor_tiles);
            neighbors.push(neighbor);
        }

        if (j_l >= 0) {
            neighbor_tiles = deepCopy(tiles);
            neighbor_tiles[i][j] = neighbor_tiles[i_l][j_l];
            neighbor_tiles[i_l][j_l] = 0;
            Board neighbor = new Board(neighbor_tiles);
            neighbors.push(neighbor);
        }

        if (j_r <= tiles.length - 1) {
            neighbor_tiles = deepCopy(tiles);
            neighbor_tiles[i][j] = neighbor_tiles[i_r][j_r];
            neighbor_tiles[i_r][j_r] = 0;
            Board neighbor = new Board(neighbor_tiles);
            neighbors.push(neighbor);
        }

        return neighbors;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(tiles.length + "\n");
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private static int[][] deepCopy(int[][] original) {
        int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

    public static void main(String[] args) // unit tests (not graded)
    {
        // // create initial board from file
        // In in = new In(args[0]);
        // int n = in.readInt();
        // int[][] blocks = new int[n][n];
        // for (int i = 0; i < n; i++)
        //     for (int j = 0; j < n; j++)
        //         blocks[i][j] = in.readInt();
        // Board initial = new Board(blocks);
        //
        // In in2 = new In("puzzle02.txt");
        // int n2 = in2.readInt();
        // int[][] blocks2 = new int[n2][n2];
        // for (int i = 0; i < n2; i++)
        //     for (int j = 0; j < n2; j++)
        //         blocks2[i][j] = in2.readInt();
        // Board next = new Board(blocks2);
        //
        // System.out.printf("Hamming: %d\n", initial.hamming());
        // System.out.printf("Manhattan: %d\n", initial.manhattan());
        // System.out.printf("Equal test: %s\n", initial.equals(next));
        //
        // for (Board b : initial.neighbors()) {
        //     System.out.printf("%s\n", b.toString());
        // }
        //
        // System.out.printf("Twin: %s\n", initial.twin().toString());

    }
}
