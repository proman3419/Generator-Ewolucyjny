package agh.ics.oop.proman.classes;

public class Epoch {
    int id = 0;
    int animalsCount = 0;
    int plantsCount = 0;
    double averageAnimalEnergy = 0;
    double averageAnimalLifespan = 0;
    double averageChildrenCount = 0;
    Genome dominantGenome;

    public Epoch() {    }

    public Epoch(int id, int animalsCount, int plantsCount, double averageAnimalEnergy,
                 double averageAnimalLifespan, double averageChildrenCount, Genome dominantGenome) {
        this.id = id;
        this.animalsCount = animalsCount;
        this.plantsCount = plantsCount;
        this.averageAnimalEnergy = averageAnimalEnergy;
        this.averageAnimalLifespan = averageAnimalLifespan;
        this.averageChildrenCount = averageChildrenCount;
        this.dominantGenome = dominantGenome;
    }

    public int getId() {
        return id;
    }

    public int getAnimalsCount() {
        return animalsCount;
    }

    public int getPlantsCount() {
        return plantsCount;
    }

    public double getAverageAnimalEnergy() {
        return averageAnimalEnergy;
    }

    public double getAverageAnimalLifespan() {
        return averageAnimalLifespan;
    }

    public double getAverageChildrenCount() {
        return averageChildrenCount;
    }

    public Genome getDominantGenome() {
        return dominantGenome;
    }

    public Epoch cumulate(Epoch other) {
        return new Epoch(this.id,
                         this.animalsCount + other.getAnimalsCount(),
                         this.plantsCount + other.getPlantsCount(),
                         this.averageAnimalEnergy + other.getAverageAnimalEnergy(),
                         this.averageAnimalLifespan + other.getAverageAnimalLifespan(),
                         this.averageChildrenCount + other.getAverageChildrenCount(),
                         this.dominantGenome);
    }

    @Override
    public String toString() {
        return String.format("%d,%d,%d,%.3f,%.3f,%.3f", this.id, this.animalsCount, this.plantsCount,
                             this.averageAnimalEnergy, this.averageAnimalLifespan, this.averageChildrenCount);
    }
}
