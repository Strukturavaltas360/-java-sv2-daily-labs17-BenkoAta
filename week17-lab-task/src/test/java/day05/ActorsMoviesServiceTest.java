package day05;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActorsMoviesServiceTest {
    private ActorsRepository actorsRepository;
    private MoviesRepository moviesRepository;
    private ActorsMoviesRepository actorsMoviesRepository;
    private ActorsMoviesService actorsMoviesService;

    @BeforeEach
    void init() throws SQLException {
        MariaDbDataSource dataSource;
        dataSource = new MariaDbDataSource("jdbc:mariadb://localhost:3306/movies-actors-test");
        dataSource.setUser("movies-actors");
        dataSource.setPassword("movies-actors");

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        actorsRepository = new ActorsRepository(dataSource);
        moviesRepository = new MoviesRepository(dataSource);
        actorsMoviesRepository = new ActorsMoviesRepository(dataSource);
        actorsMoviesService = new ActorsMoviesService(actorsRepository, moviesRepository, actorsMoviesRepository);
    }

    @Test
    void insertMovieWithActorsTest() {
        actorsMoviesService.insertMovieWithActors("title1", LocalDate.now(), List.of("Actor1", "Actor2", "Actor3"));
        actorsMoviesService.insertMovieWithActors("title2", LocalDate.now(), List.of("Actor1", "Actor4"));

        List<Movie> movies = moviesRepository.findAllMovies();
        assertEquals(2, movies.size());

        Optional<Actor> actor1 = actorsRepository.findActorByName("Actor1");
        List<Integer> moviesOfActor1 = actorsMoviesRepository.findMoviesForActor(actor1.get().getId());
        assertEquals(2, moviesOfActor1.size());

        Optional<Actor> actor2 = actorsRepository.findActorByName("Actor2");
        List<Integer> moviesOfActor2 = actorsMoviesRepository.findMoviesForActor(actor2.get().getId());
        assertEquals(1, moviesOfActor2.size());
    }

}