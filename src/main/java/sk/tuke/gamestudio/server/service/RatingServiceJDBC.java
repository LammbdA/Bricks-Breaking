package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.server.entity.Rating;

import java.sql.*;

import static sk.tuke.gamestudio.game.bricksbreaking.lutak.core.Field.GAME_NAME;

public class RatingServiceJDBC implements RatingService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "Lambda-123";


    public static final String INSERT_RATING =
            "INSERT INTO rating (game, player, rating, ratedon) VALUES (?, ?, ?, ?)";

    public static final String SELECT_RATING =
            "SELECT game, player, rating, ratedon FROM rating WHERE game = ? ORDER BY rating DESC LIMIT 100;";

    public static final String SELECT_RATING1 =
            "SELECT game, player, rating, ratedon FROM rating WHERE game = ? AND player = ? ORDER BY rating DESC LIMIT 100;";

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(INSERT_RATING)) {
                ps.setString(1, rating.getGame());
                ps.setString(2, rating.getPlayer());
                ps.setInt(3, rating.getRating());
                ps.setDate(4, new Date(rating.getRatedon().getTime()));

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RatingException("Error saving rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        int rowsCount = 0;
        int avrgRating = 0;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT_RATING)) {
                ps.setString(1, game);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Rating rating = new Rating(
                                rs.getString(1),
                                rs.getString(2),
                                rs.getInt(3),
                                rs.getTimestamp(4)
                        );
                        rowsCount++;
                        avrgRating = avrgRating + rating.getRating();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error loading rating", e);
        }
        if (rowsCount != 0)
            return avrgRating / rowsCount;
        return 0;
    }

    @Override
    public int getRating(String game, String name) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT_RATING1)) {
                ps.setString(2,name);
                ps.setString(1,game);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Rating rating = new Rating(
                                rs.getString(1),
                                rs.getString(2),
                                rs.getInt(3),
                                rs.getTimestamp(4)
                        );
                        return rating.getRating();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error loading rating", e);
        }
        return 0;
    }

    public static void main(String[] args) throws Exception {
        Rating rating = new Rating(GAME_NAME, "jaro", 5, new java.util.Date());
        RatingService ratingService = new RatingServiceJDBC();
        ratingService.setRating(rating);
        System.out.println(ratingService.getRating(GAME_NAME,"jaro"));
    }

}