package starthack.fridgetogo;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

/*
 *	Author:      Gilbert Maystre
 *	Date:        Mar 18, 2017
 */

public class Suggester {

    public Suggester(){

    }

    public void provideHint(String dishName, Boolean wasLiked){
        //Find dish name
        int index = -1;
        for(int i = 0; i < Database.getDishNames().size(); i++){
            if(Database.getDishNames().get(i).equals(dishName)){
                index = i;
                break;
            }
        }
        
        Database.c.feedDish(Database.getQuotes().get(index));
        Database.c.provideFeedback(wasLiked);
    }
    
    
    /**
     * Proposes an optimal dish for a given stock
     * @param inStock
     * @return The name of dish with scores (already sorted!)
     */
    public ArrayList<Entry<String, Double>> proposeDishes(){
        /* 
         * Stategy: For each doable dish, provide assign to it a real number a * classifier + b * urgency level
         * return sorted by this "score"
         */

        HashMap<String, Double> map = new HashMap<>();
        for(int i = 0; i < Database.getDishNames().size(); i++){
            if(isDoable(i)){
                //compute score
                map.put(Database.getDishNames().get(i), computeScore(i));
            }
        }

        //Now, order map to give best results
        ArrayList<Entry<String, Double>> toReturn = new ArrayList<>(map.entrySet());

        Collections.sort(toReturn, new Comparator<Entry<String, Double>>(){
            public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });


        return toReturn;
    }

    /**
     * Cool factor, Number of completable dishes, cheapCombinations
     * @param inStock
     * @return
     */
    public ArrayList<Entry<String, ArrayList<Double>>> proposeShopping(){
        /*
         * rationale: For each dish, we try to add missing ingredients and then compute the score,
         * 
         * A single missing ingredient is a big win, so we first prefer that to the score (heuristics)
         */

        //prepare the map!
        HashMap<String, ArrayList<Double>> toReturn = new HashMap<>();
        for(int i = 0; i < Database.getDishNames().size(); i++){
            if(!isDoable(i)){
                HashSet<String> missing = missingIngredients(i);
                //simulate a new stock! It's a hackaton right?
                HashMap<String, Date> yop = new HashMap<>();
                for(String m: missing) {
                    Date today = new Date();
                    Log.d("ttt", "today = "+today.getTime());
                    today.setDate(today.getDate() + 1);
                    Log.d("ttt", "new today = "+today.getTime());
                    Database.putNewObjectInFridge(m, today);
                    yop.put(m, today);
                }

                double score = computeScore(i);

                //rollback to old stock and update cool factors!
                for(String m: missing){
                    Database.removeObjectInFridge(m, yop.get(m));

                    //update stuff
                    if(!toReturn.containsKey(m))
                        toReturn.put(m, new ArrayList<Double>(Arrays.asList(new Double[]{0.d, 0.d, 999.d}))); 
                    toReturn.get(m).set(0, toReturn.get(m).get(0) + score);
                    toReturn.get(m).set(1, toReturn.get(m).get(1) + 1);
                    toReturn.get(m).set(2, Math.min(toReturn.get(m).get(2), missing.size()));
                }


                FridgeToGo.refreshPreferences();
            }
        }

        //Now, order map to give best results
        ArrayList<Entry<String, ArrayList<Double>>> toReturnn = new ArrayList<>(toReturn.entrySet());
        Collections.sort(toReturnn, new Comparator<Entry<String, ArrayList<Double>>>(){
            @Override
            public int compare(Entry<String, ArrayList<Double>> o1,Entry<String, ArrayList<Double>> o2) {
                Double cheapCombination1 = o1.getValue().get(2);
                Double cheapCombination2 = o2.getValue().get(2);
                Double coolFactor1 = o1.getValue().get(0);
                Double coolFactor2 = o2.getValue().get(0);
                
                if(cheapCombination1 == cheapCombination2)
                    return coolFactor2.compareTo(coolFactor1);
                else
                    return cheapCombination1.compareTo(cheapCombination2);
            }

        });

        return toReturnn;


    }

    private HashSet<String> missingIngredients(Integer dish){
        HashSet<String> toReturn = new HashSet<>();
        for(String ing : Database.getIngredientsForDish(dish)){
            if(!Database.getFridgeContent().containsKey(ing)){
                toReturn.add(ing);
            }
            else{
                boolean add = true;
                for(Date i : Database.getFridgeContent().get(ing)){//If there is at least one stock which is good, no need to buy.
                    Date today = new Date();
                    add = add && ((i.getTime() - today.getTime()) < 0);
                }
                if(add)
                    toReturn.add(ing);
            }
        }

        return toReturn;
    }

    private double computeScore(Integer dish){

        double likeable = Database.c.grade(Database.getQuotes().get(dish));

        /*
         * Urgency levels:
         * day > 5 : 0
         * 5 >= day > 2 : 1
         * 2 >= day >= 0 : 2
         */

        double urgencyLevel = 0;
        for(String ingredient: Database.getIngredientsForDish(dish)){
            for(Date date: Database.getFridgeContent().get(ingredient)){
                Date today = new Date();
                int delta = (int) (date.getTime() - today.getTime()) / (24  * 60 * 60 * 1000);
                int urgency = 0;
                if(5 >= delta && delta > 2)
                    urgency = 1;
                else if(2 >= delta)
                    urgency = 2;
                urgencyLevel = Math.max(urgencyLevel, urgency);
            }
        }


        return likeable + urgencyLevel;

    }

    private boolean isDoable(Integer dish){
        Date today = new Date();
        for(String i : Database.getIngredientsForDish(dish)){
            if(!Database.getFridgeContent().containsKey(i))
                return false;
            else{
                for(Date date: Database.getFridgeContent().get(i)){
                    if(date.getTime() - today.getTime() >= 0)
                        continue;
                }
            }
        }
        return true;
    }



}
