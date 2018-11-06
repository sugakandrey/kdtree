package io.github.sugakandrey.kdtree;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Point {
  public final List<Integer> coordinates;

  public Point(List<Integer> coordinates) {
    this.coordinates = coordinates;
  }

  boolean in(KRange kRange) {
    List<Range> boundaries = kRange.ranges;
    boolean check = true;

    for (int i = 0; i < boundaries.size(); i++) {
      Range b = boundaries.get(i);
      int c = coordinates.get(i);
      check &= b.x < c && b.y > c;
    }
    return check;
  }

  KRange toRange() {
    List<Range> boundaries = coordinates.stream().map(c -> new Range(c, c)).collect(Collectors.toList());
    return new KRange(boundaries);
  }

  @Override
  public String toString() {
    final String s = coordinates.toString();
    String coords = s.substring(1, s.length() - 1);
    return "new Point(" +
            "Arrays.asList(" + coords + ")" + ")";
            
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Point point = (Point) o;
    return Objects.equals(coordinates, point.coordinates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordinates);
  }
  
  public double distanceSqTo(Point p) {
    double res = 0;
    for (int i = 0; i < coordinates.size(); i++) {
      double diff = coordinates.get(i) - p.coordinates.get(i);
      res += diff * diff;
    }
    return res;
  }
}
