package agh.ics.oop.proman.enums;

import agh.ics.oop.proman.classes.Helper;

public enum MoveDirection {
    FORWARD,
    BACKWARD,
    RIGHT,
    LEFT;

    public static MoveDirection getRandomMoveDirection() {
        MoveDirection[] values = MoveDirection.values();
        int randomIndex = Helper.getRandomIntFromRange(0, values.length);

        return values[randomIndex];
    }
}
