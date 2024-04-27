package com.egor456788;

import com.egor456788.entities.Entity;

import java.io.Serializable;

public class Request implements Serializable {
    final private String commandName;
    final private String args;
    final private Entity entity;
    final private Integer port;
    public Request(String commandName, String args, Entity entity, int port){
        this.commandName = commandName;
        this.args = args;
        this.entity = entity;
        this.port = port;
    }

    public Request(String commandName, String args, Entity entity) {
        this.commandName = commandName;
        this.args = args;
        this.entity = entity;
        port = 0;
    }

    public Request(String commandName, String args, int port){
        this.commandName = commandName;
        this.args = args;
        this.port = port;
        this.entity = null;
    }
    public Request(String commandName, Entity entity, int port){
        this.commandName = commandName;
        this.entity = entity;
        this.port = port;
        this.args = null;
    }

    public int getPort() {
        return port;
    }

    public Request(String commandName, int port){
        this.commandName = commandName;
        this.port = port;
        this.args = null;
        this.entity = null;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getArgs() {
        return args;
    }

    public Entity getEntity() {
        return entity;
    }
}
