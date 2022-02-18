package day05;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RatingsRepository {
    private DataSource dataSource;

    public RatingsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertRating(long movieId, List<Integer> ratings) {
        try (Connection connection = dataSource.getConnection()) {
             connection.setAutoCommit(false);
             try (PreparedStatement ps = connection.prepareStatement("insert into ratings (movie_id, rating) values (?,?)")) {
                 for (int rating: ratings) {
                     if (rating < 1 || rating > 5) {
                         throw new IllegalArgumentException("Invalid rating");
                     }
                     ps.setLong(1, movieId);
                     ps.setInt(2, rating);
                     ps.executeUpdate();
                 }
                 connection.commit();
             } catch (IllegalArgumentException e) {
                 connection.rollback();
             }
        } catch (SQLException exception) {
            throw new IllegalStateException("Can not insert into ratings!", exception);
        }
    }
}
