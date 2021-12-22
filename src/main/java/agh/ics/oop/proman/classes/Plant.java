package agh.ics.oop.proman.classes;

public class Plant extends AbstractWorldMapElement {
    private boolean inJungle;

    public Plant(Vector2d position, boolean inJungle) {
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

    @Override
    public String getRepresentationImagePath() {
        return "src/main/resources/grass.png";
    }

    @Override
    public String toLabelString() {
        return "Plant";
    }
}
