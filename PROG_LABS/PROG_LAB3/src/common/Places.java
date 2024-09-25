package common;

public enum Places {
    IN_THE_MIDDLE_OF_ISLAND("в самой середине острова"),
    IN_THE_MIDDLE_OF_SUMMER("в середине лета"),
    IN_THE_MIDDLE_OF_LAWN("посреди лужайки"),
    IN_MOOMINVALLEY("в Муми-доле");
    private String locatoin;
    Places(String loc){
        this.locatoin = loc;
    }
    public String getLocation(){
        return this.locatoin;
    }
}
