package stats;

import model.Movie;

import java.util.Arrays;
import java.util.List;

@FunctionalInterface
public interface AttributeExtractor {

    List<String> extractValues(Movie movie);

    static AttributeExtractor forAttribute(String attributeName) {
        return switch (attributeName) {
            case "director" -> movie -> List.of(movie.director());
            case "year" -> movie -> List.of(String.valueOf(movie.year()));
            case "genres" -> movie -> {
                if (movie.genres() == null || movie.genres().isBlank()) {
                    return List.of();
                }
                return Arrays.stream(movie.genres().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList();
            };
            default -> throw new IllegalArgumentException(
                    "Unsupported attribute: " + attributeName +
                            ". Supported: director, year, genres");
        };
    }
}
