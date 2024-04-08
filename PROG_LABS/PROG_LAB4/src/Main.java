import com.thoughtworks.xstream.XStream;
import common.*;
import entities.Entity;
import entities.Hattifattener;
import entities.Hemulen;
import ritualItems.Barometer;
import ritualItems.BoethiahAltar;
import ritualItems.NamiraAltar;
import ritualItems.ResurrectionAltar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String filePath = "/home/egor/ITMO_LABS/PROG_LABS/PROG_LAB4/src/input.txt";
        XStream xstream = new XStream();
        xstream.alias("Entity", Entity.class);
        xstream.alias("devotion", Devotions.class);
        xstream.alias("gender", Genders.class);
        File xmlFile = new File(filePath);
        //LinkedHashSet<Entity> hattifatteners = (LinkedHashSet<Entity>) xstream.fromXML(xmlFile);
        /*try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lines = new String[5];
                for (int i = 0; i < 5; i++)
                {
                    lines[i] = reader.readLine();
                }
                hattifatteners.add(new Hattifattener(line,Devotions.valueOf(lines[0]), Integer.parseInt(lines[1]), Integer.parseInt(lines[2]), Integer.parseInt(lines[3]), Genders.valueOf(lines[4])));
            }
        } catch (IOException e) {
            e.printStackTrace();
       }*/

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

        Hemulen hemulen = new Hemulen("Хемуль", Phrases.NUMBER_TWO_HUNDREED_NINTEEN_IN_MY_HERBARIUM, 25, 180, 120, Genders.HELICOPTER);
        hemulen.collectFlowers(7);
        hemulen.countPetals();
       // Method method = hattifatteners[0].getClass().getMethod("Prnt");
       // method.setAccessible(true);
        //method.invoke(null);
        ArrayList<Entity> list = new ArrayList<>(hattifatteners);
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++){
            System.out.println(list.get(i).getScore() + " " + list.get(i).getAge());
        }
    }
}