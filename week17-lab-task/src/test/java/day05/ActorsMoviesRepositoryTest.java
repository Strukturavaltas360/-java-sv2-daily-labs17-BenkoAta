package day05;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActorsMoviesRepositoryTest {
    ActorsMoviesRepository actorsMoviesRepository;

    @BeforeEach
    void init() throws SQLException {
        MariaDbDataSource dataSource;
        dataSource = new MariaDbDataSource("jdbc:mariadb://localhost:3306/movies-actors-test");
        dataSource.setUser("movies-actors");
        dataSource.setPassword("movies-actors");

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        actorsMoviesRepository = new ActorsMoviesRepository(dataSource);

        actorsMoviesRepository.insertActorAndMovieId(1, 2);
        actorsMoviesRepository.insertActorAndMovieId(2, 2);
        actorsMoviesRepository.insertActorAndMovieId(3, 2);
        actorsMoviesRepository.insertActorAndMovieId(1, 1);
    }

    @Test
    void findMoviesForActorTest() {
        List<Integer> moviesOfActor1 = actorsMoviesRepository.findMoviesForActor(1L);
        List<Integer> moviesOfActor2 = actorsMoviesRepository.findMoviesForActor(2L);
        assertEquals(List.of(1, 2), moviesOfActor1);
        assertEquals(List.of(2), moviesOfActor2);
    }

    @Test
    void findActorsForMovieTest() {
        List<Integer> actorsOfMovie1 = actorsMoviesRepository.findActorsForMovie(1L);
        List<Integer> actorsOfMovie2 = actorsMoviesRepository.findActorsForMovie(2L);
        assertEquals(List.of(1), actorsOfMovie1);
        assertEquals(List.of(1, 2, 3), actorsOfMovie2);
    }
}