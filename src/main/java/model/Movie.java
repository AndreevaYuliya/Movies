package model;

public record Movie(
        String title,
        String director,
        int year,
        String genres // Example: "Sci-Fi, Thriller"
) { }