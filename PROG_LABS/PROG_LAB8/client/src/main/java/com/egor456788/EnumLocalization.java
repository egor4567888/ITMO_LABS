package com.egor456788;

import com.egor456788.common.Devotions;
import com.egor456788.common.Genders;
import com.egor456788.common.Races;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class EnumLocalization {

    private static final String BUNDLE_NAME = "messages";
    private static Map<Locale,Map<String, Devotions>> localizedToDevotionMap = new HashMap<>();
    static {
        localizedToDevotionMap.put(new Locale("en"),new HashMap<String,Devotions>());
        localizedToDevotionMap.put(new Locale("ru"),new HashMap<String,Devotions>());
        localizedToDevotionMap.put(new Locale("is"),new HashMap<String,Devotions>());
        localizedToDevotionMap.put(new Locale("lv"),new HashMap<String,Devotions>());
        localizedToDevotionMap.put(new Locale("es","EC"),new HashMap<String,Devotions>());
        initializeDevotions();
    }
    private static Map<Locale,Map<String, Races>> localizedToRaceMap = new HashMap<>();
    static {
        localizedToRaceMap.put(new Locale("en"),new HashMap<String, Races>());
        localizedToRaceMap.put(new Locale("ru"),new HashMap<String,Races>());
        localizedToRaceMap.put(new Locale("is"),new HashMap<String,Races>());
        localizedToRaceMap.put(new Locale("lv"),new HashMap<String,Races>());
        localizedToRaceMap.put(new Locale("es","EC"),new HashMap<String,Races>());
        initializeRaces();
    }
    private static Map<Locale,Map<String, Genders>> localizedToGenderMap = new HashMap<>();
    static {
        localizedToGenderMap.put(new Locale("en"),new HashMap<String,Genders>());
        localizedToGenderMap.put(new Locale("ru"),new HashMap<String,Genders>());
        localizedToGenderMap.put(new Locale("is"),new HashMap<String,Genders>());
        localizedToGenderMap.put(new Locale("lv"),new HashMap<String,Genders>());
        localizedToGenderMap.put(new Locale("es","EC"),new HashMap<String,Genders>());
        initializeGenders();
    }

    public static void initializeRaces() {
        for(Locale locale: localizedToRaceMap.keySet()) {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
            for (Races myEnum : Races.values()) {
                String localizedValue = bundle.getString(myEnum.toString());
                localizedToRaceMap.get(locale).put(localizedValue, myEnum);
            }
        }
    }
    public static void initializeGenders() {
        for(Locale locale: localizedToGenderMap.keySet()) {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
            for (Genders myEnum : Genders.values()) {
                String localizedValue = bundle.getString(myEnum.toString());
                localizedToGenderMap.get(locale).put(localizedValue, myEnum);
            }
        }
    }
    public static void initializeDevotions() {
        for(Locale locale: localizedToDevotionMap.keySet()) {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
            for (Devotions myEnum : Devotions.values()) {
                String localizedValue = bundle.getString(myEnum.toString());
                localizedToDevotionMap.get(locale).put(localizedValue, myEnum);
            }
        }
    }

    public static String getDevotionByLocalizedValue(String localizedValue) {
        for(Locale locale: localizedToDevotionMap.keySet()){
            if(localizedToDevotionMap.get(locale).get(localizedValue)!=null)
                return localizedToDevotionMap.get(locale).get(localizedValue).toString().toUpperCase();
        }
        return null;
    }
    public static String getRaceByLocalizedValue(String localizedValue) {
        for(Locale locale: localizedToRaceMap.keySet()){
            if(localizedToRaceMap.get(locale).get(localizedValue)!=null)
                return localizedToRaceMap.get(locale).get(localizedValue).toString().toUpperCase();
        }
        return null;
    }
    public static String getGenderByLocalizedValue(String localizedValue) {
        for(Locale locale: localizedToGenderMap.keySet()){
            if(localizedToGenderMap.get(locale).get(localizedValue)!=null)
                return localizedToGenderMap.get(locale).get(localizedValue).toString().toUpperCase();
        }
        return null;
    }

    public static void main(String[] args) {
        Locale english = new Locale("en");
        Locale french = new Locale("ru");

        // Initialize for English locale
        System.out.println(getDevotionByLocalizedValue("Light")); // Output: OPTION_ONE

        // Initialize for French locale

        System.out.println(getDevotionByLocalizedValue("Свет")); // Output: OPTION_ONE
        System.out.println(getDevotionByLocalizedValue("Xyz"));
        System.out.println(getRaceByLocalizedValue("Хемуль"));
        System.out.println(getRaceByLocalizedValue("Hemulen"));
        System.out.println(getGenderByLocalizedValue("Male"));
        System.out.println(Races.valueOf(getRaceByLocalizedValue("Хемуль")));
    }
}
