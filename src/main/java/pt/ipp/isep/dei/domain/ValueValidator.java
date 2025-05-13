package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.ui.console.menu.MainMenuUI;

public class ValueValidator {
    public boolean isPositive(int value) {
        return value > 0;
    }
    public boolean validateWidthLength(int width, int height) {
        return isPositive(width) && isPositive(height);
    }
}
