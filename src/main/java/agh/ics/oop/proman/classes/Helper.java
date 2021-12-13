package agh.ics.oop.proman.classes;

public class Helper {
    public static int getRandomIntFromRange(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
