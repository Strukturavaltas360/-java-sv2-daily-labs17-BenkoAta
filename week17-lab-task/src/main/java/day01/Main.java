package day01;

import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;

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

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        System.out.println(actorsRepository.findActorsByPrefix("j"));
    }
}
