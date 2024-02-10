package objects;

import common.Action;

public class Entity extends Obj{
    public Entity(String name){
        super(name);
    }
    public String leftAction(Action act){
        return act.getAction() + " " + this.getNameWithProp();
    }
    public String leftAction(Action act, Thing smth){
        return act.getAction() + " " + this.getNameWithProp()+ " " + smth.getNameWithProp();
    }
    public String leftAction(Action act,String options){
        return act.getAction() + " " + this.getNameWithProp() + " " + options;
    }
    public String leftAction(Action act,Thing smth, String options){
        return act.getAction() + " " + this.getNameWithProp() + " " + options + " " + smth.getNameWithProp();
    }
    public String rightAction(Action act){
        return this.getNameWithProp() + " " + act.getAction();
    }
    public String rightAction(Action act, Thing smth){
        return this.getNameWithProp() + " " + act.getAction() + " " + smth.getNameWithProp();
    }
    public String rightAction(Action act,String options){
        return this.getNameWithProp() + " " + act.getAction() + " " + options;
    }
    public String rightAction(Action act,Thing smth, String options){
        return this.getNameWithProp() + " " + act.getAction() + " " + options + " " + smth.getNameWithProp();
    }
}
