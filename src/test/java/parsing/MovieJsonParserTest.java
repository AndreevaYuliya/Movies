package parsing;

import stats.AttributeExtractor;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MovieJsonParserTest {

    @Test
    void testGenreParsing() throws Exception {
        String json = """
                [
                  {
                    "title": "Inception",
                    "director": "Christopher Nolan",
                    "year": 2010,
                    "genres": "Sci-Fi, Thriller"
                  },
                  {
                    "title": "Interstellar",
                    "director": "Christopher Nolan",
                    "year": 2014,
                    "genres": "Sci-Fi, Drama"
                  }
                ]
                """;

        var extractor = AttributeExtractor.forAttribute("genres");
        Map<String, Long> stats = new HashMap<>();

        try (var in = new ByteArrayInputStream(json.getBytes())) {
            MovieJsonParser.parseFile(in, extractor, stats);
        }

        assertEquals(2, stats.get("Sci-Fi"));
        assertEquals(1, stats.get("Thriller"));
        assertEquals(1, stats.get("Drama"));
    }
}
