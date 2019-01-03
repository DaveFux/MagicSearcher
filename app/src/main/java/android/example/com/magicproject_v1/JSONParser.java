package android.example.com.magicproject_v1;

import android.example.com.magicproject_v1.classes.Card;
import android.example.com.magicproject_v1.classes.Mana;
import android.example.com.magicproject_v1.enums.Rarity;
import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    public List<Card> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readCardsArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<Card> readCardsArray(JsonReader reader) throws IOException {
        List<Card> cards = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            String name="";
            String type="";
            int power=0;
            int toughness=0;
            String expansionName="";
            Rarity rarity = Rarity.COMMON;
            String flavorText="";
            String oracleText="";
            Mana manaCost = new Mana();

            while (reader.hasNext()) {
                String field = reader.nextName();
                if (field.equals("name")) {
                    name = reader.nextString();
                } else if (field.equals("type_line")) {
                    type = reader.nextString();
                } else if (field.equals("mana_cost")) {
                    manaCost = new Mana(reader.nextString());
                } else if (field.equals("oracle_text")) {
                    oracleText = reader.nextString();
                } else if (field.equals("flavor_text")) {
                    flavorText = reader.nextString();
                } else if (field.equals("legalities")) {
                    //TODO:
                    reader.skipValue();
                } else if (field.equals("rarity")) {
                    switch (reader.nextString()){
                        case "uncommon":
                            rarity=Rarity.UNCOMMON;
                            break;
                        case "rare":
                            rarity = Rarity.RARE;
                            break;
                        case "mythic":
                            rarity = Rarity.MYTHIC;
                            break;
                        case "masterpiece":
                            rarity = Rarity.MASTERPIECE;
                            break;
                    }
                } else if (field.equals("set_name")) {
                    expansionName = reader.nextString();
                } else if (field.equals("power")) {
                    power = reader.nextInt();
                } else if (field.equals("toughness")) {
                    toughness = reader.nextInt();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            cards.add(new Card(name,  type, power,  toughness,  expansionName, rarity,  flavorText,  oracleText, manaCost));
        }
        reader.endArray();
        return cards;
    }

}
