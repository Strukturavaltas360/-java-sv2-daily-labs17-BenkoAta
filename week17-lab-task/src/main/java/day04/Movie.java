package day04;

import java.time.LocalDate;

public class Movie {
    private Long id;
    private String title;
    private LocalDate release_date;

    public Movie(Long id, String title, LocalDate release_date) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getRelease_date() {
        return release_date;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", release_date=" + release_date +
                '}';
    }
}
