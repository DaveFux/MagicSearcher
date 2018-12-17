package android.example.com.magicproject_v1;

public enum Format {
    LEGACY("Legacy", 3),
    MODERN("Modern", 2),
    STANDARD("Standard", 1);

    private final String text;
    private final int priority;

    Format(String text, int priority) {
        this.text = text;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return this.text;
    }

    public int getPriority() {
        return priority;
    }
}