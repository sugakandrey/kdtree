package io.github.sugakandrey.kdtree;

import java.util.List;

public class KDTreeSearchManager implements KRangeSearchManager {
  private final int k;
  private final List<Point> points;
  private final KDTree tree;

  public KDTreeSearchManager(int k, List<Point> points) {
    this.k = k;
    this.points = points;
    tree = new KDTree(this.k, this.points);
  }


  @Override
  public List<Point> findInRange(KRange krange) {
    return tree.find(krange);
  }

  @Override
  public Point nearestTo(Point p) {
    return tree.nearest(p);
  }
}
