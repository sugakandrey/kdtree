package io.github.sugakandrey.kdtree;

import java.util.List;

public interface KRangeSearchManager {
  List<Point> findInRange(KRange krange);
  Point nearestTo(Point p);
}
