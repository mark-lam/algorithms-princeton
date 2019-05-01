/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;

public class PointSET {
    private SET<Point2D> set;

    public PointSET()                               // construct an empty set of points
    {
        set = new SET<Point2D>();
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return set.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return set.size();
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new java.lang.IllegalArgumentException();

        if (!set.contains(p)) set.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new java.lang.IllegalArgumentException();

        return set.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : set) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new java.lang.IllegalArgumentException();

        List<Point2D> list = new LinkedList<>();
        for (Point2D p : set) {
            if (rect.contains(p)) list.add(p);
        }
        return list;
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new java.lang.IllegalArgumentException();
        if (set.isEmpty()) return null;

        double min_dist = 2.0;
        Point2D min_p = p;
        for (Point2D p_set : set) {
            double dist = p_set.distanceSquaredTo(p);
            if (dist < min_dist) {
                min_dist = dist;
                min_p = p_set;
            }
        }
        return min_p;
    }

    public static void main(
            String[] args)                  // unit testing of the methods (optional)
    {
        // Point2D test_p = new Point2D(0.1, 0.2);
        // Point2D test_p2 = new Point2D(0.3, 0.7);
        // PointSET test_ps = new PointSET();
        // test_ps.insert(test_p);
        // test_ps.insert(test_p2);
        // System.out.printf("Size: %s\n", test_ps.size());
        // RectHV test_r = new RectHV(0.0, 0.0, 0.2, 0.2);
        // for (Point2D p : test_ps.range(test_r)) {
        //     System.out.printf("In rectangle: %s\n", p.toString());
        // }
        // Point2D test_mid = new Point2D(0.5, 0.5);
        // System.out.printf("Nearest: %s\n", test_ps.nearest(test_mid).toString());
        //
        // test_ps.draw();
        // StdDraw.show();
    }
}
