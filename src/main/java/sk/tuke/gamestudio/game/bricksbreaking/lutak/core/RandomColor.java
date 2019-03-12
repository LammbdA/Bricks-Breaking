package sk.tuke.gamestudio.game.bricksbreaking.lutak.core;

import java.util.Random;

public class RandomColor {
    public int generateRandomColor(){
        Random random = new Random();
        return random.nextInt(4)+1;
    }
}
