package com.egor456788.menegers;

import com.egor456788.entities.Entity;
import com.egor456788.entities.Hemulen;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Менеджер коллекции
 */

public class CollectionMeneger {
    private List<Entity> collection;
    final private LocalDateTime creationDate;

    public CollectionMeneger() {
        this.collection = new ArrayList<Entity>();
        this.creationDate = LocalDateTime.now();
    }

    public List<Entity> getCollection() {
        return collection;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void add(Entity entity){
        collection.add(entity);
    }
    public void addFromFile(String filePath){
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.alias("Entity", com.egor456788.entities.Entity.class);
        xstream.alias("devotion", com.egor456788.common.Devotions.class);
        xstream.alias("gender", com.egor456788.common.Genders.class);
        xstream.alias("condition", com.egor456788.common.Conditions.class);
        xstream.alias("phrase",com.egor456788.common.Phrases.class);
        xstream.alias("Hemulen", Hemulen.class);
        xstream.alias("Hattifattener", com.egor456788.entities.Hattifattener.class);
        File xmlFile = new File(filePath);
        collection = (List<Entity>) xstream.fromXML(xmlFile);
    }
}