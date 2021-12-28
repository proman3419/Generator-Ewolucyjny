package agh.ics.oop.proman.Classes;

import agh.ics.oop.proman.MapElements.Animal.Animal;
import agh.ics.oop.proman.MapElements.Animal.Gene;

import java.io.File;
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
        assert 0 < truthBias && truthBias < 1 : "truthBias should be in range (0, 1)";
        int max = 1000;
        return getRandomIntFromRange(0, max) < max * truthBias;
    }

    public final static Comparator<Gene> genesComparator = new Comparator<Gene>() {
        @Override
        public int compare(Gene o1, Gene o2) {
            return o1.getValue().compareTo(o2.getValue());
        }
    };

    public final static Comparator<Animal> animalEnergyComparator = new Comparator<Animal>() {
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

    public static String adjustPathString(String pathString) {
        String separator = File.separator;
        if (separator.equals("\\"))
            separator = "\\\\";
        return pathString.replaceAll("/", separator);
    }
}
