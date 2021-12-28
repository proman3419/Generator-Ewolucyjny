package agh.ics.oop.proman.Settings;

import static agh.ics.oop.proman.Settings.GuiControlType.CHECK_BOX;
import static agh.ics.oop.proman.Settings.GuiControlType.TEXT_FIELD;

public enum SimulationParameter implements IParameter {
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

    @Override
    public String getDefaultValue() {
        return switch (this) {
            case MAP_WIDTH -> "9";
            case MAP_HEIGHT -> "16";
            case START_ENERGY -> "100";
            case MOVE_ENERGY -> "1";
            case PLANT_ENERGY -> "25";
            case JUNGLE_RATIO -> "0.25";
            case ANIMALS_COUNT -> "4";
            case IS_MAGIC_BREEDING_ALLOWED_UM -> "false";
            case IS_MAGIC_BREEDING_ALLOWED_BM -> "false";
        };
    }

    @Override
    public GuiControlType getInputGuiControlType() {
        return switch (this) {
            case MAP_WIDTH -> TEXT_FIELD;
            case MAP_HEIGHT -> TEXT_FIELD;
            case START_ENERGY -> TEXT_FIELD;
            case MOVE_ENERGY -> TEXT_FIELD;
            case PLANT_ENERGY -> TEXT_FIELD;
            case JUNGLE_RATIO -> TEXT_FIELD;
            case ANIMALS_COUNT -> TEXT_FIELD;
            case IS_MAGIC_BREEDING_ALLOWED_UM -> CHECK_BOX;
            case IS_MAGIC_BREEDING_ALLOWED_BM -> CHECK_BOX;
        };
    }
}
