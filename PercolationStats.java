/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] p_array;
    private int t;
    private double mu, sigma;

    public PercolationStats(int n,
                            int trials)    // perform trials independent experiments on an n-by-n grid
    {
        if (n <= 0 || trials <= 0) throw new java.lang.IllegalArgumentException();

        p_array = new double[trials];
        t = trials;

        for (int i = 0; i < trials; i++) {
            Percolation grid = new Percolation(n);
            while (!grid.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                grid.open(row, col);
            }
            double num_open = grid.numberOfOpenSites();
            p_array[i] = num_open / (n * n);
        }

        mu = mean();
        sigma = stddev();
    }

    public double mean()                          // sample mean of percolation threshold
    {
        return StdStats.mean(p_array);
    }

    public double stddev()                        // sample standard deviation of percolation threshold
    {
        return StdStats.stddev(p_array);
    }

    public double confidenceLo()                  // low  endpoint of 95% confidence interval
    {
        return mu - 1.96 * sigma / Math.sqrt(t);
    }

    public double confidenceHi()                  // high endpoint of 95% confidence interval
    {
        return mu + 1.96 * sigma / Math.sqrt(t);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, t);

        String confidence = ps.confidenceLo() + ", " + ps.confidenceHi();
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + confidence);
    }
}
