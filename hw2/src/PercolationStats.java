import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import java.util.concurrent.TimeUnit;
import com.google.common.base.Stopwatch;


public class PercolationStats {
    private final double mean;
    private final double stddev;
    private final double T;

    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        this.T = T;
        double[] ratio = new double[T];
        for (int i = 0; i < T; i += 1) {
            Percolation p = new Percolation(N);
            while (!p.percolates()) {
                int randRow = StdRandom.uniform(N);
                int randCol = StdRandom.uniform(N);
                p.open(randRow, randCol);
            }
            ratio[i] = ((double) p.numberOfOpenSites()) / (N * N);
        }

        this.mean = StdStats.mean(ratio);
        this.stddev = StdStats.stddev(ratio);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLow() {
        return mean - 1.96 * stddev / Math.sqrt(T);
    }

    public double confidenceHigh() {
        return mean + 1.96 * stddev / Math.sqrt(T);
    }

    public static void main(String[] args) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        int trials = 100, gridSize = 50;
        PercolationStats ps = new PercolationStats(gridSize, trials);
        stopwatch.stop();
        long elapsedMillis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println("gridSize is: " + gridSize + ". trials is: " + trials + ". Total running time: " + elapsedMillis + " milliseconds");
        System.out.printf("Grid Size: %d x %d | Number of Trials: %d%n", gridSize, gridSize, trials);
        System.out.printf("The mean percolation threshold is %.2f%n", ps.mean());
        System.out.printf("The standard deviation of the percolation threshold is %.2f.%n", ps.stddev());
        System.out.printf("The 95%% confidence interval is [%.3f, %.3f].%n", ps.confidenceLow(), ps.confidenceHigh());
    }
}
