package agh.ics.oop.proman.classes;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WorldMapStatistics {
    private AbstractWorldMap map;

    public WorldMapStatistics(AbstractWorldMap map) {
        this.map = map;
    }

    public int getAnimalsCount() {
        return this.map.animalsList.size();
    }

    private int getDeadAnimalsCount() {
        return this.map.animalsLifespans.size();
    }

    public int getPlantsCount() {
        return this.map.plantsList.size();
    }

    public double getAverageAnimalEnergy() {
        if (getAnimalsCount() == 0) return 0;

        int energySum = 0;
        for (Animal animal : this.map.animalsList)
            energySum += animal.getEnergy();

        return (double) energySum / getAnimalsCount();
    }

    public double getAverageAnimalLifespan() {
        if (getDeadAnimalsCount() == 0) return 0;

        int animalLifespansSum = 0;
        for (Integer animalLifespan : this.map.animalsLifespans)
            animalLifespansSum += animalLifespan;

        return (double) animalLifespansSum / getDeadAnimalsCount();
    }

    public double getAverageChildrenCount() {
        if (getAnimalsCount() == 0) return 0;

        int childrenSum = 0;
        for (Animal animal : this.map.animalsList)
            childrenSum += animal.getChildrenCount();

        return (double) childrenSum / getAnimalsCount();
    }

    private LinkedHashMap<Genome, Integer> initGenomesToOccurances() {
        LinkedHashMap<Genome, Integer> genomesToOccurances = new LinkedHashMap<>();

        for (Animal animal : this.map.animalsList) {
            Genome genome = animal.getGenome();
            if (genomesToOccurances.containsKey(genome)) {
                int occurances = genomesToOccurances.get(genome);
                genomesToOccurances.put(genome, occurances+1);
            }
            else
                genomesToOccurances.put(genome, 1);
        }

        return genomesToOccurances;
    }

    public Genome getDominantGenome() {
        LinkedHashMap<Genome, Integer> genomesToOccurances = initGenomesToOccurances();

        int maxOccurances = 0;
        Genome maxOccurancesGenome = null;
        for (Genome genome : genomesToOccurances.keySet()) {
            int currOccurances = genomesToOccurances.get(genome);
            if (currOccurances > maxOccurances) {
                maxOccurances = currOccurances;
                maxOccurancesGenome = genome;
            }
        }

        return maxOccurancesGenome;
    }
}
