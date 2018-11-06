package io.github.sugakandrey.kdtree;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by sugakandrey.
 */
@State(Scope.Benchmark)
public class SearchManagerTest {
  @Param({"0", "1", "2", "3", "4"})
  public int idx;
  private final Random rng = new Random();
  private KRangeSearchManager naiveSearcher;
  private KRangeSearchManager kdTreeSearcher;
  private List<List<Point>> points = new ArrayList<>();
  private Point from;
  private static final int dimensions = 300;

  {
    for (int i = 0; i < 5; i++) {
      List<Point> p = new ArrayList<>();
      int top = (int) Math.pow(10, i + 1);
      for (int j = 0; j < top; j++) {
        final List<Integer> coords = new ArrayList<>();
        for (int k = 0; k < dimensions; k++) {
          coords.add(rng.nextInt(200) - 100);
        }

        p.add(new Point(coords));
      }
      points.add(p);
    }
  }
  
  @Setup(Level.Trial)
  public void setUp() {
    naiveSearcher = new NaiveSearchManager(points.get(idx));
    kdTreeSearcher = new KDTreeSearchManager(dimensions, points.get(idx));
    System.out.println("setup finished");
  }
  
  @Setup(Level.Invocation)
  public void setUpRange() {
    List<Integer> coords = new ArrayList<>();
    for (int j = 0; j < dimensions; j++) {
      coords.add(rng.nextInt(200) - 100);
    }
    from = new Point(coords);
  }

  @Benchmark
  @BenchmarkMode(Mode.SingleShotTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void testRequestsNaive() {
    naiveSearcher.nearestTo(from);
  }
  
  @Benchmark
  @BenchmarkMode(Mode.SingleShotTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void testRequestKDTree() {
    kdTreeSearcher.nearestTo(from);
  }
}
