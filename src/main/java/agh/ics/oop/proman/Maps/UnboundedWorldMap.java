package agh.ics.oop.proman.Maps;

import agh.ics.oop.proman.Entities.Vector2d;

public class UnboundedWorldMap extends AbstractWorldMap {
    public UnboundedWorldMap(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, double jungleRatio,
                            int animalsCount, boolean isMagicBreedingAllowed) {
        super(width, height, startEnergy, moveEnergy, plantEnergy, jungleRatio, animalsCount, isMagicBreedingAllowed);
    }

    public Vector2d positionToUnboundedPosition(Vector2d position) {
        int newX = position.x;
        int newY = position.y;

        if (newX < this.lowerLeft.x) newX = this.upperRight.x;
        else if (newX > this.upperRight.x) newX = this.lowerLeft.x;

        if (newY < this.lowerLeft.y) newY = this.upperRight.y;
        else if (newY > this.upperRight.y) newY = this.lowerLeft.y;

        return new Vector2d(newX, newY);
    }
}
