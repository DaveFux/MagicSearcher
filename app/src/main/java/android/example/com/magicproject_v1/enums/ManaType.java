package android.example.com.magicproject_v1.enums;

import android.graphics.Color;

public enum ManaType {
    RED("Red", "R",Color.RED),
    BLUE("Blue", "U",Color.BLUE),
    WHITE("White", "W",Color.WHITE),
    BLACK("Black", "B",Color.BLACK),
    GREEN("Green", "G",Color.GREEN),
    COLORLESS("Colorless","C",Color.GRAY);
    private final String text;
    private final String abv;
    private final int color;
    ManaType(String text, String abv, int color) {
        this.text=text;
        this.abv=abv;
        this.color=color;
    }
}
