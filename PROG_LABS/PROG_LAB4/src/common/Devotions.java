package common;

public enum Devotions {
    BAROMETER("барометр"),
    NAMIRA("Намира"),
    LIGHT("свет"),
    BOETHIAH("Боэтия"),
    FLOWERS("Цветы");

    private String devotion;
    Devotions(String devotion){
        this.devotion = devotion;
    }
    @Override
    public String toString() {
        return devotion;
    }
}
