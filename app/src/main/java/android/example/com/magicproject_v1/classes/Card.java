package android.example.com.magicproject_v1.classes;

import android.example.com.magicproject_v1.enums.Rarity;

public class Card {
    protected String name;
    protected String type;
    protected int power;
    protected int toughness;
    protected String expansionName;
    protected Rarity rarity;
    protected String flavorText;
    protected String oracleText;
    protected Mana manaCost;

    public Card(String name, String type,
                int power, int toughness, String expansionName,
                Rarity rarity, String flavorText, String oracleText,
                Mana manaCost) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.toughness = toughness;
        this.expansionName = expansionName;
        this.rarity = rarity;
        this.flavorText = flavorText;
        this.oracleText = oracleText;
        this.manaCost = manaCost;
    }

    public Card(Card card) {
        this.name = card.getName();
        this.type = card.getType();
        this.power = card.getPower();
        this.toughness = card.getToughness();
        this.expansionName = card.getExpansionName();
        this.rarity = card.getRarity();
        this.flavorText = card.getFlavorText();
        this.oracleText = card.getOracleText();
        this.manaCost = card.getManaCost();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getToughness() {
        return toughness;
    }

    public void setToughness(int toughness) {
        this.toughness = toughness;
    }

    public String getExpansionName() {
        return expansionName;
    }

    public void setExpansionName(String expansionName) {
        this.expansionName = expansionName;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public String getFlavorText() {
        return flavorText;
    }

    public void setFlavorText(String flavorText) {
        this.flavorText = flavorText;
    }

    public String getOracleText() {
        return oracleText;
    }

    public void setOracleText(String oracleText) {
        this.oracleText = oracleText;
    }

    public Mana getManaCost() {
        return manaCost;
    }

    public void setManaCost(Mana manaCost) {
        this.manaCost = manaCost;
    }
    //Name, manacost, type, power, toughness, setName, rarity, flavorText, oracleText-> O que a carta faz
}
