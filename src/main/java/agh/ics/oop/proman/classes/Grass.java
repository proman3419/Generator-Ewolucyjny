package agh.ics.oop.proman.classes;

public class Grass extends AbstractWorldMapElement {
    private boolean inJungle;

    public Grass(Vector2d position, boolean inJungle) {
        super(position);
        this.inJungle = inJungle;
    }

    @Override
    public String toString() {
        return "*";
    }

    public boolean isInJungle() {
        return inJungle;
    }
}
