import common.*;
import entities.Hattifattener;
import entities.Hemulen;
import ritualItems.Barometer;
import ritualItems.BoethiahAltar;
import ritualItems.NamiraAltar;
import ritualItems.ResurrectionAltar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String filePath = "/home/egor/IdeaProjects/PROG_LAB3.1/src/HattifattenerNames";

        Hattifattener[] hattifatteners = new Hattifattener[300];
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int index = 0;

            while ((line = reader.readLine()) != null && index < 75) {
                hattifatteners[index] = new Hattifattener(line,Devotions.BAROMETER);
                index++;
            }
            while ((line = reader.readLine()) != null && index < 150) {
                hattifatteners[index] = new Hattifattener(line,Devotions.BOETHIAH);
                index++;
            }
            while ((line = reader.readLine()) != null && index < 225) {
                hattifatteners[index] = new Hattifattener(line,Devotions.NAMIRA);
                index++;
            }
            while ((line = reader.readLine()) != null && index < 300) {
                hattifatteners[index] = new Hattifattener(line,Devotions.LIGHT);
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
       }
        System.out.println(hattifatteners.getClass());
        class Starter{
            String plName;
            float plLatitude;
            float plLongitude;
            String ritualItemName;
            RitualItems ritualItemType;
            Starter(RitualItems ritualItemType, String ritualItemName, String plName, float plLatitude, float plLongitude){
                this.ritualItemType = ritualItemType;
                this.ritualItemName = ritualItemName;
                this.plName = plName;
                this.plLatitude = plLatitude;
                this.plLongitude = plLongitude;
            }
            Starter(){

            }
            void StartRitual(){
                switch (ritualItemType) {
                    case BAROMETER -> {
                        Barometer ritualItem = new Barometer(ritualItemName);
                        Place place = new Place(plName,plLatitude,plLongitude);
                        Gathering gathering = new Gathering(hattifatteners, ritualItem, place);
                        gathering.startRitual();
                    }
                    case NAMIRA_ALTAR -> {
                        NamiraAltar ritualItem = new NamiraAltar(ritualItemName);
                        Place place = new Place(plName,plLatitude,plLongitude);
                        Gathering gathering = new Gathering(hattifatteners, ritualItem, place);
                        gathering.startRitual();
                    }
                    case RESURRECTION_ALTAR -> {
                        ResurrectionAltar ritualItem = new ResurrectionAltar(ritualItemName);
                        Place place = new Place(plName,plLatitude,plLongitude);
                        Gathering gathering = new Gathering(hattifatteners, ritualItem, place);
                        gathering.startRitual();
                    }
                    case BOETHIAH_ALTAR -> {
                        BoethiahAltar ritualItem = new BoethiahAltar(ritualItemName);
                        Place place = new Place(plName,plLatitude,plLongitude);
                        Gathering gathering = new Gathering(hattifatteners, ritualItem, place);
                        gathering.startRitual();
                    }
                }
            }
        }

        Place place = new Place("лужайка в центре острова",11,67);
        NamiraAltar namiraAltar = new NamiraAltar("алтарь Намиры");
        Gathering gathering1 = new Gathering(hattifatteners,namiraAltar, place);
        gathering1.startRitual();

        Starter starter = new Starter(RitualItems.NAMIRA_ALTAR, "алтарь Намиры", "лужайка в центре острова", 11, 76);
        starter.StartRitual();

        Starter st = new Starter(){
            void setRitualItemType(RitualItems ritualItem){
                this.ritualItemType = ritualItem;
            }
            void setRitualItemName(String name){
                this.ritualItemName = name;
            }
            void setPlName(String name){
                this.plName = name;
            }
            void setPlLongitude(float longt){
                this.plLongitude = longt;
            }
            void setPlLatitude(float latitude){
                this.plLatitude = latitude;
            }
        };
        //st.StartRitual();

        Barometer barometer = new Barometer("барометр");
        Gathering gathering2 = new Gathering(hattifatteners,barometer, place);
        gathering2.startRitual();

        BoethiahAltar boethiahAltar = new BoethiahAltar("алтарь Боэтии");
        Gathering gathering3 = new Gathering(hattifatteners,boethiahAltar, place);




        /*ResurrectionAltar resurrectionAltar = new ResurrectionAltar("алтарь воскрешения");
        Gathering gathering4 = new Gathering(hattifatteners,resurrectionAltar, place);
        gathering4.startRitual();*/

        Hemulen hemulen = new Hemulen("Хемуль", Phrases.NUMBER_TWO_HUNDREED_NINTEEN_IN_MY_HERBARIUM);
        hemulen.collectFlowers(7);
        hemulen.countPetals();
        Method method = hattifatteners[0].getClass().getMethod("Prnt");
        method.setAccessible(true);
        method.invoke(null);
    }
}