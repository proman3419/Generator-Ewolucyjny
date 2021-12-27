package agh.ics.oop.proman.Observers;

import agh.ics.oop.proman.Entities.Vector2d;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, Object objectAtOldPosition);
}
