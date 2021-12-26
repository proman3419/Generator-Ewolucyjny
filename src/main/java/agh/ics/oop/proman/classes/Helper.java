package agh.ics.oop.proman.classes;

import java.util.Comparator;
import java.util.Random;
import java.util.Set;

public class Helper {
    // Inclusive min, exclusive max
    public static int getRandomIntFromRange(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    // truthBias from range (0, 1), the higher it is the higher the probability of getting true
    // assumes min = 0
    public static boolean getRandomBoolean(double truthBias) {
        int max = 1000;
        return getRandomIntFromRange(0, max) < max * truthBias;
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

    public static Object getRandomElementFromSet(Set set) {
        int size = set.size();
        if (size > 0) {
            int itemId = new Random().nextInt(size);
            int i = 0;

            for (Object o : set) {
                if (i == itemId)
                    return o;
                i++;
            }
        }

        return null;
    }
}
