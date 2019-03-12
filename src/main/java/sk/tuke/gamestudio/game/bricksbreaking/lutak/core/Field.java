package sk.tuke.gamestudio.game.bricksbreaking.lutak.core;

public class Field {
    public static final String GAME_NAME = "BricksBreaking-Lutak";
    private final Tile[][] tiles;
    private int rowCount;
    private int columnCount;
    private GameState state = GameState.PLAYING;
    private int startRow;
    private int startColumn;
    private int brickCount;
    private int lifes;

    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.tiles = new Tile[rowCount][columnCount];
        this.brickCount = rowCount * columnCount;
        lifes = 3;
        generateField();
    }

    public int getLifes() {
        return lifes;
    }

    //Vigeneruje pole
    public void generateField() {
            for (int row = 0; row < getRowCount(); row++) {
                for (int column = 0; column < getColumnCount(); column++) {
                    RandomColor randomColor = new RandomColor();
                    switch (randomColor.generateRandomColor()) {
//                    tiles[row][column] = new Brick(Color.values()[randomColor.generateRandomColor() - 1]);
                        case 1:
                            tiles[row][column] = new Brick(Color.RED);
                            break;
                        case 2:
                            tiles[row][column] = new Brick(Color.GREEN);
                            break;
                        case 3:
                            tiles[row][column] = new Brick(Color.BLUE);
                            break;
                        case 4:
                            tiles[row][column] = new Brick(Color.PURPLE);
                            break;
                    }
                }
            }
    }

    //Otvori tile
    public void openTile(int row, int column) {
        if (state == GameState.PLAYING) {
            if (isSameBricksThere(row, column)) {
                checkNeighbourTiles(row, column);
                breakBricks();
                fallBricks();
                moveColumn();
            }else{
                if(lifes > 0)
                    breakOneBrick(row, column);
            }
            if (isSolved()) {
                state = GameState.SOLVED;
                System.out.println("You won!");
                return;
            }
            if (isFailed()) {
                state = GameState.FAILED;
                System.out.println("You lost!");
            }
        }
    }

    private void breakOneBrick(int row, int column){
        tiles[row][column] = new Brick(null);
        fallBricks();
        moveColumn();
        brickCount--;
        lifes--;
    }

    //Overi ktore briki maju prejst' kontrolu na znicenie
    private void checkNeighbourTiles(int row, int column) {
        startColumn = column;
        startRow = row;
        if (checkRight(row, column) && tiles[row][column + 1].getState() != TileState.MARKED) {
            checkAll(row, column + 1);
            checkNeighbourTiles(row, column);
        } else {
            if (checkUp(row, column) && tiles[row + 1][column].getState() != TileState.MARKED) {
                checkAll(row + 1, column);
                checkNeighbourTiles(row, column);
            } else {
                if (checkLeft(row, column) && tiles[row][column - 1].getState() != TileState.MARKED) {
                    checkAll(row, column - 1);
                    checkNeighbourTiles(row, column);
                } else {
                    if (checkDown(row, column) && tiles[row - 1][column].getState() != TileState.MARKED) {
                        checkAll(row - 1, column);
                        checkNeighbourTiles(row, column);
                    } else {
                        tiles[row][column].setState(TileState.MARKED);
                    }
                }
            }
        }
    }

    //Overi ktore briki ma znicit'
    private void checkAll(int row, int column) {
        if (checkRight(row, column) && tiles[row][column + 1].getState() != TileState.MARKED) {
            if (column + 1 != startColumn || row != startRow) {
                tiles[row][column].setState(TileState.MARKED);
                checkAll(row, column + 1);
            }
        }
        if (checkUp(row, column) && tiles[row + 1][column].getState() != TileState.MARKED) {
            if (column != startColumn || row + 1 != startRow) {
                tiles[row][column].setState(TileState.MARKED);
                checkAll(row + 1, column);
            }
        }
        if (checkDown(row, column) && tiles[row - 1][column].getState() != TileState.MARKED) {
            if (column != startColumn || row - 1 != startRow) {
                tiles[row][column].setState(TileState.MARKED);
                checkAll(row - 1, column);
            }
        }
        if (checkLeft(row, column) && tiles[row][column - 1].getState() != TileState.MARKED) {
            if (column - 1 != startColumn || row != startRow) {
                tiles[row][column].setState(TileState.MARKED);
                checkAll(row, column - 1);
            }
        }
        tiles[row][column].setState(TileState.MARKED);
    }

    //Znici briki
    private void breakBricks() {
        for (int row = 0; row < getRowCount(); row++) {
            for (int column = 0; column < getColumnCount(); column++) {
                if (tiles[row][column].getState() == TileState.MARKED) {
                    tiles[row][column] = new Brick(null);
                    brickCount--;
                }
            }
        }
    }

    //Spadnu briki
    private void fallBricks() {
        for (int column = 0; column < getColumnCount(); column++) {
            for (int row = 0; row < getRowCount() - 1; row++) {
                if (tiles[row][column].getColor() != null && tiles[row + 1][column].getColor() == null) {
                    tiles[row + 1][column] = tiles[row][column];
                    tiles[row][column] = new Brick(null);
                    row = -1;
                }
            }
        }
    }

    //Zdvihne stlpec
    private void moveColumn() {
        for (int column = 0; column < getColumnCount(); column++) {
            if (!isColumnEmpty(column) && isColumnEmpty(column + 1)) {
                swapColumns(column);
                column = -1;
            }
        }
    }

    //Swapne stlpci
    private void swapColumns(int column) {
        for (int row = 0; row < getRowCount(); row++) {
            tiles[row][column + 1] = tiles[row][column];
            tiles[row][column] = new Brick(null);
        }
    }

    //Overi ci je stlpec prazdny
    private boolean isColumnEmpty(int column) {
        if (column < getColumnCount() - 1) {
            for (int row = 0; row < getRowCount(); row++) {
                if (tiles[row][column].getColor() != null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    //Pozrie, ci su rovnake briki v pol'u
    private boolean isSameBricksThere(int row, int column) {
        if (row >= 0 && column >= 0 && row < getRowCount() && column < getColumnCount()) {
            if (row == 0 && column == 0) {
                return (tiles[row][column].getColor() == tiles[row + 1][column].getColor()
                        || tiles[row][column].getColor() == tiles[row][column + 1].getColor());
            }
            if (row == getRowCount() - 1 && column == getColumnCount() - 1) {
                return ((tiles[row][column].getColor() == tiles[row - 1][column].getColor())
                        || (tiles[row][column].getColor() == tiles[row][column - 1].getColor()));
            }
            if (row == 0 && column == getColumnCount() - 1) {
                return (tiles[row][column].getColor() == tiles[row + 1][column].getColor()
                        || tiles[row][column].getColor() == tiles[row][column - 1].getColor());
            }
            if (column == 0 && row == getRowCount() - 1) {
                return (tiles[row][column].getColor() == tiles[row - 1][column].getColor()
                        || tiles[row][column].getColor() == tiles[row][column + 1].getColor());
            }
            if (row == 0 && column != getColumnCount() - 1) {
                return (tiles[row][column].getColor() == tiles[row + 1][column].getColor()
                        || tiles[row][column].getColor() == tiles[row][column - 1].getColor()
                        || tiles[row][column].getColor() == tiles[row][column + 1].getColor());
            }
            if (column == 0 && row != getRowCount() - 1) {
                return (tiles[row][column].getColor() == tiles[row - 1][column].getColor()
                        || tiles[row][column].getColor() == tiles[row][column + 1].getColor()
                        || tiles[row][column].getColor() == tiles[row + 1][column].getColor());
            }
            if (row == getRowCount() - 1 && column != getColumnCount() - 1) {
                return (tiles[row][column].getColor() == tiles[row - 1][column].getColor())
                        || (tiles[row][column].getColor() == tiles[row][column - 1].getColor())
                        || (tiles[row][column].getColor() == tiles[row][column + 1].getColor());
            }
            if (column == getColumnCount() - 1 && row != getRowCount() - 1) {
                return (tiles[row][column].getColor() == tiles[row - 1][column].getColor())
                        || (tiles[row][column].getColor() == tiles[row][column - 1].getColor())
                        || (tiles[row + 1][column].getColor() == tiles[row][column].getColor());
            }
            return (tiles[row][column].getColor() == tiles[row - 1][column].getColor())
                    || (tiles[row][column].getColor() == tiles[row][column - 1].getColor())
                    || (tiles[row][column].getColor() == tiles[row][column + 1].getColor())
                    || (tiles[row][column].getColor() == tiles[row + 1][column].getColor());
        }
        return false;
    }

    //Overi kolor s pravim brikom
    private boolean checkRight(int row, int column) {
        return column < getColumnCount() - 1 && (tiles[row][column].getColor() == tiles[row][column + 1].getColor());
    }

    //Overi kolor s lavym brikom
    private boolean checkLeft(int row, int column) {
        return column >= 1 && (tiles[row][column].getColor() == tiles[row][column - 1].getColor());
    }

    //Overi kolor s dolnym brikom
    private boolean checkUp(int row, int column) {
        return row < getRowCount() - 1 && (tiles[row][column].getColor() == tiles[row + 1][column].getColor());
    }

    //Overi kolor s hornym brikom
    private boolean checkDown(int row, int column) {
        return row >= 1 && (tiles[row][column].getColor() == tiles[row - 1][column].getColor());
    }

    //Overi ci je hra prehrana
    public boolean isFailed() {
        for (int row = 0; row < getRowCount(); row++) {
            for (int column = 0; column < getColumnCount(); column++) {
                if ((isSameBricksThere(row, column) && tiles[row][column].getColor() != null) || lifes > 0)
                    return false;
            }
        }
        return true;
    }

    //Pozrie ci je hra vyhrana
    private boolean isSolved() {
        for (int row = 0; row < getRowCount(); row++) {
            for (int column = 0; column < getColumnCount(); column++) {
                if (tiles[row][column].getColor() != null)
                    return false;
            }
        }
        return true;
    }

    public Tile getTiles(int row, int column) {
        return tiles[row][column];
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public GameState getState() {
        return state;
    }

    public int getScore() {
        return (getColumnCount() * getRowCount() - brickCount) * 100;
    }
}
