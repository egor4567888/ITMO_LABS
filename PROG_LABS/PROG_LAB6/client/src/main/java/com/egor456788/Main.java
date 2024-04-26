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


    }
}