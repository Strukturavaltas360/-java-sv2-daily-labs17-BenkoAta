package day05;

import java.time.LocalDate;

public class Movie {
    private Long id;
    private String title;
    private LocalDate releaseDate;
    private double avgRating;

    public Movie(Long id, String title, LocalDate releaseDate, double avgRating) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.avgRating = avgRating;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public double getAvgRating() {
        return avgRating;
    }
}
