package main.java;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;

public class Benchmarks {

  @State(Scope.Benchmark)
  public static class BenchState {
    public int[] data;
    public long sigOps;

    @Setup(Level.Invocation)
    public void doSetup() {
      this.data = Main.getBenchData(10_000, 314159265l);
      this.sigOps = 0;
    }
  }

  @Benchmark
  @BenchmarkMode(Mode.SampleTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void benchBubbleSort(BenchState state) {
    var sorter = new Main.BubbleSort();
    sorter.sort(state.data);
    state.sigOps = sorter.ops();
  }

  @Benchmark
  @BenchmarkMode(Mode.SampleTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void benchInsertionSort(BenchState state) {
    var sorter = new Main.InsertionSort();
    sorter.sort(state.data);
    state.sigOps = sorter.ops();
  }

  @Benchmark
  @BenchmarkMode(Mode.SampleTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void benchMergeSort(BenchState state) {
    var sorter = new Main.MergeSort();
    sorter.sort(state.data);
    state.sigOps = sorter.ops();
  }

  @Benchmark
  @BenchmarkMode(Mode.SampleTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void benchQuickSort(BenchState state) {
    var sorter = new Main.QuickSort();
    sorter.sort(state.data);
    state.sigOps = sorter.ops();
  }

  @Benchmark
  @BenchmarkMode(Mode.SampleTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void benchRadixSort(BenchState state) {
    var sorter = new Main.RadixSort();
    sorter.sort(state.data);
    state.sigOps = sorter.ops();
  }
}
