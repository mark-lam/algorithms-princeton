/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {
    private List<LineSegment> list;

    public FastCollinearPoints(
            Point[] points)     // finds all line segments containing 4 or more points
    {
        checkNullDupe(points);

        Point[] points_sorted = points.clone();
        Arrays.sort(points_sorted);
        list = new LinkedList<>();

        for (int i = 0; i < points_sorted.length; i++) {
            Point[] points_copy = points_sorted.clone();
            Arrays.sort(points_copy, points_sorted[i].slopeOrder());

            int count = 1;
            int front, rear;
            for (front = 1, rear = 2; rear < points_copy.length; rear++) {
                if (points_copy[front].slopeTo(points_copy[0]) == points_copy[rear]
                        .slopeTo(points_copy[0])) {
                    count++;
                }
                else if (count >= 3 && points_copy[0].compareTo(points_copy[front])
                        < 0) { // this line ensures the starting point (of the line segment) < the min point (in the segment), which ensures i) only segments in one direction are added,  ii) duplicates are not added
                    list.add(new LineSegment(points_copy[0], points_copy[rear - 1]));
                    count = 1;
                    front = rear;
                }
                else {
                    count = 1;
                    front = rear;
                }
            }
            if (count >= 3 && points_copy[0].compareTo(points_copy[front]) < 0) {
                list.add(new LineSegment(points_copy[0], points_copy[rear - 1]));
                count = 1;
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
        // FastCollinearPoints collinear = new FastCollinearPoints(points);
        // for (LineSegment segment : collinear.segments()) {
        //     StdOut.println(segment);
        //     segment.draw();
        // }
        // StdDraw.show();
    }
}
