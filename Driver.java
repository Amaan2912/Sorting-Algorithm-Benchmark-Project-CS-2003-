package main.java;

import org.openjdk.jmh.runner.*;
import org.openjdk.jmh.runner.options.*;

public class Driver {
  public static void main(String[] args) throws RunnerException {
    Options opts = new OptionsBuilder().include(Benchmarks.class.getSimpleName()).result("benchmarks.dat").build();
    new Runner(opts).run();
  }
}
