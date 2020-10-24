package agh.cs.lab1;

public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public String toString() {
        switch(this) {
            case NORTH: return "Północ";
            case SOUTH: return "Południe";
            case EAST: return "Wschód";
            case WEST: return "Zachód";
            default: return "Błąd";
        }
    }

    public MapDirection next() {
        switch(this) {
            case NORTH: return EAST;
            case SOUTH: return WEST;
            case EAST: return SOUTH;
            default: return NORTH;
        }
    }

    public MapDirection previous() {
        switch(this) {
            case NORTH: return WEST;
            case SOUTH: return EAST;
            case EAST: return NORTH;
            default: return SOUTH;
        }
    }

    public Vector2d toUnitVector() {
        switch(this) {
            case NORTH: return new Vector2d(0, 1);
            case SOUTH: return new Vector2d(0, -1);
            case EAST: return new Vector2d(1, 0);
            default: return new Vector2d(-1, 0);
        }
    }
}
