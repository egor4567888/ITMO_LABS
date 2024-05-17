package com.egor456788;

import com.egor456788.entities.Entity;

import java.io.Serializable;

public class Request implements Serializable {
    final private String commandName;
    final private String args;
    final private Entity entity;
    final private Integer port;
    final private String userName;
    final private String password;
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Request(String commandName, String args, Entity entity, int port, String userName, String password){
        this.commandName = commandName;
        this.args = args;
        this.entity = entity;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Request(String commandName, String args, Entity entity, String userName, String password) {
        this.commandName = commandName;
        this.args = args;
        this.entity = entity;
        this.userName = userName;
        this.password = password;
        port = 0;
    }

    public Request(String commandName, String args, int port, String userName, String password){
        this.commandName = commandName;
        this.args = args;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.entity = null;
    }
    public Request(String commandName, Entity entity, int port, String userName, String password){
        this.commandName = commandName;
        this.entity = entity;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.args = null;
    }

    public int getPort() {
        return port;
    }

    public Request(String commandName, int port, String userName, String password){
        this.commandName = commandName;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.args = null;
        this.entity = null;
    }
    public Request(String commandName, int port, String userName, String password,int userId){
        this.commandName = commandName;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.args = null;
        this.entity = null;
        this.userId = userId;
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
