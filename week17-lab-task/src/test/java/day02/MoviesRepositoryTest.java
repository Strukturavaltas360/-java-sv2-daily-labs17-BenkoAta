package day02;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoviesRepositoryTest {
    MoviesRepository moviesRepository;

    @BeforeEach
    void init() throws SQLException {
        MariaDbDataSource dataSource;
        dataSource = new MariaDbDataSource("jdbc:mariadb://localhost:3306/movies-actors-test");
        dataSource.setUser("movies-actors");
        dataSource.setPassword("movies-actors");

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        moviesRepository = new MoviesRepository(dataSource);
    }

    @Test
    void saveAndFindAllTest() {
        LocalDate date1 = LocalDate.of(2022, 1, 1);
        LocalDate date2 = LocalDate.of(2022, 2, 2);
        moviesRepository.saveMovie("title2", date2);
        moviesRepository.saveMovie("title1", date1);

        List<Movie> actual = moviesRepository.findAllMovies();
        assertEquals(2, actual.size());
        assertEquals(2, actual.get(0).getId());
        assertEquals("title1", actual.get(0).getTitle());
        assertEquals(date1, actual.get(0).getRelease_date());
    }

}