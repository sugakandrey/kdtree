package io.github.sugakandrey.kdtree;

import java.util.ArrayList;
import java.util.List;

public class NaiveSearchManager implements KRangeSearchManager {
  public final List<Point> points;

  public NaiveSearchManager(List<Point> points) {
    this.points = points;
  }

  @Override
  public List<Point> findInRange(KRange krange) {
    final ArrayList<Point> res = new ArrayList<>();
    for (Point point : points) {
      if (point.in(krange)) res.add(point);
    }
    return res;
  }

  @Override
  public Point nearestTo(Point p) {
    Point best = null;
    double bestDist = Double.MAX_VALUE;
    for (Point point : points) {
      double dist = point.distanceSqTo(p);
      if (dist < bestDist) {
        bestDist = dist;
        best = point;
      }
    }
    return best;
  }
}
