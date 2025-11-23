package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class GenerateTestFiles {

    private static final List<String> TITLES = List.of(
            "Inception", "Interstellar", "Dunkirk", "Tenet", "Memento",
            "The Prestige", "The Dark Knight", "Avatar", "Titanic",
            "Pulp Fiction", "Kill Bill", "Reservoir Dogs", "Matrix",
            "John Wick", "Gladiator", "The Martian", "Joker"
    );

    private static final List<String> DIRECTORS = List.of(
            "Christopher Nolan", "James Cameron", "Quentin Tarantino",
            "Ridley Scott", "Denis Villeneuve", "Martin Scorsese",
            "Steven Spielberg", "Guy Ritchie", "Zack Snyder"
    );

    private static final List<String> GENRES = List.of(
            "Action", "Sci-Fi", "Thriller", "Drama", "Crime", "Fantasy",
            "Adventure", "Mystery", "Romance"
    );

    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws IOException {
        Path dir = Path.of("data");

        if (!Files.exists(dir)) {
            Files.createDirectory(dir);
        }

        for (int i = 1; i <= 100; i++) {
            createJsonFile(dir.resolve("movie_" + i + ".json"));
        }

        System.out.println("✔ 100 JSON files generated in /data folder!");
    }

    private static void createJsonFile(Path file) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        int movieCount = 50; // фільмів у кожному файлі

        for (int i = 0; i < movieCount; i++) {
            String title = random(TITLES);
            String director = random(DIRECTORS);
            int year = 1980 + RANDOM.nextInt(45);

            String genres = random(GENRES) + ", " + random(GENRES);

            json.append("  {\n");
            json.append("    \"title\": \"").append(title).append("\",\n");
            json.append("    \"director\": \"").append(director).append("\",\n");
            json.append("    \"year\": ").append(year).append(",\n");
            json.append("    \"genres\": \"").append(genres).append("\"\n");
            json.append("  }");

            if (i < movieCount - 1) json.append(",");
            json.append("\n");
        }

        json.append("]");

        Files.writeString(file, json.toString());
    }

    private static <T> T random(List<T> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }
}
