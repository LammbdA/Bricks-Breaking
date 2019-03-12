package sk.tuke.gamestudio.game.bricksbreaking.lutak.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.bricksbreaking.lutak.core.GameState;
import sk.tuke.gamestudio.server.entity.Comment;
import sk.tuke.gamestudio.server.entity.Rating;
import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.game.bricksbreaking.lutak.core.Field;
import sk.tuke.gamestudio.server.service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static sk.tuke.gamestudio.game.bricksbreaking.lutak.core.Field.GAME_NAME;


public class ConsoleUI {
    private String name;
    private Field field;
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private final Pattern INPUT_PATTERN = Pattern.compile("([A-Z])([1-9][0-9]*)");

//    private RatingService ratingService = new RatingServiceJDBC();
//    private ScoreService scoreService = new ScoreServiceJDBC();
//    private CommentService commentService = new CommentServiceJDBC();

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";

    private Scanner sc = new Scanner(System.in);


    //Spusti hru
    public void run() {
        askPlayerName();
        askPlayerField();
        while (field.getState() == GameState.PLAYING) {
            printField();
            writeLifes();
            processInput();
        }
        printField();
        wtireScore();
        if (field.getState() == GameState.FAILED || field.getState() == GameState.SOLVED)
            askPlayerRerun();
        writeRating();
        writeComment();
    }

    private void writeLifes(){
        System.out.printf("You have %d lifes %n",field.getLifes());
    }

    private void askPlayerName() {
        System.out.print("Write your name: ");
        name = sc.next();
    }

    private void writeRating() {
        System.out.print("Rate the game(From 1 to 5):");
        String string = sc.next();
        if (!string.matches("[1-5]")) {
            System.out.println("Zadali ste zly vstup");
            writeRating();
        } else {
            int rating = Integer.parseInt(string);
            try {
                ratingService.setRating(new Rating(
                        GAME_NAME,
                        name,
                        rating,
                        new Date()
                ));
                System.out.printf("Your rating (%d) was added to database %n",rating);
                printAvrRaiting();
      //          printPlayersRating();
            } catch (RatingException e) {
                System.err.println(e.getMessage());
            }
        }
    }

//    private void printPlayersRating(){
//        try {
//            List<Rating> ratings = RatingServiceJDBC.getPlayersRatings(GAME_NAME);
//            for (Rating r : ratings) {
//                System.out.println(r);
//            }
//        } catch (RatingException e) {
//            System.err.println(e.getMessage());
//        }
//    }

    private void printAvrRaiting(){
        try {
            System.out.printf("The average game rating is: %d%n",ratingService.getAverageRating(GAME_NAME));
        } catch (RatingException e) {
            System.err.println(e.getMessage());
        }
    }

    public int getAvrRating(){
        try {
            return ratingService.getAverageRating(GAME_NAME);
        }catch (RatingException e){
            return 0;
        }
    }

    //Zapise score do databazy
    private void wtireScore() {
        System.out.printf("Your score was: %d%n", field.getScore());
        try {
            scoreService.addScore(new Score(
                    GAME_NAME,
                    name,
                    field.getScore(),
                    new Date()
            ));
            System.out.println("Your score was added to database");
            printScore();
        } catch (ScoreException e) {
            System.err.println(e.getMessage());
        }
    }

    //Zapise komentar do databazy
    private void writeComment() {
        System.out.print("Do you want to write a comment? [Y/N]");
        String answer = sc.next();
        answer = answer.toUpperCase();
        if (answer.equals("Y")) {
            String comment = readLine();
            try {
                commentService.addComment(new Comment(
                        GAME_NAME,
                        name,
                        comment,
                        new Date()
                ));
                System.out.printf("Your comment (%s) was added to database %n",comment);
                printComments();
                System.exit(0);
            } catch (CommentException e) {
                System.err.println(e.getMessage());
            }
        } else {
            if (answer.equals("N")) {
                printComments();
                System.exit(0);
            } else {
                System.out.println("Nespravna otpoved'");
                writeComment();
            }
        }
    }

