package io.github.sugakandrey.kdtree;

public class Range {
  public final int x;
  public final int y;

  public Range(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "Range{" +
            "x=" + x +
            ", y=" + y +
            '}';
  }

  private static final int INF = (int) 1e8;
  public static Range INF_RANGE = new Range(-INF, +INF);
}
