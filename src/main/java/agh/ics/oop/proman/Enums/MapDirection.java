package agh.ics.oop.proman.Enums;

import agh.ics.oop.proman.Entities.Vector2d;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public static MapDirection fromInteger(Integer i) {
        return switch(i) {
            case 0 -> NORTH;
            case 1 -> NORTHEAST;
            case 2 -> EAST;
            case 3 -> SOUTHEAST;
            case 4 -> SOUTH;
            case 5 -> SOUTHWEST;
            case 6 -> WEST;
            default -> NORTHWEST;
        };
    }

    public String toString() {
        return switch (this) {
            case NORTH -> "North";
            case NORTHEAST -> "Northeast";
            case EAST -> "East";
            case SOUTHEAST -> "Southeast";
            case SOUTH -> "South";
            case SOUTHWEST -> "Southwest";
            case WEST -> "West";
            case NORTHWEST -> "Northwest";
        };
    }

    public MapDirection next() {
        int valuesLength = MapDirection.values().length;
        return fromInteger((this.ordinal() + 1) % valuesLength);
    }

    public MapDirection previous() {
        int valuesLength = MapDirection.values().length;
        return fromInteger((this.ordinal() - 1 + valuesLength) % valuesLength);
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTHEAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTHEAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTHWEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTHWEST -> new Vector2d(-1, 1);
        };
    }
}
