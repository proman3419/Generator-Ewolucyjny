package agh.ics.oop.proman.classes;

public class Gene {
    private final int value;

    public Gene(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }
}
