package android.example.com.magicproject_v1.classes;

import android.example.com.magicproject_v1.enums.Rarity;
import android.graphics.Bitmap;

public class Card {

    protected String id;
    protected String name;
    protected String type;
    protected int power;
    protected int toughness;
    protected String expansionName;
    protected Rarity rarity;
    protected String flavorText;
    protected String oracleText;
    protected Mana manaCost;
    protected String image;
    protected String thumbnailURL;
    protected Bitmap thumbnail;

    public Card(String id, String name, String type,
                int power, int toughness, String expansionName,
                Rarity rarity, String flavorText, String oracleText,
                Mana manaCost, String image, String thumbnailURL) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.power = power;
        this.toughness = toughness;
        this.expansionName = expansionName;
        this.rarity = rarity;
        this.flavorText = flavorText;
        this.oracleText = oracleText;
        this.manaCost = manaCost;
        this.image = image;
        this.thumbnailURL = thumbnailURL;
    }

    public Card(String id, String name, String type, String manaCost,
                String flavorText, String oracleText, String expansionName, String rarity,
                int power, int toughness, String image, String thumbnailURL) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.power = power;
        this.toughness = toughness;
        this.expansionName = expansionName;
        this.rarity = rarityFromString(rarity);
        this.flavorText = flavorText;
        this.oracleText = oracleText;
        this.manaCost = new Mana(manaCost);
        this.image = image;
        this.thumbnailURL = thumbnailURL;
    }

    public Card(Card card) {
        this.id = card.getId();
        this.name = card.getName();
        this.type = card.getType();
        this.power = card.getPower();
        this.toughness = card.getToughness();
        this.expansionName = card.getExpansionName();
        this.rarity = card.getRarity();
        this.flavorText = card.getFlavorText();
        this.oracleText = card.getOracleText();
        this.manaCost = card.getManaCost();
        this.image = card.getImage();
        this.thumbnailURL = card.getThumbnailURL();
    }

    private Rarity rarityFromString(String rarity) {
        return Rarity.COMMON;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    //Name, manacost, type, power, toughness, setName, rarity, flavorText, oracleText-> O que a carta faz
}
