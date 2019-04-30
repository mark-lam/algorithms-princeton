/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int size, top_index, bottom_index;
    private WeightedQuickUnionUF conn_array;
    private boolean[] open_array;

    public Percolation(int n)                // create n-times-n array, with all sites blocked
    {
        if (n <= 0) throw new java.lang.IllegalArgumentException();
        size = n;
        conn_array = new WeightedQuickUnionUF(size * size + 2);
        open_array = new boolean[size * size];

        top_index = size * size;
        bottom_index = size * size + 1;

        // connect top row to virtual top node
        for (int i = 0; i < size; i++) {
            conn_array.union(top_index, i);
        }

        // connect bottom row to virtual bottom node
        for (int i = 0; i < size; i++) {
            conn_array.union(bottom_index, size * size - size + i);
        }
    }

    private int xyToIndex(int row, int col) {
        return (row - 1) * size + (col - 1);
    }

    public void open(int row, int col)    // open site (row, col) if it is not open already
    {
        if (row > size || col > size || row <= 0 || col <= 0)
            throw new java.lang.IllegalArgumentException();

        if (!isOpen(row, col)) {
            open_array[xyToIndex(row, col)] = true;

            // connect to neighbors
            int lower_col = Math.max(1, col - 1);
            int higher_col = Math.min(size, col + 1);
            int lower_row = Math.max(1, row - 1);
            int higher_row = Math.min(size, row + 1);

            if (isOpen(row, lower_col))
                conn_array.union(xyToIndex(row, col), xyToIndex(row, lower_col));
            if (isOpen(row, higher_col))
                conn_array.union(xyToIndex(row, col), xyToIndex(row, higher_col));
            if (isOpen(lower_row, col))
                conn_array.union(xyToIndex(row, col), xyToIndex(lower_row, col));
            if (isOpen(higher_row, col))
                conn_array.union(xyToIndex(row, col), xyToIndex(higher_row, col));
        }
    }

    public boolean isOpen(int row, int col)  // is site (row, col) open?
    {
        if (row > size || col > size || row <= 0 || col <= 0)
            throw new java.lang.IllegalArgumentException();
        return open_array[xyToIndex(row, col)];
    }

    public boolean isFull(int row, int col)  // is site (row, col) full?
    {
        if (row > size || col > size || row <= 0 || col <= 0)
            throw new java.lang.IllegalArgumentException();
        return (conn_array.connected(top_index, xyToIndex(row, col)) && isOpen(row, col));
    }

    public int numberOfOpenSites()       // number of open sites
    {
        int num_open = 0;
        for (int i = 0; i < open_array.length; i++) {
            if (open_array[i] == true) num_open += 1;
        }

        return num_open;
    }

    public boolean percolates()              // does the system percolate?
    {
        return conn_array.connected(top_index, bottom_index);
    }

    public static void main(String[] args) {

        // int n = Integer.parseInt(args[0]);
        // Percolation grid = new Percolation(n);
        // grid.open(2, 1);
        // grid.open(2, 2);
        // StdOut.printf("Test output: %s",
        //               grid.conn_array.connected(grid.xyToIndex(2, 1), grid.xyToIndex(2, 2)));
    }

}
