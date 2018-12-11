package com.trafficmon;

public class ActionsDecided {
    private static Actions actions=new ActionsTaken();
    public void changeActions(Actions actions){
        this.actions=actions;
    }//allows implementing different objects in tests
    public static Actions getActions(){
        return actions;
    }
}
