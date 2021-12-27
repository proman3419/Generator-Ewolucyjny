package agh.ics.oop.proman.Enums;

import agh.ics.oop.proman.Classes.Helper;

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
