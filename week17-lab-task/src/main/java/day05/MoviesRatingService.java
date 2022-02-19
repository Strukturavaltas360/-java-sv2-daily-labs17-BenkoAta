package day05;

import java.util.Arrays;
import java.util.Optional;

public class MoviesRatingService {
    private MoviesRepository moviesRepository;
    private MoviesRatingRepository ratingsRepository;

    public MoviesRatingService(MoviesRepository moviesRepository, MoviesRatingRepository ratingsRepository) {
        this.moviesRepository = moviesRepository;
        this.ratingsRepository = ratingsRepository;
    }

    public void addRatings(String title, Integer... ratings) {
        Optional<Movie> movie = moviesRepository.findMovieByTitle(title);
        if (movie.isPresent()) {
            ratingsRepository.insertRatings(movie.get().getId(), Arrays.stream(ratings).toList());
            double average = ratingsRepository.getAverageRating(movie.get().getId());
            moviesRepository.updateAverageRating(movie.get().getId(), average);
        } else {
            throw new IllegalArgumentException("Can not find movie: " + title);
        }
    }
}
