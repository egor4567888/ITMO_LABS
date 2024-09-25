package common;

public enum Coloures {
    RED("красный"),
    BLUE("голубой"),
    GREEN("зеленоватый"),
    YELLOW("жёлтый"),
    PURPLE("фиолетовый");
    private String colour;
    Coloures(String colour){
        this.colour = colour;
    }
    @Override
    public String toString() {
        return colour;
    }

}
