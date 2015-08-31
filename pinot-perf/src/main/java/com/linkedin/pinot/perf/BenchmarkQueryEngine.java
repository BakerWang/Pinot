package com.linkedin.pinot.perf;

import com.google.common.util.concurrent.Uninterruptibles;
import com.linkedin.pinot.core.segment.index.SegmentMetadataImpl;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;


/**
 * FIXME Document me!
 *
 * @author jfim
 */
@State(Scope.Benchmark)
public class BenchmarkQueryEngine {
  private static String _tableName = "sTest_OFFLINE";
  private static String _dataDirectory = "/Users/jfim/index_dir";

  PerfBenchmarkDriver _perfBenchmarkDriver;

  @Setup
  public void startPinot() throws Exception {
    System.out.println("Using table name " + _tableName);
    System.out.println("Using data directory " + _dataDirectory);
    System.out.println("Starting pinot");

    PerfBenchmarkDriverConf conf = new PerfBenchmarkDriverConf();
    conf.setStartBroker(true);
    conf.setStartController(true);
    conf.setStartServer(true);
    conf.setStartZookeeper(true);
    conf.setUploadIndexes(false);
    conf.setRunQueries(false);
    conf.setServerInstanceSegmentTarDir(null);
    conf.setServerInstanceDataDir(_dataDirectory);
    conf.setConfigureResources(false);
    _perfBenchmarkDriver = new PerfBenchmarkDriver(conf);
    _perfBenchmarkDriver.run();

    Set<String> tables = new HashSet<String>();
    File[] segments = new File(_dataDirectory, _tableName).listFiles();
    for (File segmentDir : segments) {
      SegmentMetadataImpl segmentMetadata = new SegmentMetadataImpl(segmentDir);
      if (!tables.contains(segmentMetadata.getTableName())) {
        _perfBenchmarkDriver.configureTable(segmentMetadata.getTableName());
        tables.add(segmentMetadata.getTableName());
      }
      System.out.println("Adding segment " + segmentDir.getAbsolutePath());
      _perfBenchmarkDriver.addSegment(segmentMetadata);
    }

    System.out.println("Waiting for 10s for everything to be loaded");
    Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
  }

  @Benchmark
  @BenchmarkMode({Mode.SampleTime})
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public int sendQueryToPinot() throws Exception {
    JSONObject returnValue = _perfBenchmarkDriver.postQuery("select count(*) from sTest group by daysSinceEpoch");
    if (returnValue.getInt("numDocsScanned") != 25000000 || returnValue.getInt("totalDocs") != 25000000) {
      System.out.println("returnValue = " + returnValue);
      throw new RuntimeException("Unexpected number of docs scanned/total docs");
    }
    return returnValue.getInt("totalDocs");
  }

  public static void main(String[] args) throws Exception {

    Options opt = new OptionsBuilder()
        .include(BenchmarkQueryEngine.class.getSimpleName())
        .forks(1)
        .warmupTime(TimeValue.seconds(6))
        .warmupIterations(10)
        .measurementTime(TimeValue.seconds(6))
        .measurementIterations(10)
        .build();

    new Runner(opt).run();
  }
}
