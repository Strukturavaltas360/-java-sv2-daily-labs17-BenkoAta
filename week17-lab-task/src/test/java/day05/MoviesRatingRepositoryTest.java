package day05;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoviesRatingRepositoryTest {

    private MoviesRatingRepository moviesRatingRepository;

    @BeforeEach
    void init() throws SQLException {
        MariaDbDataSource dataSource;
        dataSource = new MariaDbDataSource("jdbc:mariadb://localhost:3306/movies-actors-test");
        dataSource.setUser("movies-actors");
        dataSource.setPassword("movies-actors");

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        MoviesRepository moviesRepository = new MoviesRepository(dataSource);
        ActorsMoviesRepository actorsMoviesRepository = new ActorsMoviesRepository(dataSource);
        ActorsMoviesService actorsMoviesService = new ActorsMoviesService(actorsRepository, moviesRepository, actorsMoviesRepository);
        actorsMoviesService.insertMovieWithActors("Titanic", LocalDate.of(2002, 11, 13), List.of("Leonardo di Caprio", "Kate Winslet"));
        actorsMoviesService.insertMovieWithActors("Great Gatsby", LocalDate.of(1997, 11, 13), List.of("Leonardo di Caprio"));

        moviesRatingRepository = new MoviesRatingRepository(dataSource);
    }

    @Test
    void getAverageRatingTest() {
        moviesRatingRepository.insertRatings(1, List.of(5, 4, 2));
        assertEquals(11.0/3, moviesRatingRepository.getAverageRating(1), 0.01);
    }

}