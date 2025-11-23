package stats;

import parsing.MovieJsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public final class StatisticsCalculator {

    private StatisticsCalculator() {}

    public static Map<String, Long> calculate(Path folder,
                                              AttributeExtractor extractor,
                                              int threadCount) throws Exception {

        List<Path> files;
        try (var stream = Files.list(folder)) {
            files = stream.filter(p -> p.toString().endsWith(".json")).toList();
        }

        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {

            try {
                List<Future<Map<String, Long>>> tasks = new ArrayList<>();

                for (Path file : files) {
                    tasks.add(executor.submit(() -> process(file, extractor)));
                }

                Map<String, Long> aggregated = new HashMap<>();
                for (Future<Map<String, Long>> f : tasks) {
                    Map<String, Long> part = f.get();
                    merge(aggregated, part);
                }

                return aggregated;
            } finally {
                executor.shutdown();
            }
        }
    }

    private static Map<String, Long> process(Path file,
                                             AttributeExtractor extractor) throws IOException {
        Map<String, Long> stats = new HashMap<>();
        try (InputStream in = Files.newInputStream(file)) {
            MovieJsonParser.parseFile(in, extractor, stats);
        }
        return stats;
    }

    private static void merge(Map<String, Long> target, Map<String, Long> src) {
        for (var e : src.entrySet()) {
            target.merge(e.getKey(), e.getValue(), Long::sum);
        }
    }
}
