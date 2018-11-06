package io.github.sugakandrey.kdtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KDTree {
  public final int k;
  private final NodeT root;

  public KDTree(int k, List<Point> points) {
    this.k = k;
    List<Range> boundaries = new ArrayList<Range>();
    for (int i = 0; i < k; i++) {
      boundaries.add(Range.INF_RANGE);
    }
    KRange kRange = new KRange(boundaries);
    root = initTree(points, kRange, 0);
  }

  public List<Point> find(KRange kRange) {
    final ArrayList<Point> res = new ArrayList<>();
    root.pointsIn(kRange, res);
    return res;
  }
  
  public Point nearest(Point p) {
    return nearest(root, p, root.median);
  }
  
  private Point nearest(NodeT node, Point p, Point best) {
    if (node.median.distanceSqTo(p) < best.distanceSqTo(p)) {
      best = node.median;
    }

    if (node.median.equals(p)) return p;
    if (node instanceof Leaf) return best;

    Node notLeaf = (Node) node;
    double toPartition = toPartition(p, notLeaf);
    
    if (toPartition < 0) {
      best = nearest(notLeaf.leftSibling, p, best);
      if (best.distanceSqTo(p) >= Math.pow(toPartition, 2)) 
        best = nearest(notLeaf.rightSibling, p, best);
    } else {
      best = nearest(notLeaf.rightSibling, p, best);
      
      if (best.distanceSqTo(p) >= Math.pow(toPartition, 2))
        best = nearest(notLeaf.leftSibling, p, best);
    }
    return best;
  }

  private double toPartition(Point p, Node n) {
    int coord = n.medianCoord;
    return p.coordinates.get(coord) - n.median.coordinates.get(coord);
  }

  private List<List<Point>> split(List<Point> points, int coord) {
    points.sort(Comparator.comparing(p -> p.coordinates.get(coord)));
    final int idx = points.size() / 2;
    Point median = points.get(idx);
    
    final List<Point> finalLeft = points.subList(0, idx);
    final List<Point> finalRight = points.subList(idx, points.size());
    return new ArrayList<List<Point>>() {{
      add(Collections.singletonList(median));
      add(finalLeft);
      add(finalRight);
    }};
  }

  private NodeT initTree(List<Point> points, KRange kRange, int coord) {
    if (points.size() == 0) return null;
    else if (points.size() == 1) {
      Point p = points.get(0);
      KRange r = p.toRange();
      return new Leaf(p, r);
    } else {
      List<List<Point>> siblings = split(points, coord);
      int nDepth = (coord + 1) % points.get(0).coordinates.size();
      Point median = siblings.get(0).get(0);
      List<KRange> ranges = kRange.split(median, coord);
      NodeT left = initTree(siblings.get(1), ranges.get(0), nDepth);
      NodeT right = initTree(siblings.get(2), ranges.get(1), nDepth);
      return new Node(points, median, coord, kRange, left, right);
    }
  }

  private static abstract class NodeT {
    public final Point median;
    public final KRange range;

    private NodeT(Point median, KRange range) {
      this.median = median;
      this.range = range;
    }

    abstract void pointsIn(KRange kRange, ArrayList<Point> res);
  }

  private static class Node extends NodeT {
    public final NodeT leftSibling;
    public final NodeT rightSibling;
    public final List<Point> points;
    public final int medianCoord;

    private Node(List<Point> points, Point median, int medianCoord, KRange range, NodeT leftSibling1, NodeT rightSibling1) {
      super(median, range);
      this.medianCoord = medianCoord;
      this.points = points;
      this.leftSibling = leftSibling1;
      this.rightSibling = rightSibling1;
    }

    @Override
    void pointsIn(final KRange kRange, ArrayList<Point> res) {
      if (range.subsetOf(kRange)) {
        res.addAll(points);
      } else {
        if (rightSibling != null && rightSibling.range.intersects(kRange)) rightSibling.pointsIn(kRange, res);
        if (leftSibling != null && leftSibling.range.intersects(kRange)) leftSibling.pointsIn(kRange, res);
      }
    }
  }

  private static class Leaf extends NodeT {
    private Leaf(Point median, KRange range) {
      super(median, range);
    }

    @Override
    void pointsIn(KRange kRange, ArrayList<Point> res) {
      if (median.in(kRange)) res.add(median);
    }
  }
}
