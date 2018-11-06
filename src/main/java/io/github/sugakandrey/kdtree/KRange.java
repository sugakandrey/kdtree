package io.github.sugakandrey.kdtree;

import java.util.ArrayList;
import java.util.List;

public class KRange {
  public final List<Range> ranges;

  public KRange(List<Range> ranges) {
    this.ranges = ranges;
  }

  List<KRange> split(Point median, int coord) {
    int pivot = median.coordinates.get(coord);
    List<Range> leftBoundaries = new ArrayList<>(ranges);
    List<Range> rightBoundaries = new ArrayList<>(ranges);
    Range tmp = ranges.get(coord);
    leftBoundaries.set(coord, new Range(tmp.x, pivot));
    rightBoundaries.set(coord, new Range(pivot, tmp.y));
    KRange left = new KRange(leftBoundaries);
    KRange right = new KRange(rightBoundaries);
    return new ArrayList<KRange>() {{
      add(left);
      add(right);
    }};
  }

  public boolean subsetOf(KRange kRange) {
    for (int i = 0; i < ranges.size(); i++) {
      Range bound = ranges.get(i);
      Range oBound = kRange.ranges.get(i);
      boolean sub = oBound.x < bound.x && oBound.y > bound.y;
      if (!sub) return false;
    }
    return true;
  }

  public boolean intersects(KRange kRange) {
    for (int i = 0; i < ranges.size(); i++) {
      Range bound = ranges.get(i);
      Range oBound = kRange.ranges.get(i);
      if ((bound.x < oBound.y) && (oBound.x < bound.y)) return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return "KRange{" +
            "ranges=" + ranges +
            '}';
  }
}
