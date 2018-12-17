package android.example.com.magicproject_v1.classes;

import android.example.com.magicproject_v1.enums.Format;

import java.util.ArrayList;

public class Deck {

    private final static int MAX_SAME_CARD_COUNT = 4; // lands are exception
    private final static int MIN_MAINBOARD_CARD_COUNT = 60;
    private final static int MAX_SIDEBOARD_CARD_COUNT = 15;

    private String name;
    private ArrayList<Card> mainboard;
    private ArrayList<Card> sideboard;
    private Format format;

    public Deck(String name) {
        this.name = name;
        this.mainboard = new ArrayList<>();
        this.sideboard = new ArrayList<>();
        this.format = Format.STANDARD;
    }

    public boolean add(Card pCard){
        return false;
    }

    public boolean addToMainboard(Card card, int count){
        // TODO:
        return false;
    }

    public boolean addToSideboard(Card card, int count){
        // TODO:
        return false;
    }

    public boolean removeFromMainboard(Card card){
        // TODO:
        return false;
    }

    public boolean removeFromSideboard(Card card){
        // TODO:
        return false;
    }

    public boolean removeFromMainboard(int index){
        // TODO:
        return false;
    }

    public boolean removeFromSideboard(int index){
        // TODO:
        return false;
    }

    public int contains(ArrayList<Card> container, Card pCard){
        int count = 0;
        for (Card card : container) {
            if(card.getName().equals(pCard.getName())) count++;
        }
        return count;
    }

    public int mainboardCardCount(){
        return mainboard.size();
    }

    public int sideboardCardCount(){
        return sideboard.size();
    }

    public int cardCount(){
        return mainboard.size() + sideboard.size();
    }

    public int landCount(){
        //TODO:
        return 0;
    }

    public int creaturesCount(){
        //TODO:
        return 0;
    }

    public int spellsCount(){
        //TODO:
        return 0;
    }

    public int artifactsCount(){
        //TODO:
        return 0;
    }

    public int planeswalkersCount(){
        //TODO:
        return 0;
    }

    public int enchantmentsCount(){
        //TODO:
        return 0;
    }

    public Mana colors(){
        //TODO:
        return null;
    }

    public String averageManaCost(){
        //TODO:
        return "";
    }

    @Override
    public String toString() {
        return "Deck{}";
    }

    // -------------- GETTERS AND SETTERS --------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }
}
