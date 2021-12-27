package agh.ics.oop.proman.Settings;

import static agh.ics.oop.proman.Settings.GuiControlType.TEXT_FIELD;

public enum GuiParameter implements IParameter {
    APP_WIDTH,
    APP_HEIGHT;

    @Override
    public String toString() {
        return switch (this) {
            case APP_WIDTH -> "App width";
            case APP_HEIGHT -> "App height";
        };
    }

    @Override
    public String getDefaultValue() {
        return switch (this) {
            case APP_WIDTH -> "1920";
            case APP_HEIGHT -> "1080";
        };
    }

    @Override
    public GuiControlType getInputGuiControlType() {
        return switch (this) {
            case APP_WIDTH -> TEXT_FIELD;
            case APP_HEIGHT -> TEXT_FIELD;
        };
    }
}
