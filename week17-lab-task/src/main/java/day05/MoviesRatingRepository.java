package day05;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MoviesRatingRepository {
    private DataSource dataSource;

    public MoviesRatingRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertRatings(long movieId, List<Integer> ratings) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            if (!insertRatings(connection, movieId, ratings)) {
                throw new IllegalArgumentException("Invalid ratings!");
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Can not insert into ratings!", exception);
        }
    }

    public List<Integer> getRatings(long movieId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("select rating from ratings where movie_id=?")) {
            ps.setLong(1, movieId);
            return getRatings(ps);
        } catch (SQLException throwables) {
            throw new IllegalStateException("Can not get average rating!");
        }
    }

    private List<Integer> getRatings(PreparedStatement ps) throws SQLException {
        List<Integer> result = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getInt("rating"));
            }
        }
        return result;
    }

    public double getAverageRating(long movieId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("select avg(rating) as avg_rating from ratings where movie_id=?")) {
            ps.setLong(1, movieId);
            return getAverageRating(ps);
        } catch (SQLException throwables) {
            throw new IllegalStateException("Can not get average rating!");
        }
    }

    private double getAverageRating(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }
            throw new IllegalArgumentException("No rating found for movie!");
        }
    }

    private boolean insertRatings(Connection connection, long movieId, List<Integer> ratings) throws SQLException {
        boolean valid = true;
        try (PreparedStatement ps = connection.prepareStatement("insert into ratings (movie_id, rating) values (?,?)")) {
            for (int rating : ratings) {
                valid &= insertRating(ps, movieId, rating);
            }
            if (valid) {
                connection.commit();
            } else {
                connection.rollback();
            }
        }
        return valid;
    }

    private boolean insertRating(PreparedStatement ps, long movieId, int rating) throws SQLException {
        if (rating < 1 || rating > 5) {
            return false;
        }
        ps.setLong(1, movieId);
        ps.setInt(2, rating);
        ps.executeUpdate();
        return true;
    }
}
