package objects;

abstract class Obj {
    final protected String name;
    protected String leftProp="";
    protected String rightProp="";
    Obj(String name){
        this.name = name;
    }
    public void addLeftProp(String prop){
        this.leftProp += prop + " ";
    }
    public void addRightProp(String prop){
        this.rightProp += " " + prop;
    }
    public String getName(){
        return this.name;
    }
    protected String getNameWithProp(){
        return leftProp + name + rightProp;
    }
}
