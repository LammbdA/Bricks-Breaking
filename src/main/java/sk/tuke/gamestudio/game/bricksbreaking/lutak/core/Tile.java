package sk.tuke.gamestudio.game.bricksbreaking.lutak.core;

public abstract class Tile {
    private Color color;
    private TileState state = TileState.DEFAULT;

    public Tile(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setState(TileState state) {
        this.state = state;
    }

    public TileState getState() {
        return state;
    }
}
