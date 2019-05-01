/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private Board initial_board;
    private Node min_node;
    private boolean solveable;

    public Solver(
            Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        if (initial == null) throw new java.lang.IllegalArgumentException();

        initial_board = initial;
        this.solveable();
    }

    private class Node implements Comparable<Node> {
        private Board board;
        private Node prev;
        private int num_moves;
        private int manhattan;
        private int priority;

        private Node(Board board, Node prev, int num_moves) {
            this.board = board;
            this.prev = prev;
            this.num_moves = num_moves;
            this.manhattan = board.manhattan();
            this.priority = this.manhattan + num_moves;
        }

        public int compareTo(Node that) {
            if (this.priority < that.priority) return -1;
            else if (this.priority > that.priority) return 1;
            else return this.manhattan - that.manhattan;
        }
    }

    private void solveable()            // is the initial board solvable?
    {
        MinPQ<Node> pq = new MinPQ<Node>();
        min_node = new Node(initial_board, null, 0);
        pq.insert(min_node);

        Board twin_board = initial_board.twin();
        MinPQ<Node> pq_twin = new MinPQ<Node>();
        Node min_node_twin = new Node(twin_board, null, 0);
        pq_twin.insert(min_node_twin);

        while (!min_node.board.isGoal() && !min_node_twin.board.isGoal()) {

            min_node = pq.delMin();
            min_node_twin = pq_twin.delMin();

            for (Board b : min_node.board.neighbors()) {
                if (min_node.prev == null) {
                    pq.insert(new Node(b, min_node, 1));
                }
                else {
                    if (!b.equals(min_node.prev.board))
                        pq.insert(new Node(b, min_node, min_node.num_moves + 1));
                }
            }

            for (Board b_twin : min_node_twin.board.neighbors()) {
                if (min_node_twin.prev == null)
                    pq_twin.insert(
                            new Node(b_twin, min_node_twin, 1));
                else {
                    if (!b_twin.equals(min_node_twin.prev.board))
                        pq_twin.insert(
                                new Node(b_twin, min_node_twin, min_node_twin.num_moves + 1));
                }
            }
        }
        if (min_node.board.isGoal()) solveable = true;
        else solveable = false;
    }

    public boolean isSolvable()            // is the initial board solvable?
    {
        return solveable;
    }

    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
        if (solveable) return min_node.num_moves;
        else return -1;
    }

    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        if (solveable) {
            Stack<Board> boards = new Stack<Board>();
            while (min_node.prev != null) {
                boards.push(min_node.board);
                min_node = min_node.prev;
            }
            boards.push(min_node.board); // push the initial board in
            return boards;
        }
        else return null;
    }

    public static void main(String[] args) // solve a slider puzzle (given below)
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
        // // solve the puzzle
        // Solver solver = new Solver(initial);
        //
        // // print solution to standard output
        // if (!solver.isSolvable())
        //     StdOut.println("No solution possible");
        // else {
        //     StdOut.println("Minimum number of moves = " + solver.moves());
        //     for (Board board : solver.solution())
        //         StdOut.println(board);
        // }

    }
}
