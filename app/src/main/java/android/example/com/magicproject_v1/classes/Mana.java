package android.example.com.magicproject_v1.classes;

import android.example.com.magicproject_v1.enums.ManaType;
import android.support.annotation.NonNull;

import java.util.Objects;

public class Mana implements Comparable<Mana> {

    private int red, green, blue, white, black, colorless;

    public Mana() { }

    public Mana(final int red, final int green, final int blue, final int white, final int black, final int colorless){
        this.red = notNegative(red);
        this.green = notNegative(green);
        this.blue = notNegative(blue);
        this.white = notNegative(white);
        this.black = notNegative(black);
        this.colorless = notNegative(colorless);
    }

    public Mana(final Mana mana) {
        Objects.requireNonNull(mana);
        this.red = mana.getRed();
        this.green = mana.getGreen();
        this.blue = mana.getBlue();
        this.white = mana.getWhite();
        this.black = mana.getBlack();
        this.colorless = mana.getColorless();
    }

    public Mana(String strManaCost) {
        this.set(ManaType.RED, strManaCost.length() - strManaCost.replaceAll("R","").length());
        this.set(ManaType.BLACK, strManaCost.length() - strManaCost.replaceAll("B","").length());
        this.set(ManaType.BLUE, strManaCost.length() - strManaCost.replaceAll("U","").length());
        this.set(ManaType.GREEN, strManaCost.length() - strManaCost.replaceAll("G","").length());
        this.set(ManaType.WHITE, strManaCost.length() - strManaCost.replaceAll("W","").length());
        this.set(ManaType.COLORLESS, Integer.parseInt(strManaCost.replaceAll("[^0-9]", "").length() > 0 ? strManaCost.replaceAll("[^0-9]", "") : "0"));
    }

    public static Mana RedMana(int value){
        return new Mana(notNegative(value), 0, 0, 0, 0, 0);
    }

    public static Mana GreenMana(int value){
        return new Mana(0, notNegative(value), 0, 0, 0, 0);
    }

    public static Mana BlueMana(int value){
        return new Mana(0, 0, notNegative(value), 0, 0, 0);
    }

    public static Mana WhiteMana(int value){
        return new Mana(0, 0, 0, notNegative(value), 0, 0);
    }

    public static Mana BlackMana(int value){
        return new Mana(0, 0, 0, 0, notNegative(value), 0);
    }

    public static Mana ColorlessMana(int value){
        return new Mana(0, 0, 0, 0, 0, notNegative(value));
    }

    public void add(final Mana mana){
        this.red += mana.getRed();
        this.green += mana.getGreen();
        this.blue += mana.getBlue();
        this.white += mana.getWhite();
        this.black += mana.getBlack();
        this.colorless += mana.getColorless();
    }

    public int get(final ManaType manaType){
        switch (manaType){
            case RED: return this.red;
            case GREEN: return this.green;
            case BLUE: return this.blue;
            case WHITE: return this.white;
            case BLACK: return this.black;
            case COLORLESS: return this.colorless;
        }
        return 0;
    }

    public void set(final ManaType manaType, final int value){
        switch (manaType){
            case RED: this.red = value;
                break;
            case GREEN: this.green = value;
                break;
            case BLUE: this.blue = value;
                break;
            case WHITE: this.white = value;
                break;
            case BLACK: this.black = value;
                break;
            case COLORLESS: this.colorless = value;
        }
    }

    public boolean contains(final ManaType manaType){
        if(manaType.getText().equals("Red") && this.red > 0) return true;
        if(manaType.getText().equals("Green") && this.green > 0) return true;
        if(manaType.getText().equals("Blue") && this.blue > 0) return true;
        if(manaType.getText().equals("White") && this.white > 0) return true;
        if(manaType.getText().equals("Black") && this.black > 0) return true;
        return manaType.getText().equals("Colorless") && this.colorless > 0;
    }

    public int convertedManaCost(){
        return this.red + this.green + this.blue + this.white + this.black + this.colorless;
    }

    @Override
    public int compareTo(@NonNull Mana otherMana) {
        return this.convertedManaCost() - otherMana.convertedManaCost();
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getWhite() {
        return white;
    }

    public int getBlack() {
        return black;
    }

    public int getColorless() {
        return colorless;
    }

    private static int notNegative(int value){
        if (value < 0){
            value = 0;
        }
        return value;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(this.colorless > 0) sb.append(this.colorless);
        for (int i = 0; i < this.red; i++) {
            sb.append(ManaType.RED.getAbv());
        }
        for (int h = 0; h < this.green; h++) {
            sb.append(ManaType.GREEN.getAbv());
        }
        for (int g = 0; g < this.blue; g++) {
            sb.append(ManaType.BLUE.getAbv());
        }
        for (int j = 0; j < this.black; j++) {
            sb.append(ManaType.BLACK.getAbv());
        }
        for (int k = 0; k < this.white; k++) {
            sb.append(ManaType.WHITE.getAbv());
        }
        return sb.toString();
    }
}

