package day02;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;

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
        flyway.migrate();

        MoviesRepository moviesRepository = new MoviesRepository(dataSource);
        System.out.println(moviesRepository.findAllMovies());
    }
}
