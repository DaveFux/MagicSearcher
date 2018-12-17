package android.example.com.magicproject_v1.classes;

import android.example.com.magicproject_v1.enums.ManaType;
import android.example.com.magicproject_v1.enums.Rarity;

import java.util.HashMap;
import java.util.Objects;

public class Collection {

    private final static HashMap<Card, Integer> CARDS = new HashMap<>();

    public void add(Card pCard){
        Objects.requireNonNull(pCard);
        if(CARDS.containsKey(pCard)){
            CARDS.put(pCard, CARDS.get(pCard) + 1);
        }else {
            CARDS.put(pCard, 1);
        }
    }

    public void add(Card pCard, int count){
        Objects.requireNonNull(pCard);
        if(CARDS.containsKey(pCard)){
            CARDS.put(pCard, CARDS.get(pCard) + count);
        }else {
            CARDS.put(pCard, count);
        }
    }

    public void remove(Card pCard){
        CARDS.remove(pCard);
    }

    public boolean remove(Card pCard, int count){
        if(CARDS.containsKey(pCard)){
            int existingValue = CARDS.get(pCard);
            if(existingValue - count <= 0){
                CARDS.remove(pCard);
            }else {
                CARDS.put(pCard, CARDS.get(pCard) - count);
            }
            return true;
        }else{
            // cannot remove something that does not exist
            return false;
        }
    }

    public int count(){
        return CARDS.values().stream().mapToInt(i -> i).sum();
    }

    public HashMap<Card, Integer> filterByName(String nameFilter){
        HashMap<Card, Integer> returnMap = new HashMap<>();
        for (Card card : CARDS.keySet()) {
            if(card.getName().contains(nameFilter)){
                returnMap.put(card, CARDS.get(card));
            }
        }
        return returnMap;
    }

    public HashMap<Card, Integer> filterByRarity(Rarity rarityFilter){
        HashMap<Card, Integer> returnMap = new HashMap<>();
        for (Card card : CARDS.keySet()) {
            if(card.getRarity() == rarityFilter){
                returnMap.put(card, CARDS.get(card));
            }
        }
        return returnMap;
    }

    public HashMap<Card, Integer> filterByColor(ManaType colorFilter){
        HashMap<Card, Integer> returnMap = new HashMap<>();
        for (Card card : CARDS.keySet()) {
            if(card.getManaCost().contains(colorFilter)){
                returnMap.put(card, CARDS.get(card));
            }
        }
        return returnMap;
    }

    public HashMap<Card, Integer> filterByColor(ManaType[] colorsFilter){
        HashMap<Card, Integer> returnMap = new HashMap<>();
        for (Card card : CARDS.keySet()) {
            for (ManaType manaType : colorsFilter) {
                if (card.getManaCost().contains(manaType)) {
                    returnMap.put(card, CARDS.get(card));
                }
            }
        }
        return returnMap;
    }
}
