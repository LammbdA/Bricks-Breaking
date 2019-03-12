import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.tuke.gamestudio.server.entity.Comment;
import sk.tuke.gamestudio.server.service.CommentService;
import sk.tuke.gamestudio.server.service.CommentServiceJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.*;
import static sk.tuke.gamestudio.game.bricksbreaking.lutak.core.Field.GAME_NAME;

public class CommentServiceJDBCTest {
    protected CommentService commentService = new CommentServiceJDBC();

    public static final String DELETE = "DELETE FROM comment";

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
        assertEquals(0, commentService.getComments(GAME_NAME).size());
    }

    @Test
    public void testAddComment() throws Exception {
        Comment comment = new Comment(GAME_NAME, "miska", "Hello", new Date());
        commentService.addComment(comment);
        assertEquals(1, commentService.getComments(GAME_NAME).size());
    }

    @Test
    public void testGetComments() throws Exception {
        Comment c1 = new Comment(GAME_NAME, "janko", "Nice game", new Date());
        Comment c2 = new Comment(GAME_NAME, "hrasko", "Good developer", new Date());

        commentService.addComment(c1);
        commentService.addComment(c2);

        List<Comment> comments = commentService.getComments(GAME_NAME);
        assertEquals(c1.getComment(), comments.get(0).getComment());
        assertEquals(c2.getComment(), comments.get(1).getComment());
    }

    @After
    public void endTest() throws Exception{
        Connection c = DriverManager.getConnection(URL, USER, PASS);
        Statement s = c.createStatement();
        s.execute(DELETE);
    }
}
