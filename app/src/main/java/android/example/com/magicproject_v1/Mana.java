package android.example.com.magicproject_v1;

public class Mana implements Comparable<Mana> {

    private int red, green, blue, white, black, colorless;

    public Mana() {
        red = 0;
        green= 0;
        blue = 0;
        white = 0;
        black = 0;
        colorless = 0;
    }

    public Mana(final int red, final int green, final int blue, final int white, final int black, final int colorless){
        this.red = notNegative(red, "Red");
        this.green = notNegative(green, "Green");
        this.blue = notNegative(blue, "Blue");
        this.white = notNegative(white, "White");
        this.black = notNegative(black, "Black");
        this.colorless = notNegative(colorless, "Colorless");
    }

    public Mana(final int value, final ManaType manaType){
        switch (manaType){
            case RED: this.red = notNegative(value, "Red");
                break;
            case GREEN: this.green = notNegative(value, "Green");
                break;
            case BLUE: this.blue = notNegative(value, "Blue");
                break;
            case WHITE: this.white = notNegative(value, "White");
                break;
            case BLACK: this.black = notNegative(value, "Black");
                break;
            case COLORLESS: this.colorless = notNegative(value, "Colorless");
        }
    }

    public Mana(final Mana mana) {
        this.red = mana.getRed();
        this.green = mana.getGreen();
        this.blue = mana.getBlue();
        this.white = mana.getWhite();
        this.black = mana.getBlack();
        this.colorless = mana.getColorless();
    }

    public static Mana RedMana(int value){
        return new Mana(value, 0, 0, 0, 0, 0);
    }

    public static Mana GreenMana(int value){
        return new Mana(0, value, 0, 0, 0, 0);
    }

    public static Mana BlueMana(int value){
        return new Mana(0, 0, value, 0, 0, 0);
    }

    public static Mana WhiteMana(int value){
        return new Mana(0, 0, 0, value, 0, 0);
    }

    public static Mana BlackMana(int value){
        return new Mana(0, 0, 0, 0, value, 0);
    }

    public static Mana ColorlessMana(int value){
        return new Mana(0, 0, 0, 0, 0, value);
    }

    public void add(final Mana mana){
        this.red += mana.getRed();
        this.green += mana.getGreen();
        this.blue += mana.getBlue();
        this.white += mana.getWhite();
        this.black += mana.getBlack();
        this.colorless += mana.getColorless();
    }

    public void increaseRed(){
        this.red++;
    }

    public void increaseGreen(){
        this.green++;
    }

    public void increaseBlue(){
        this.blue++;
    }

    public void increaseWhite(){
        this.white++;
    }

    public void increaseBlack(){
        this.black++;
    }

    public void increaseColorless(){
        this.colorless++;
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

    public void subtract(final Mana mana){
        this.red -= mana.getRed();
        this.green -= mana.getGreen();
        this.blue -= mana.getBlue();
        this.white -= mana.getWhite();
        this.black -= mana.getBlack();
        this.colorless -= mana.getColorless();
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
        this.red = notNegative(red, "Red");
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = notNegative(green, "Green");
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = notNegative(blue, "Blue");
    }

    public int getWhite() {
        return white;
    }

    public void setWhite(int white) {
        this.white = notNegative(white, "White");;
    }

    public int getBlack() {
        return black;
    }

    public void setBlack(int black) {
        this.black = notNegative(black, "Black");;
    }

    public int getColorless() {
        return colorless;
    }

    public void setColorless(int colorless) {
        this.colorless = notNegative(colorless, "Colorless");
    }

    private static int notNegative(int value, final String name){
        if (value < 0){
            value = 0;
        }
        return value;
    }
}

