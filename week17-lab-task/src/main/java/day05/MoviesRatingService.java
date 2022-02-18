package day05;

import java.util.Arrays;
import java.util.Optional;

public class MoviesRatingService {
    private MoviesRepository moviesRepository;
    private RatingsRepository ratingsRepository;

    public MoviesRatingService(MoviesRepository moviesRepository, RatingsRepository ratingsRepository) {
        this.moviesRepository = moviesRepository;
        this.ratingsRepository = ratingsRepository;
    }

    public void addRatings(String title, Integer... ratings) {
        Optional<Movie> movie = moviesRepository.findMovieByTitle(title);
        if (movie.isPresent()) {
            ratingsRepository.insertRating(movie.get().getId(), Arrays.stream(ratings).toList());
        } else {
            throw new IllegalArgumentException("Can not find movie: " + title);
        }
    }
}
