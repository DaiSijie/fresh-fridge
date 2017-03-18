package starthack.fridgetogo;


import android.util.Pair;

import java.util.Map;

public class Ingredient {
    private static String name;
    private static Pair<Integer, Boolean> quantity;
    private static Boolean inFridge;
    private static int minTemp;
    private static int maxTemp;
    private static int minHum;
    private static int maxHum;

    public Ingredient(String name, int quantity, boolean quantityInMass, boolean inFridge, int minTemp, int maxTemp, int minHum, int maxHum){
        this.name = name;
        this.quantity = new Pair(quantity, quantityInMass);
        this.inFridge = inFridge;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.minHum = minHum;
        this.maxHum = maxHum;
    }

    public Ingredient(String name, int quantity, boolean quantityInMass){
        this.name = name;
        this.quantity = new Pair(quantity, quantityInMass);
        this.minTemp = -1;
        this.maxTemp = -1;
        this.minHum = -1;
        this.maxHum = -1;
    }

    public String getName(){
        return name;
    }

    public Pair<Integer, Boolean> getQuantity(){
        return quantity;
    }

    public boolean isInFridge(){
        return inFridge;
    }

    public int getMinTemp(){
        return minTemp;
    }

    public int getMaxTemp(){
        return maxTemp;
    }

    public int getMinHum(){
        return minHum;
    }

    public int getMaxHum(){
        return maxHum;
    }
}
