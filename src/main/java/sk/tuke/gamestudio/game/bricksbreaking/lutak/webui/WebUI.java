package sk.tuke.gamestudio.game.bricksbreaking.lutak.webui;

import sk.tuke.gamestudio.game.bricksbreaking.lutak.core.Field;
import sk.tuke.gamestudio.server.service.RatingException;
import sk.tuke.gamestudio.server.service.RatingService;

import java.util.Formatter;


public class WebUI {
    private Field field = new Field(10, 10);
    private RatingService ratingService;

    public void processCommand(String command, String rowString, String columnString, String ratingString) {
        if (command != null) {
            if (command.equals("new")) {
                field = new Field(10, 10);
            }
        }
    }


    public String renderAsHtml() {
        Formatter sb = new Formatter();
        sb.format("<table class='field'>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.format("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                sb.format("<td>\n");
                sb.format("<a href='/BricksBreaking-Lutak?row=%d&column=%d'>", row, column);
                sb.format("<img src='/images/bricksbreaking/lutak/" + getImageName(row, column) + ".png'>\n");
                sb.format("</a>");
            }
        }
        sb.format("</table>\n");
        return sb.toString();
    }

    public String renderRatingAsHtml() {
        Formatter sb = new Formatter();

        int userRating;

        try {
            userRating = ratingService.getAverageRating(Field.GAME_NAME);
        } catch (RatingException e) {
            userRating = 0;
        }

        for (int i = 1; i < 6; i++) {
            sb.format("<a href='/BricksBreaking-Lutak?rating=%d'>", i);
            sb.format("<img src='/images/bricksbreaking/lutak/rating");
            if (userRating >= i) {
                sb.format(".png'");
            } else {
                sb.format("Empty.png'");
            }
            sb.format(">\n");
            sb.format("</a>");
        }
        return sb.toString();
    }


    private String getImageName(int row, int column) {
        if (field.getTiles(row, column).getColor() == null) {
            return "empty";
        } else {
            switch (field.getTiles(row, column).getColor()) {
                case RED:
                    return "red";
                case BLUE:
                    return "blue";
                case GREEN:
                    return "green";
                case PURPLE:
                    return "purple";
            }
            throw new IllegalArgumentException("Uns. tile state ");
        }
    }

    public Field getField() {
        return field;
    }

    public void setRatingService(RatingService ratingService) {
        this.ratingService = ratingService;
    }
}

