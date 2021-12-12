package agh.ics.oop.proman.classes;

import java.util.List;
import java.util.Random;

public class Genome {
    private List<Gene> genes;

    public Genome(List<Gene> genes) {
        this.genes = genes;
    }

    public Gene getRandomGene() {
        return this.genes.get(new Random().nextInt(this.genes.size()));
    }
}
