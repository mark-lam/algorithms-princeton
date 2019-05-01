/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private int count;
    private Node root;
    private Point2D closest = null;

    public KdTree()                               // construct an empty set of points
    {
        root = null;
    }

    private static class Node implements Comparable<Node> {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        private Node(Point2D p) {
            this.p = p;
            this.rect = null;
            this.lb = null;
            this.rt = null;
        }

        public int compareTo(Node that) {
            return this.p.compareTo(that.p);
        }
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return count == 0;
    }

    public int size()                         // number of points in the set
    {
        return count;
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new java.lang.IllegalArgumentException();

        root = put(root, p, true);
        if (count == 0) root.rect = new RectHV(0, 0, 1, 1);
        count++;
    }

    private Node put(Node n, Point2D p, boolean vertical) {
        if (n == null) return new Node(p); // this line terminates recursion

        int cmp = compare(n, p, vertical);
        vertical = !vertical; // flip orientation for next recursion

        if (cmp < 0) {
            n.lb = put(n.lb, p, vertical);
            // deduce formulas required for rectangles in 4 total scenarios (vertical/!vertical vs. >0/<0)
            if (!vertical && n.lb.rect
                    == null) { // if previous recursion was vertical i.e. vertical->!vertical
                n.lb.rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.p.x(), n.rect.ymax());
            }
            else if (vertical && n.lb.rect == null) {
                n.lb.rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.rect.xmax(), n.p.y());
            }
        }
        else if (cmp > 0) {
            n.rt = put(n.rt, p, vertical);
            if (!vertical && n.rt.rect
                    == null) { // if previous recursion was vertical i.e. vertical->!vertical
                n.rt.rect = new RectHV(n.p.x(), n.rect.ymin(), n.rect.xmax(), n.rect.ymax());
            }
            else if (vertical && n.rt.rect == null) {
                n.rt.rect = new RectHV(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.rect.ymax());
            }
        }
        else if (cmp == 0) {
            if (p.y() == n.p.y() && p.x() == n.p.x()) { // if duplicate point
                count--;
                return n; // ignore the point, return the root node
            }
            else {
                n.lb = put(n.lb, p, vertical); // go left if cmp==0 and point is not duplicate
                if (!vertical && n.lb.rect
                        == null) // reuse formulas from cmp < 0
                    n.lb.rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.p.x(), n.rect.ymax());
                else if (vertical && n.lb.rect == null)
                    n.lb.rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.rect.xmax(), n.p.y());
            }
        }

        return n;
    }

    private int compare(Node n, Point2D p, boolean vertical) {
        int cmp;
        if (vertical) { // vertical node: compare x coordinates
            if (p.x() < n.p.x()) cmp = -1;
            else if (p.x() > n.p.x()) cmp = 1;
            else cmp = 0;
        }
        else { // horizontal node: compare y coordinates
            if (p.y() < n.p.y()) cmp = -1;
            else if (p.y() > n.p.y()) cmp = 1;
            else cmp = 0;
        }
        return cmp;
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new java.lang.IllegalArgumentException();

        Node n = root;
        boolean vertical = true;

        while (n != null) {
            int cmp = compare(n, p, vertical);
            vertical = !vertical; // flip orientation for next iteration

            if (cmp < 0) n = n.lb;
            else if (cmp > 0) n = n.rt;
            else if (cmp == 0) {
                if (p.y() == n.p.y() && p.x() == n.p.x()) {
                    return true;
                }
                else {
                    n = n.lb;
                }
            }
        }
        return false;
    }

    public String toString() {
        Queue<Node> q = new Queue<>();
        inorder(root, q);

        StringBuilder s = new StringBuilder();
        for (Node n : q) {
            s.append(n.p.toString() + " ");
            s.append("with rect: " + n.rect.toString() + "\n");
        }
        return s.toString();
    }

    private void inorder(Node n, Queue<Node> q) {
        if (n == null) return;
        inorder(n.lb, q);
        q.enqueue(n);
        inorder(n.rt, q);
    }

    public void draw()                         // draw all points to standard draw (ignoring line segments)
    {
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        Queue<Node> q = new Queue<>();
        inorder(root, q);

        for (Node n : q) {
            n.p.draw();
        }
    }

    private void range_inorder(Node n, RectHV rect, Queue<Point2D> q) {
        if (n == null) return;
        if (!rect.intersects(n.rect)) return;

        range_inorder(n.lb, rect, q);
        if (rect.contains(n.p)) q.enqueue(n.p);
        range_inorder(n.rt, rect, q);
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new java.lang.IllegalArgumentException();

        Queue<Point2D> q = new Queue<>();
        range_inorder(root, rect, q);
        return q;
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new java.lang.IllegalArgumentException();

        if (isEmpty()) return null;
        else {
            closest = root.p;
            nearest_inorder(root, p);
            return closest;
        }
    }

    private void nearest_inorder(Node n, Point2D p) {
        if (n == null) return;
        if (n.rect.distanceSquaredTo(p) > closest.distanceSquaredTo(p)) return;

        if (n.lb != null && n.lb.rect.contains(p)) {
            nearest_inorder(n.lb, p);
            if (n.p.distanceSquaredTo(p) < closest.distanceSquaredTo(p)) closest = n.p;
            nearest_inorder(n.rt, p);
        }
        else if (n.rt != null && n.rt.rect.contains(p)) {
            nearest_inorder(n.rt, p);
            if (n.p.distanceSquaredTo(p) < closest.distanceSquaredTo(p)) closest = n.p;
            nearest_inorder(n.lb, p);
        }
        else {
            nearest_inorder(n.lb, p);
            if (n.p.distanceSquaredTo(p) < closest.distanceSquaredTo(p)) closest = n.p;
            nearest_inorder(n.rt, p);
        }
    }

    public static void main(
            String[] args)                  // unit testing of the methods (optional)
    {
        // KdTree tree = new KdTree();
        // tree.insert(new Point2D(0.7, 0.2));
        // tree.insert(new Point2D(0.5, 0.4));
        // tree.insert(new Point2D(0.2, 0.3));
        // tree.insert(new Point2D(0.4, 0.7));
        // tree.insert(new Point2D(0.9, 0.6));
        // tree.insert(new Point2D(0.71, 0.9));
        // tree.insert(new Point2D(0.7, 0.2));
        // System.out.printf("Size: %d\n", tree.size());
        // System.out.printf("Contains: %s\n", tree.contains(new Point2D(0, 0)));
        // System.out.printf("Tree contents:\n%s\n", tree.toString());
        //
        // RectHV test_rect = new RectHV(0, 0, 0.4, 0.4);
        // for (Point2D p : tree.range(test_rect))
        //     System.out.printf("Found in test_rect: %s\n", p.toString());
        //
        // Point2D test_point = new Point2D(0.69, 0.9);
        // System.out.printf("Nearest: %s\n", tree.nearest(test_point).toString());
        //
        // tree.draw();
        // StdDraw.show();

        // In in = new In(args[0]);
        // int n = Integer.parseInt(args[1]);
        // KdTree kdtree = new KdTree();
        //
        // for (int i = 0; i < n; i++) {
        //     Point2D p = new Point2D(in.readDouble(), in.readDouble());
        //     kdtree.insert(p);
        // }
        //
        // kdtree.nearest(new Point2D(0.81, 0.3));
    }
}
