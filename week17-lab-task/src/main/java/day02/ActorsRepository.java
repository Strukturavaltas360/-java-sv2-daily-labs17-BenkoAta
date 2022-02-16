package day02;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActorsRepository {
    private DataSource dataSource;

    public ActorsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveActor(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO actors (actor_name) VALUES (?) ")) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Can not insert!", e);
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
