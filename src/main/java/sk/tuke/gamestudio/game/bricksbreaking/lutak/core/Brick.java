package sk.tuke.gamestudio.game.bricksbreaking.lutak.core;

public class Brick extends Tile {
    private final Color color;
    public Brick(Color color) {
        super(color);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
