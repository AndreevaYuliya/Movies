package app;

import io.XmlStatisticsWriter;
import stats.AttributeExtractor;
import stats.StatisticsCalculator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.err.println("""
                Usage: java -jar movie-statistics.jar <folder> <attribute> [threads]
                Attributes: director | year | genres
                """);
            System.exit(1);
        }

        Path folder = Path.of(args[0]);
        String attribute = args[1];
        int threads = args.length == 3 ? Integer.parseInt(args[2]) : 4;

        if (!Files.isDirectory(folder)) {
            System.err.println("Folder not found: " + folder);
            System.exit(1);
        }

        try {
            var extractor = AttributeExtractor.forAttribute(attribute);

            long start = System.currentTimeMillis();
            Map<String, Long> stats =
                    StatisticsCalculator.calculate(folder, extractor, threads);
            long end = System.currentTimeMillis();

            XmlStatisticsWriter.writeStatistics(stats, attribute, folder);

            System.out.printf(
                    "Done! statistics_by_%s.xml saved. Time: %d ms, threads: %d%n",
                    attribute, (end - start), threads);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
    }
}
