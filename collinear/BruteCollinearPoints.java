/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BruteCollinearPoints {
    private List<LineSegment> list;

    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        checkNullDupe(points);

        Point[] points_copy = points.clone();
        Arrays.sort(points_copy);

        list = new LinkedList<>();
        for (int i = 0; i < points_copy.length; i++) {
            for (int j = i + 1; j < points_copy.length; j++) {
                for (int k = j + 1; k < points_copy.length; k++) {
                    for (int l = k + 1; l < points_copy.length; l++) {

                        if (points_copy[j].slopeTo(points_copy[i]) == points_copy[k]
                                .slopeTo(points_copy[i]) &&
                                points_copy[j].slopeTo(points_copy[i]) == points_copy[l]
                                        .slopeTo(points_copy[i])) {
                            list.add(new LineSegment(points_copy[i], points_copy[l]));
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments()        // the number of line segments
    {
        return list.size();
    }

    public LineSegment[] segments()                // the line segments
    {
        return list.toArray(new LineSegment[0]);
    }

    private void checkNullDupe(Point[] points) {
        if (points == null) {
            throw new java.lang.IllegalArgumentException();
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new java.lang.IllegalArgumentException();
        }
        for (int i = 0; i < points.length - 1; i++) {
            Point[] points_copy = points.clone();
            Arrays.sort(points_copy);
            if (points_copy[i].compareTo(points_copy[i + 1]) == 0)
                throw new java.lang.IllegalArgumentException();
        }
    }


    public static void main(String[] args) {
        // // read the n points from a file
        // In in = new In(args[0]);
        // int n = in.readInt();
        // Point[] points = new Point[n];
        // for (int i = 0; i < n; i++) {
        //     int x = in.readInt();
        //     int y = in.readInt();
        //     points[i] = new Point(x, y);
        // }
        //
        // // draw the points
        // StdDraw.enableDoubleBuffering();
        // StdDraw.setXscale(0, 32768);
        // StdDraw.setYscale(0, 32768);
        // for (Point p : points) {
        //     p.draw();
        // }
        // StdDraw.show();
        //
        // // print and draw the line segments
        // BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        // for (LineSegment segment : collinear.segments()) {
        //     StdOut.println(segment);
        //     segment.draw();
        // }
        // StdDraw.show();
    }
}
