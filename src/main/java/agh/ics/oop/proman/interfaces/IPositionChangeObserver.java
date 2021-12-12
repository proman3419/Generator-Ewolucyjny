package agh.ics.oop.proman.interfaces;

import agh.ics.oop.proman.classes.Vector2d;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition);
}
