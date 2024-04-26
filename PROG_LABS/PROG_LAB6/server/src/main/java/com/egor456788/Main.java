package com.egor456788;

import com.egor456788.common.*;
import com.egor456788.entities.Entity;
import com.egor456788.entities.Hattifattener;
import com.egor456788.entities.Hemulen;
import com.egor456788.ritualItems.Barometer;
import com.egor456788.ritualItems.BoethiahAltar;
import com.egor456788.ritualItems.NamiraAltar;
import com.egor456788.ritualItems.ResurrectionAltar;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;
import java.util.List;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Applicaton applicaton = new Applicaton();
        applicaton.run(args);

        String filePath = "/home/egor/IdeaProjects/PROG_LAB5/input.xml";
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.alias("Entity", Hattifattener.class);
        xstream.alias("devotion", Devotions.class);
        xstream.alias("gender", Genders.class);
        File xmlFile = new File(filePath);
        List<Entity> listHattifatteners = (List<Entity>) xstream.fromXML(xmlFile);
        LinkedHashSet<Entity> hattifatteners = new LinkedHashSet<>(listHattifatteners);
        for (Entity ent : hattifatteners)
            ent.setCondition(Conditions.ALIVE);

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
        Hattifattener x = new Hattifattener("as",Devotions.BAROMETER,1,1,1,Genders.MALE, Races.Hattifattner);
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

        Hemulen hemulen = new Hemulen("Хемуль", Phrases.NUMBER_TWO_HUNDREED_NINTEEN_IN_MY_HERBARIUM, 25, 180, 120, Genders.HELICOPTER, Races.HEMULEN);
        hemulen.collectFlowers(7);
        hemulen.countPetals();
       // Method method = hattifatteners[0].getClass().getMethod("Prnt");
       // method.setAccessible(true);
        //method.invoke(null);
        /*ArrayList<Entity> list = new ArrayList<>(hattifatteners);
        Collections.sort(list);
        for(Entity ent : list)
            System.out.println(ent);
        */
    }
}