package day05;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        long id = actorsRepository.saveActor("Jack Doe");
        assertTrue(id > 0);
    }

    @Test
    void findActorByName() {
        actorsRepository.saveActor("John Doe");
        actorsRepository.saveActor("Jane Doe");
        long id = actorsRepository.saveActor("Jack Doe");

        Optional<Actor> actor = actorsRepository.findActorByName("Jack Doe");
        assertTrue(actor.isPresent());
        assertEquals(id, actor.get().getId());
        assertEquals("Jack Doe", actor.get().getName());
    }
}