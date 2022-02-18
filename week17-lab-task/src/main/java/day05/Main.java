package day05;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MariaDbDataSource dataSource;
        try {
            dataSource = new MariaDbDataSource("jdbc:mariadb://localhost:3306/movies-actors");
            dataSource.setUser("movies-actors");
            dataSource.setPassword("movies-actors");
        } catch (SQLException exception) {
            throw new IllegalStateException("SQL error!", exception);
        }

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        MoviesRepository moviesRepository = new MoviesRepository(dataSource);
        ActorsMoviesRepository actorsMoviesRepository = new ActorsMoviesRepository(dataSource);
        RatingsRepository ratingsRepository = new RatingsRepository(dataSource);

        ActorsMoviesService actorsMoviesService = new ActorsMoviesService(actorsRepository, moviesRepository, actorsMoviesRepository);
        MoviesRatingService moviesRatingService = new MoviesRatingService(moviesRepository, ratingsRepository);

        actorsMoviesService.insertMovieWithActors("Titanic", LocalDate.of(2002, 11, 13), List.of("Leonardo di Caprio", "Kate Winslet"));
        actorsMoviesService.insertMovieWithActors("Great Gatsby", LocalDate.of(1997, 11, 13), List.of("Leonardo di Caprio"));
        moviesRatingService.addRatings("Titanic", 5, 4, 3);
        moviesRatingService.addRatings("Great Gatsby", 3, 4, 0);
    }
}
