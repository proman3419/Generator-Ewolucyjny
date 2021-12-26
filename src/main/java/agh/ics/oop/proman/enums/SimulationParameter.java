package agh.ics.oop.proman.enums;

public enum SimulationParameter {
    MAP_WIDTH,
    MAP_HEIGHT,
    START_ENERGY,
    MOVE_ENERGY,
    PLANT_ENERGY,
    JUNGLE_RATIO,
    ANIMALS_COUNT,
    IS_MAGIC_BREEDING_ALLOWED_UM,
    IS_MAGIC_BREEDING_ALLOWED_BM;

    @Override
    public String toString() {
        return switch (this) {
            case MAP_WIDTH -> "Map width";
            case MAP_HEIGHT -> "Map height";
            case START_ENERGY -> "Start energy";
            case MOVE_ENERGY -> "Move energy";
            case PLANT_ENERGY -> "Plant energy";
            case JUNGLE_RATIO -> "Jungle ratio";
            case ANIMALS_COUNT -> "Animals count";
            case IS_MAGIC_BREEDING_ALLOWED_UM -> "Magic evolution for UnboundedMap";
            case IS_MAGIC_BREEDING_ALLOWED_BM -> "Magic evolution for BoundedMap";
        };
    }

    public String getDefaultValue() {
        return switch (this) {
            case MAP_WIDTH -> "12";
            case MAP_HEIGHT -> "12";
            case START_ENERGY -> "25";
            case MOVE_ENERGY -> "1";
            case PLANT_ENERGY -> "2";
            case JUNGLE_RATIO -> "0.25";
            case ANIMALS_COUNT -> "4";
            case IS_MAGIC_BREEDING_ALLOWED_UM -> "false";
            case IS_MAGIC_BREEDING_ALLOWED_BM -> "false";
        };
    }
}
