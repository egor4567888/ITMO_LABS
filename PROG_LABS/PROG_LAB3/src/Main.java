import objects.*;
import common.*;
public class Main {
    public static void main(String[] args) {
        String s;
        LocatebleEntity lawn = new LocatebleEntity("лужайка");
        lawn.setLocation(Places.IN_THE_MIDDLE_OF_ISLAND);
        lawn.addLeftProp("ровная зелёная");
        Thing bush = new Thing("кустарником");
        bush.addLeftProp("цветущим");
        s = lawn.getLocation() + " " + lawn.leftAction(Action.SPREAD_OUT,bush, "окруженная");
        s = s.substring(0, 1).toUpperCase() + s.substring(1) + ". ";
        System.out.println(s);

        Entity that = new Entity("это");
        that.addRightProp("и");
        Thing meetingPlace = new Thing("место встреч хатифнаттов");
        meetingPlace.addLeftProp("тайное");
        LocatebleEntity they = new LocatebleEntity("они");
        they.setLocation(Places.IN_THE_MIDDLE_OF_SUMMER);
        they.addLeftProp("где");
        s = that.rightAction(Action.EXIST1, meetingPlace) + ", " + they.rightAction(Action.MEET,"раз в год") + " " + they.getLocation();
        s = s.substring(0, 1).toUpperCase() + s.substring(1)+ ". ";
        System.out.println(s);


    }
}