package day02;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MoviesRepository {
    private DataSource dataSource;

    public MoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveMovie(String title, LocalDate release_date) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("insert into movies (title,release_date) values (?,?)")) {
            ps.setString(1, title);
            ps.setDate(2, Date.valueOf(release_date));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Can not save!", e);
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

    private List<Movie> readMovies(ResultSet rs) throws SQLException {
        List<Movie> result = new ArrayList<>();
        while (rs.next()) {
            Movie movie = new Movie(rs.getLong("id"),
                    rs.getString("title"),
                    rs.getDate("release_date").toLocalDate());
            result.add(movie);
        }
        return result;
    }
}
