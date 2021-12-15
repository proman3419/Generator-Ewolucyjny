package agh.ics.oop.proman.enums;

import agh.ics.oop.proman.classes.Helper;

public enum GenomSide {
    LEFT,
    RIGHT;

    public static GenomSide getRandomGenomeSide() {
        GenomSide[] values = GenomSide.values();
        int randomIndex = Helper.getRandomIntFromRange(0, values.length);

        return values[randomIndex];
    }
}
