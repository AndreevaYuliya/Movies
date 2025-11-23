package io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class XmlStatisticsWriter {

    private XmlStatisticsWriter() { }

    public static void writeStatistics(Map<String, Long> statistics,
                                       String attributeName,
                                       Path outputDirectory) throws IOException {

        String fileName = "statistics_by_" + attributeName + ".xml";
        Path outputFile = outputDirectory.resolve(fileName);

        List<Map.Entry<String, Long>> sorted = statistics.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .toList();

        try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {
            writer.write("<statistics>");
            writer.newLine();

            for (var entry : sorted) {
                writer.write("  <item>");
                writer.newLine();

                writer.write("    <value>");
                writer.write(escapeXml(entry.getKey()));
                writer.write("</value>");
                writer.newLine();

                writer.write("    <count>");
                writer.write(entry.getValue().toString());
                writer.write("</count>");
                writer.newLine();

                writer.write("  </item>");
                writer.newLine();
            }

            writer.write("</statistics>");
            writer.newLine();
        }
    }

    private static String escapeXml(String value) {
        if (value == null) return "";
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