    private void printComments(){
        try {
            List<Comment> comments = commentService.getComments(GAME_NAME);

            for (Comment s : comments) {
                System.out.println(s);
            }
        } catch (CommentException e) {
            System.err.println(e.getMessage());
        }
    }

    //Vypise top10 lepsih hracov
    private void printScore() {
        try {
            List<Score> scores = scoreService.getBestScores(GAME_NAME);

            for (Score s : scores) {
                System.out.println(s);
            }
        } catch (ScoreException e) {
            System.err.println(e.getMessage());
        }
    }


    //Pita sa ci hce zahrat' znova
    private void askPlayerRerun() {
        System.out.print("Zahrat' znova? [Y/N]");
        String answer = sc.next();
        answer = answer.toUpperCase();
        if (answer.equals("Y")) {
            run();
        } else {
            if (answer.equals("N"))
                return;
            else {
                System.out.println("Nespravna otpoved'");
                askPlayerRerun();
            }
        }
    }


    //Pita sa hraca ake pole hce zahrat'
    private void askPlayerField() {
        System.out.println("Zadajte rozmery pol'a:");
        System.out.print("Rows=");
        String string = sc.next();
        if (!string.matches("([1-9][0-9]*)")) {
            System.out.println("Zadali ste zly vstup");
            askPlayerField();
            return;
        }
        int row = Integer.parseInt(string);
        System.out.print("Columns=");
        string = sc.next();
        if (!string.matches("([1-9][0-9]*)")) {
            System.out.println("Zadali ste zly vstup");
            askPlayerField();
            return;
        }
        int column = Integer.parseInt(string);
        if (column > 1 && row > 1) {
            field = new Field(row, column);
            while (field.isFailed()) {
                field.generateField();
            }
        } else {
            System.out.println("Zadajte pole aspon' 2x2");
            askPlayerField();
        }
    }

    //Vypise pole
    private void printField() {
        printGameInfo();
        printFieldHeader();
        printFieldBody();
    }

    private void printFieldHeader() {
        System.out.print(" ");
        for (int column = 0; column < field.getColumnCount(); column++) {
            System.out.printf(" %2d", column + 1);
        }
        System.out.println();
    }


    private void printFieldBody() {
        for (int row = 0; row < field.getRowCount(); row++) {
            System.out.print((char) (row + 'A'));
            for (int column = 0; column < field.getColumnCount(); column++) {
                System.out.print(" ");
                if (field.getTiles(row, column).getColor() != null) {
                    switch (field.getTiles(row, column).getColor()) {
                        case RED:
                            System.out.print(ANSI_RED_BACKGROUND + "  " + ANSI_RESET);
                            break;
                        case BLUE:
                            System.out.print(ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET);
                            break;
                        case GREEN:
                            System.out.print(ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET);
                            break;
                        case PURPLE:
                            System.out.print(ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET);
                            break;
                    }
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
            System.out.println();
        }
    }

    private void printGameInfo() {
        System.out.printf("Your score: %d%n", field.getScore());
    }

    private String readLine() {
        try {
            return br.readLine();
        } catch (IOException e) {
            System.err.println("Nepodarilo sa nacitat vstup, skus znova");
            return "";
        }
    }

    private void processInput() {
        System.out.println("Napis vstup (napr. a1, exit):");
        String line = readLine();
        line = line.toUpperCase();
        if (line.equals("EXIT")) {
            System.exit(0);
        }
        Matcher m = INPUT_PATTERN.matcher(line);
        if (m.matches()) {
            int row = m.group(1).charAt(0) - 65;
            int column = Integer.parseInt(m.group(2)) - 1;
            if (row < field.getRowCount() && column < field.getColumnCount() && field.getTiles(row, column).getColor() != null)
                field.openTile(row, column);
            else {
                System.out.println("To nie je spravny move.");
                processInput();
            }
        } else {
            System.out.println("Nezadal si dobry vstup, skus znova.");
            processInput();
        }
    }
}
