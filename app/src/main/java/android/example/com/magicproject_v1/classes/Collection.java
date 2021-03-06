package android.example.com.magicproject_v1.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Collection {

    private int id;
    private String name;
    private List<String> tags;
    private List<Card> cards;
    private int numberOfCards;

    public Collection(int id, String name, List<String> tags, List<Card> cards) {
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.cards = cards;
        this.numberOfCards = cards.size();
    }

    public Collection(int id, String name, String tags) {
        this.id = id;
        this.name = name;
        this.tags = Arrays.asList(tags.replaceAll("\\[", "")
                .replaceAll("]", "").split(","));
        this.cards = new ArrayList<>();
        this.numberOfCards = 0;
    }

    public Collection(String name, String tags) {
        this.name = name;
        this.tags = Arrays.asList(tags.replaceAll("\\[", "")
                .replaceAll("]", "").split(","));
        this.cards = new ArrayList<>();
        this.numberOfCards = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        StringBuilder sb = new StringBuilder("[" + tags.get(0));
        for (int i = 1; i < tags.size(); i++) {
            sb.append(", ");
            sb.append(tags.get(i));
        }
        sb.append("]");
        return sb.toString();
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
