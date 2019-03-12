import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.tuke.gamestudio.server.entity.Rating;
import sk.tuke.gamestudio.server.service.RatingService;
import sk.tuke.gamestudio.server.service.RatingServiceJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;

import static junit.framework.TestCase.*;
import static sk.tuke.gamestudio.game.bricksbreaking.lutak.core.Field.GAME_NAME;

public class RatingServiceJDBCTest {
    protected RatingService ratingService = new RatingServiceJDBC();

    public static final String DELETE = "DELETE FROM rating";

    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASS = "Lambda-123";

    @Before
    public void setUp() throws Exception{
        Connection c = DriverManager.getConnection(URL, USER, PASS);
        Statement s = c.createStatement();
        s.execute(DELETE);
    }

    @Test
    public void testDbInit() throws Exception {
        assertEquals(0, ratingService.getAverageRating(GAME_NAME));
    }

    @Test
    public void testGetSetRating() throws Exception {
        Rating rating1 = new Rating(GAME_NAME, "miska", 5, new Date());
        Rating rating2 = new Rating(GAME_NAME, "janko", 4, new Date());
        ratingService.setRating(rating1);
        ratingService.setRating(rating2);
        assertEquals(5, ratingService.getRating(GAME_NAME,"miska"));
        assertEquals(4, ratingService.getRating(GAME_NAME,"janko"));
    }

    @Test
    public void testGetAvrgRating() throws Exception {
        Rating r1 = new Rating(GAME_NAME, "janko", 5, new Date());
        Rating r2 = new Rating(GAME_NAME, "hrasko", 3, new Date());
        Rating r3 = new Rating(GAME_NAME, "hrasko", 4, new Date());

        ratingService.setRating(r1);
        ratingService.setRating(r2);
        ratingService.setRating(r3);

        assertEquals(4,ratingService.getAverageRating(GAME_NAME));
    }

    @After
    public void endTest() throws Exception{
        Connection c = DriverManager.getConnection(URL, USER, PASS);
        Statement s = c.createStatement();
        s.execute(DELETE);
    }
}
