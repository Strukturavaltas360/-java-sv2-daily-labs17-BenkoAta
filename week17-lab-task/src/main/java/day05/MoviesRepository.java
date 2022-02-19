package day05;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MoviesRepository {
    private DataSource dataSource;

    public MoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long saveMovie(String title, LocalDate release_date) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "insert into movies (title,release_date) values (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, title);
            ps.setDate(2, Date.valueOf(release_date));
            ps.executeUpdate();
            return getGeneratedId(ps);
        } catch (SQLException e) {
            throw new IllegalStateException("Can not save!", e);
        }
    }

    public void updateAverageRating(long movieId, double avgRating) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("update movies set avg_rating=? where id=?")) {
            ps.setDouble(1, avgRating);
            ps.setLong(2, movieId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Can not update average rating!", e);
        }
    }

    private long getGeneratedId(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new IllegalStateException("Can not get generated id!");
        }
    }

    public List<Movie> findAllMovies() {
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("select * from movies order by release_date");
        ) {
            return readMovies(rs);
        } catch (SQLException e) {
            throw new IllegalStateException("Can not read!", e);
        }
    }

    public Optional<Movie> findMovieByTitle(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM movies WHERE title = ?")) {
            stmt.setString(1, name);
            return findMovieByTitle(stmt);
        } catch (SQLException exception) {
            throw new IllegalStateException("Can not find movie!", exception);
        }
    }

    private Optional<Movie> findMovieByTitle(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(new Movie(rs.getLong("id"),
                        rs.getString("title"),
                        rs.getDate("release_date").toLocalDate(),
                        rs.getDouble("avg_rating")));
            }
            return Optional.empty();
        }
    }

    private List<Movie> readMovies(ResultSet rs) throws SQLException {
        List<Movie> result = new ArrayList<>();
        while (rs.next()) {
            Movie movie = new Movie(rs.getLong("id"),
                    rs.getString("title"),
                    rs.getDate("release_date").toLocalDate(),
                    rs.getDouble("avg_rating"));
            result.add(movie);
        }
        return result;
    }
}
