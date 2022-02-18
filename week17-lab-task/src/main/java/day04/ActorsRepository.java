package day04;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActorsRepository {
    private DataSource dataSource;

    public ActorsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long saveActor(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO actors (actor_name) VALUES (?) ", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            return getGeneratedId(stmt);
        } catch (SQLException e) {
            throw new IllegalStateException("Can not insert!", e);
        }
    }

    public Optional<Actor> findActorByName(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM actors WHERE actor_name = ?")) {
            stmt.setString(1, name);
            return findActorByName(stmt);
        } catch (SQLException exception) {
            throw new IllegalStateException("Can not select!", exception);
        }
    }

    private Optional<Actor> findActorByName(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(new Actor(rs.getLong("id"), rs.getString("actor_name")));
            }
            return Optional.empty();
        }
    }

    private long getGeneratedId(Statement stmt) throws SQLException {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new IllegalStateException("Can not get generated id!");
        }
    }

    public List<String> findActorsByPrefix(String prefix) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT actor_name FROM actors WHERE actor_name LIKE ?")) {
            stmt.setString(1, prefix + '%');
            return findActorsByPrefix(stmt);
        } catch (SQLException e) {
            throw new IllegalStateException("Can not select!", e);
        }
    }

    private List<String> findActorsByPrefix(PreparedStatement stmt) throws SQLException {
        List<String> result = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getString("actor_name"));
            }
            return result;
        }
    }
}
