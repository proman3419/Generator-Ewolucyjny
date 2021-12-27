package agh.ics.oop.proman.MapElements.Animal;

import agh.ics.oop.proman.Classes.Helper;
import agh.ics.oop.proman.Settings.Constants;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Genome {
    private final List<Gene> genes;

    public Genome(int genesCount) {
        this.genes = new LinkedList<>();
        for (int i = 0; i < genesCount; i++)
            genes.add(new Gene(Helper.getRandomIntFromRange(Constants.minGeneValue, Constants.maxGeneValue+1)));
        genes.sort(Helper.genesComparator); // Keep genes in the non-decreasing order
    }

    public Genome(List<Gene> genes) {
        this.genes = genes;
        genes.sort(Helper.genesComparator); // Keep genes in the non-decreasing order
    }

    public Gene getRandomGene() {
        return this.genes.get(Helper.getRandomIntFromRange(0, this.genes.size()));
    }

    public Gene getGene(int index) {
        return this.genes.get(index);
    }

    public Genome combine(Genome other, double otherGenesRatio) {
        int thisGenesCount = (int) (Constants.genesInGenomeCount * (1 - otherGenesRatio));
        int otherGenesCount = Constants.genesInGenomeCount - thisGenesCount;

        if (GenomSide.getRandom() == GenomSide.LEFT) {
            return new Genome(Stream.concat(this.genes.subList(0, thisGenesCount).stream(),
                                            other.genes.subList(thisGenesCount, Constants.genesInGenomeCount).stream())
                                    .collect(Collectors.toList()));
        }
        else {
            return new Genome(Stream.concat(other.genes.subList(0, otherGenesCount).stream(),
                                            this.genes.subList(otherGenesCount, Constants.genesInGenomeCount).stream())
                                    .collect(Collectors.toList()));
        }
    }
}
