package agh.ics.oop.proman.classes;

import java.util.Comparator;

public class Helper {
    // Inclusive min, exclusive max
    public static int getRandomIntFromRange(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static Comparator<Gene> genesComparator = new Comparator<Gene>() {
        @Override
        public int compare(Gene o1, Gene o2) {
            return o1.getValue().compareTo(o2.getValue());
        }
    };

    public static Comparator<Animal> animalEnergyComparator = new Comparator<Animal>() {
        @Override
        public int compare(Animal o1, Animal o2) {
            return Integer.compare(o2.getEnergy(), o1.getEnergy());
        }
    };
}
