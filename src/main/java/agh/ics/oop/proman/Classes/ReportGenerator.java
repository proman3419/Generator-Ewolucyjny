package agh.ics.oop.proman.Classes;

import agh.ics.oop.proman.Entities.Epoch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class ReportGenerator {
    private final List<Epoch> epochs = new LinkedList<>();

    public void feed(Epoch epoch) {
        this.epochs.add(epoch);
    }

    public void saveReport(File saveFile) {
        try {
            PrintWriter saveFileWriter = new PrintWriter(saveFile);
            saveFileWriter.println(getHeader());

            Epoch cumulativeEpoch = new Epoch();
            for (Epoch epoch : epochs) {
                saveFileWriter.println(epoch);
                cumulativeEpoch = cumulativeEpoch.cumulate(epoch);
            }

            saveFileWriter.println(getAverages(cumulativeEpoch, this.epochs.size()));
            saveFileWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getHeader() {
        return "epochId,animalsCount,plantsCount,averageAnimalEnergy,averageAnimalLifespan,averageChildrenCount";
    }

    private String getAverages(Epoch cumulativeEpoch, int epochsCount) {
        return String.format("avg,%.3f,%.3f,%.3f,%.3f,%.3f",
                             (double) cumulativeEpoch.getAnimalsCount() / epochsCount,
                             (double) cumulativeEpoch.getPlantsCount() / epochsCount,
                             cumulativeEpoch.getAverageAnimalEnergy() / epochsCount,
                             cumulativeEpoch.getAverageAnimalLifespan() / epochsCount,
                             cumulativeEpoch.getAverageAnimalLifespan() / epochsCount);
    }
}
