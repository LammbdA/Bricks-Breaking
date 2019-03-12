package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.bricksbreaking.lutak.core.GameState;
import sk.tuke.gamestudio.game.bricksbreaking.lutak.webui.WebUI;
import sk.tuke.gamestudio.server.entity.Rating;
import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.*;

import java.util.Date;


@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BricksbreakingLutakController {
    private WebUI webUI = new WebUI();
    private boolean serviceAdded = false;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private UserController userController;

    @RequestMapping("/BricksBreaking-Lutak")
    public String bricksBreaking(@RequestParam(value = "command", required = false) String command,
                                 @RequestParam(value = "row", required = false) String rowString,
                                 @RequestParam(value = "column", required = false) String columnString,
                                 @RequestParam(value = "rating", required = false) String ratingString,
                                 Model model) throws CommentException {

        if (!serviceAdded) {
            webUI.setRatingService(ratingService);
            serviceAdded = true;
        }

        webUI.processCommand(command, rowString, columnString, ratingString);
        model.addAttribute("webUI", webUI);
        try {
            if (webUI.getField().getState() == GameState.PLAYING) {
                int row = Integer.parseInt(rowString);
                int column = Integer.parseInt(columnString);
                webUI.getField().openTile(row, column);
            }
            if (webUI.getField().getState() == GameState.SOLVED || webUI.getField().getState() == GameState.FAILED) {
                if (userController.isLogged()) {
                    scoreService.addScore(
                            new Score("BricksBreaking-Lutak", userController.getLoggedUser().getUsername(),
                                    webUI.getField().getScore(), new Date()));
                }
            }

        } catch (NumberFormatException e) {
            //
        }
        if (ratingString != null && userController.isLogged()) {
            try {
                int rating = Integer.parseInt(ratingString);
                ratingService.setRating(new Rating("BricksBreaking-Lutak",
                        userController.getLoggedUser().getUsername(),
                        rating, new Date()));
            } catch (RatingException e) {
                //
            }
        }

        model.addAttribute("comments", commentService.getComments("BricksBreaking-Lutak"));
        model.addAttribute("scores", scoreService.getBestScores("BricksBreaking-Lutak"));
        return "BricksBreaking-Lutak"; //same name as the template
    }
}
