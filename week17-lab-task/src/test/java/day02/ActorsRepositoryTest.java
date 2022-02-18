package day02;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ActorsRepositoryTest {

    ActorsRepository actorsRepository;

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
    }

    @Test
    void testInsert() {
        actorsRepository.saveActor("Jack Doe");
    }

}