package day05;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActorsMoviesRepository {
    DataSource dataSource;

    public ActorsMoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertActorAndMovieId(long actorId, long movieId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("insert into movies_actors (actor_id, movie_id) values (?,?) ")) {
            ps.setLong(1, actorId);
            ps.setLong(2, movieId);
            ps.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Can not insert row!", exception);
        }
    }

    public List<Integer> findMoviesForActor(long actorId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "select movie_id from movies_actors where actor_id = ? order by movie_id")) {
            ps.setLong(1, actorId);
            return readIntList(ps);
        } catch (SQLException exception) {
            throw new IllegalStateException("Can select!", exception);
        }
    }

    public List<Integer> findActorsForMovie(long movieId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "select actor_id from movies_actors where movie_id = ? order by actor_id")) {
            ps.setLong(1, movieId);
            return readIntList(ps);
        } catch (SQLException exception) {
            throw new IllegalStateException("Can select!", exception);
        }
    }

    private List<Integer> readIntList(PreparedStatement ps) throws SQLException {
        List<Integer> result = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getInt(1));
            }
        }
        return result;
    }

}
