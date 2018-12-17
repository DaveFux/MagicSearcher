package android.example.com.magicproject_v1.enums;

public enum Rarity {
    COMMON("Common", 1),
    UNCOMMON("Uncommon", 2),
    RARE("Rare", 3),
    MYTHIC("Mythic", 4),
    MASTERPIECE("Masterpiece",5);

    private final String text;
    private final int value;

    Rarity(String text, int value){
        this.text = text;
        this.value = value;
    }

    @Override
    public String toString() {
        return this.text;
    }

    public int getValue() {
        return value;
    }
}
