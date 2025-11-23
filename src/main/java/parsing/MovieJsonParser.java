package parsing;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import model.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public final class MovieJsonParser {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    private MovieJsonParser() {}

    public static void parseFile(InputStream in,
                                 stats.AttributeExtractor extractor,
                                 Map<String, Long> stats) throws IOException {

        try (JsonParser parser = JSON_FACTORY.createParser(in)) {
            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new IOException("Expected root array");
            }

            while (parser.nextToken() == JsonToken.START_OBJECT) {
                Movie movie = readMovie(parser);

                List<String> values = extractor.extractValues(movie);
                for (String v : values) {
                    stats.merge(v, 1L, Long::sum);
                }
            }
        }
    }

    private static Movie readMovie(JsonParser parser) throws IOException {
        String title = null;
        String director = null;
        String genres = null;
        Integer year = null;

        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String name = parser.getCurrentName();
            parser.nextToken();

            switch (name) {
                case "title" -> title = parser.getValueAsString();
                case "director" -> director = parser.getValueAsString();
                case "year" -> year = parser.getValueAsInt();
                case "genres" -> genres = parser.getValueAsString();
                default -> parser.skipChildren();
            }
        }

        return new Movie(title, director, year != null ? year : 0, genres);
    }
}
