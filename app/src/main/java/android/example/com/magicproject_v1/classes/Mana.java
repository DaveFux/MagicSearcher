package android.example.com.magicproject_v1.classes;

import android.example.com.magicproject_v1.enums.ManaType;

import java.util.Objects;

public class Mana implements Comparable<Mana> {

    private int red, green, blue, white, black, colorless;

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
        if(manaType.toString().equals("Red") && this.red > 0) return true;
        if(manaType.toString().equals("Green") && this.green > 0) return true;
        if(manaType.toString().equals("Blue") && this.blue > 0) return true;
        if(manaType.toString().equals("White") && this.white > 0) return true;
        if(manaType.toString().equals("Black") && this.black > 0) return true;
        return manaType.toString().equals("Colorless") && this.colorless > 0;
    }

    public boolean contains(final Mana mana){
        return this.red >= mana.getRed() && this.green >= mana.getGreen() && this.blue >= mana.getBlue() &&
                this.white >= mana.getWhite() && this.black >= mana.getBlack() && this.colorless >= mana.getColorless();
    }

    public int convertedManaCost(){
        return this.red + this.green + this.blue + this.white + this.black + this.colorless;
    }

    public int convertedColoredManaCost(){
        return this.red + this.green + this.blue + this.white + this.black;
    }

    public void clear(){
        this.red = 0;
        this.green = 0;
        this.blue = 0;
        this.white = 0;
        this.black = 0;
        this.colorless = 0;
    }

    @Override
    public int compareTo(Mana otherMana) {
        return this.convertedManaCost() - otherMana.convertedManaCost();
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = notNegative(red);
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = notNegative(green);
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = notNegative(blue);
    }

    public int getWhite() {
        return white;
    }

    public void setWhite(int white) {
        this.white = notNegative(white);;
    }

    public int getBlack() {
        return black;
    }

    public void setBlack(int black) {
        this.black = notNegative(black);;
    }

    public int getColorless() {
        return colorless;
    }

    public void setColorless(int colorless) {
        this.colorless = notNegative(colorless);
    }

    private static int notNegative(int value){
        if (value < 0){
            value = 0;
        }
        return value;
    }
}

