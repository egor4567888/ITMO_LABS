package common;

public enum Action {
    SPREAD_OUT("расстилилась"),
    EXIST1("было"),
    EXIST2("были"),
    MEET("собирались"),
    APPEARE("явилось"),
    WAIT("ожидалось"),
    WALK("Ходили"),
    STAND("стоял"),
    HANG("висел"),
    BOW1("кланялись"),
    BOW2("раскланивались"),
    Roam("бродил"),
    LOOK("посмотреть"),
    SEE("видел"),
    COUNT("считал"),
    MUMBLE("бормотать");


    private final String action;
    Action (String text){
        this.action = text;
    }
    public String getAction(){
        return action;
    }

}
